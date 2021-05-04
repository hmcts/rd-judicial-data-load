package uk.gov.hmcts.reform.juddata.camel.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.DataIngestionLibraryRunner;
import uk.gov.hmcts.reform.juddata.camel.servicebus.TopicPublisher;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang.BooleanUtils.isFalse;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static uk.gov.hmcts.reform.juddata.camel.util.JobStatus.FAILED;
import static uk.gov.hmcts.reform.juddata.camel.util.JobStatus.IN_PROGRESS;
import static uk.gov.hmcts.reform.juddata.camel.util.JobStatus.SUCCESS;

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

    @Value("${update-job-sql}")
    String updateJobStatus;

    @Value("${get-sidam-ids}")
    String getSidamIds;

    @Autowired
    TopicPublisher topicPublisher;

    public JrdDataIngestionLibraryRunner() {
        super();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run(Job job, JobParameters params) throws Exception {
        super.run(job, params);
        //To do add sidam calls to get Sidam id for Object id once Sidam elastic APi ready
        Optional<Pair<String, String>> pair = Optional.of(jdbcTemplate.queryForObject(selectJobStatus, (rs, i) ->
            Pair.of(rs.getString(1), rs.getString(2))));

        String jobId = pair.map(val -> val.getLeft()).orElse(EMPTY);
        String jobStatus = pair.map(val -> val.getRight()).orElse(EMPTY);

        List<String> sidamIds = jdbcTemplate.query(getSidamIds, JrdConstants.ROW_MAPPER);

        if (isFalse(validateSidamIdsExists(jobId, sidamIds))) {
            return;
        }
        //After Job completes Publish message in ASB
        publishMessage(jobStatus, sidamIds, jobId);
        updateJobCompletion(SUCCESS, jobId);
        log.info("{}:: completed JrdDataIngestionLibraryRunner for JOB id {}", logComponentName, jobId);
    }

    private void updateJobCompletion(JobStatus success, String jobId) {
        jdbcTemplate.update(updateJobStatus, success.getStatus(),
            Integer.valueOf(jobId));
    }

    private boolean validateSidamIdsExists(String jobId, List<String> sidamIds) {
        var returnFlag = true;
        if (isEmpty(sidamIds)) {
            log.warn("{}:: No Sidam id exists in JRD  for publishing in ASB for JOB id {}", logComponentName, jobId);
            updateJobCompletion(SUCCESS, jobId);
            returnFlag = false;
        }
        return returnFlag;
    }

    private void publishMessage(String status, List<String> sidamIds, String jobId) {
        try {
            if ((IN_PROGRESS.getStatus().equals(status))
                || (FAILED.getStatus()).equals(status) && isNotEmpty(sidamIds)) {
                //Publish or retry Message in ASB
                log.info("{}:: Publishing/Retrying JRD messages in ASB for Job Id ", logComponentName, jobId);
                topicPublisher.sendMessage(sidamIds);
            }
        } catch (Exception ex) {
            log.error("{}:: Publishing/Retrying JRD messages in ASB failed for Job Id", logComponentName, jobId);
            updateJobCompletion(FAILED, jobId);
            throw ex;
        }
    }

}
