package uk.gov.hmcts.reform.juddata.camel.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.DataIngestionLibraryRunner;
import uk.gov.hmcts.reform.juddata.camel.servicebus.TopicPublisher;
import uk.gov.hmcts.reform.juddata.client.IdamClient;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.BooleanUtils.isNotTrue;
import static org.apache.commons.lang3.BooleanUtils.negate;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static uk.gov.hmcts.reform.juddata.camel.util.FeatureToggleServiceImpl.JRD_ASB_FLAG;
import static uk.gov.hmcts.reform.juddata.camel.util.JobStatus.FAILED;
import static uk.gov.hmcts.reform.juddata.camel.util.JobStatus.FILE_LOAD_FAILED;
import static uk.gov.hmcts.reform.juddata.camel.util.JobStatus.IN_PROGRESS;
import static uk.gov.hmcts.reform.juddata.camel.util.JobStatus.SUCCESS;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.ASB_PUBLISHING_STATUS;

@Component
@Slf4j
public class JrdDataIngestionLibraryRunner extends DataIngestionLibraryRunner {

    @Autowired
    @Qualifier("springJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @Value("${select-job-status-sql}")
    String selectJobStatus;

    @Value("${logging-component-name}")
    String logComponentName;

    @Value("${get-sidam-ids}")
    String getSidamIds;

    @Value("${scheduler-audit-failure}")
    String failedAuditFileCount;

    @Autowired
    TopicPublisher topicPublisher;

    @Autowired
    FeatureToggleService featureToggleService;

    @Value("${launchdarkly.sdk.environment}")
    String environment;

    @Autowired
    JrdSidamTokenService jrdSidamTokenService;

    @Value("${update-sidam-ids}")
    String updateSidamIds;

    @Value("${update-job-sql}")
    String updateJobStatus;

    public JrdDataIngestionLibraryRunner() {
        super();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run(Job job, JobParameters params) throws Exception {
        try {
            super.run(job, params);
            Pair<String, String> jobDetails = getJobDetails();
            //After Job completes Publish message in ASB and toggle off for prod with launch Darkly & one
            //more explicit check to  avoid executing in prod Should be removed in prod release
            if (featureToggleService.isFlagEnabled(JRD_ASB_FLAG)
                && negate(environment.startsWith("prod"))) {
                mapAndPublishSidamIds(jobDetails.getLeft(), jobDetails.getRight());
                log.info("{}:: completed JrdDataIngestionLibraryRunner for JOB id {}",
                    logComponentName, getJobDetails().getLeft());
                //Update JRD DB with ASB Status
                updateAsbStatus(jobDetails.getLeft(), SUCCESS.status);
            }
        } catch (Exception ex) {
            String publishStatus = camelContext.getGlobalOptions().get(ASB_PUBLISHING_STATUS);
            publishStatus = (nonNull(publishStatus) && isNotTrue(publishStatus
                .equalsIgnoreCase(IN_PROGRESS.getStatus())))
                ? publishStatus : FILE_LOAD_FAILED.getStatus();
            updateAsbStatus(getJobDetails().getLeft(), publishStatus);
            throw ex;
        }
    }

    public Pair<String, String> getJobDetails() {
        Optional<Pair<String, String>> pair = Optional.of(jdbcTemplate.queryForObject(selectJobStatus, (rs, i) ->
            Pair.of(rs.getString(1), rs.getString(2))));
        final String jobId = pair.map(Pair::getLeft).orElse(EMPTY);
        final String jobStatus = pair.map(Pair::getRight).orElse(EMPTY);
        return Pair.of(jobId, jobStatus);
    }

    private void mapAndPublishSidamIds(String jobId, String jobStatus) {
        Set<IdamClient.User> sidamUsers = jrdSidamTokenService.getSyncFeed();
        updateSidamIds(sidamUsers);
        List<String> sidamIds = jdbcTemplate.query(getSidamIds, JrdConstants.ROW_MAPPER);
        int failedFileCount = jdbcTemplate.queryForObject(failedAuditFileCount, Integer.class);
        if (failedFileCount > 0) {
            log.warn("{}:: JRD load failed, hence no publishing sidam id's to ASB", logComponentName, jobId);
            camelContext.getGlobalOptions().put(ASB_PUBLISHING_STATUS, FILE_LOAD_FAILED.getStatus());
            updateAsbStatus(jobId, FILE_LOAD_FAILED.getStatus());
            return;
        }

        //In case on NO sidam id's matched for object id's nothing to publish in ASB
        if (isEmpty(sidamIds)) {
            log.warn("{}:: No Sidam id exists in JRD  for publishing in ASB for JOB id {}",
                logComponentName, jobId);
            updateAsbStatus(jobId, SUCCESS.getStatus());
            return;
        }
        publishMessage(jobStatus, sidamIds, jobId);
    }

    private void updateAsbStatus(String jobId, String jobStatus) {
        //Update JRD DB with Publishing Status
        String publishingStatus = StringUtils.isEmpty(jobStatus) ? SUCCESS.getStatus() : jobStatus;
        jdbcTemplate.update(updateJobStatus, publishingStatus, Integer.valueOf(jobId));
    }

    private void updateSidamIds(Set<IdamClient.User> sidamUsers) {
        List<Pair<String, String>> sidamObjectId = new ArrayList<>();
        sidamUsers.stream().filter(user -> nonNull(user.getSsoId())).forEach(s -> {
            sidamObjectId.add(Pair.of(s.getId(), s.getSsoId()));
        });

        jdbcTemplate.batchUpdate(
            updateSidamIds,
            sidamObjectId,
            10,
            new ParameterizedPreparedStatementSetter<Pair<String, String>>() {
                public void setValues(PreparedStatement ps, Pair<String, String> argument) throws SQLException {
                    ps.setString(1, argument.getLeft());
                    ps.setString(2, argument.getRight());
                }
            });
    }

    private void publishMessage(String status, List<String> sidamIds, String jobId) {
        try {
            if ((IN_PROGRESS.getStatus().equals(status))
                || (FAILED.getStatus()).equals(status) && isNotEmpty(sidamIds)) {
                //Publish or retry Message in ASB
                log.info("{}:: Publishing/Retrying JRD messages in ASB for Job Id ", logComponentName, jobId);
                topicPublisher.sendMessage(sidamIds, jobId);
            }
        } catch (Exception ex) {
            log.error("{}:: Publishing/Retrying JRD messages in ASB failed for Job Id", logComponentName, jobId);
            camelContext.getGlobalOptions().put(ASB_PUBLISHING_STATUS, FAILED.getStatus());
            updateAsbStatus(jobId, FAILED.getStatus());
            throw ex;
        }
    }
}
