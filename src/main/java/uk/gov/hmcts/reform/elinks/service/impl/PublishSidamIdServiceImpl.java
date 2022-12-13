package uk.gov.hmcts.reform.elinks.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.data.ingestion.camel.service.IEmailService;
import uk.gov.hmcts.reform.data.ingestion.camel.service.dto.Email;
import uk.gov.hmcts.reform.elinks.configuration.ElinkEmailConfiguration;
import uk.gov.hmcts.reform.elinks.repository.DataloadSchedularAuditRepository;
import uk.gov.hmcts.reform.elinks.response.SchedulerJobStatusResponse;
import uk.gov.hmcts.reform.elinks.service.FeatureToggleService;
import uk.gov.hmcts.reform.elinks.service.PublishSidamIdService;
import uk.gov.hmcts.reform.elinks.servicebus.ElinkTopicPublisher;
import uk.gov.hmcts.reform.elinks.util.RefDataConstants;
import uk.gov.hmcts.reform.elinks.util.JobStatus;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static java.lang.System.currentTimeMillis;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.BooleanUtils.isNotTrue;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.util.CollectionUtils.isEmpty;
import static uk.gov.hmcts.reform.elinks.util.JobStatus.FAILED;
import static uk.gov.hmcts.reform.elinks.util.JobStatus.FILE_LOAD_FAILED;
import static uk.gov.hmcts.reform.elinks.util.JobStatus.IN_PROGRESS;
import static uk.gov.hmcts.reform.elinks.util.JobStatus.SUCCESS;
import static uk.gov.hmcts.reform.elinks.util.RefDataConstants.ASB_PUBLISHING_STATUS;
import static uk.gov.hmcts.reform.elinks.util.RefDataConstants.CONTENT_TYPE_PLAIN;
import static uk.gov.hmcts.reform.elinks.util.SqlContants.INSERT_AUDIT_JOB;
import static uk.gov.hmcts.reform.elinks.util.SqlContants.SELECT_JOB_STATUS_SQL;
import static uk.gov.hmcts.reform.elinks.util.SqlContants.GET_DISTINCT_SIDAM_ID;
import static uk.gov.hmcts.reform.elinks.util.SqlContants.UPDATE_JOB_SQL;



@Slf4j
@Service
public class PublishSidamIdServiceImpl implements PublishSidamIdService {

    private static final String ZERO = "0";

    private static final String JRD_ELINKS_ASB_FLAG = "rd_jud_elinks";

    @Autowired
    @Qualifier("springJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @Value("${elink-update-job-sql}")
    String updateJobStatus;

    @Value("${elink-select-job-status-sql}")
    String selectJobStatus;

    @Value("${previous-day-job-status}")
    String selectPreviousDayJobStatus;

    @Value("${logging-component-name}")
    String logComponentName;

    @Value("${elink-get-sidam-ids}")
    String getSidamIds;

    @Autowired
    ElinkTopicPublisher elinkTopicPublisher;

    @Autowired
    ElinkEmailConfiguration emailConfiguration;

    @Value("${launchdarkly.sdk.environment}")
    String environment;

    @Value("${elink-insert-job-sql}")
    String insertAuditJob;

    @Autowired
    IEmailService emailService;

    @Autowired
    FeatureToggleService featureToggleService;

    private int sidamIdcount;

    @Autowired
    DataloadSchedularAuditRepository dataloadSchedularAuditRepository;

    public SchedulerJobStatusResponse publishSidamIdToAsb() throws Exception{

        try {
            jobBeforePublishingMessageToASB();
            //Get the job details from dataload_schedular_job table
            Pair<String, String>  jobDetails = getJobDetails(SELECT_JOB_STATUS_SQL);

            // Get all sidam id's from the judicial_user_profile table
            List<String> sidamIds = jdbcTemplate.query(GET_DISTINCT_SIDAM_ID, RefDataConstants.ROW_MAPPER);

            sidamIdcount = sidamIds.size();

            log.info("{}::Total SIDAM Id count from JUD_Database: {}", logComponentName, sidamIdcount);
            if (isEmpty(sidamIds)) {
                log.warn("{}:: No Sidam id exists in JRD for publishing in ASB for JOB id {}",
                        logComponentName);
                updateAsbStatus(jobDetails.getLeft(),SUCCESS.getStatus());
            }

            publishMessage(jobDetails.getRight(), sidamIds, jobDetails.getLeft());

            log.info("{}:: completed Publish SidamId to ASB with JOB Id: {}  ", logComponentName, jobDetails.getLeft());

           /* We have to uncomment it when feature toggle button

           if (featureToggleService.isFlagEnabled(JRD_ELINKS_ASB_FLAG)) {
                if (isEmpty(sidamIds)) {
                    log.warn("{}:: No Sidam id exists in JRD  for publishing in ASB for JOB id {}",
                            logComponentName);
                    updateAsbStatus(jobDetails.getLeft(),SUCCESS.getStatus());
                }
                publishMessage(jobDetails.getRight(), sidamIds, jobDetails.getLeft());
                log.info("{}:: completed Publish SidamId to ASB {} with JOB Id:", logComponentName,
                        getJobDetails(SELECT_JOB_STATUS_SQL).getLeft());
            }*/
            return new SchedulerJobStatusResponse(jobDetails.getLeft(),jobDetails.getRight(),sidamIds);
        } catch (Exception ex) {
            String publishStatus = ASB_PUBLISHING_STATUS;
            publishStatus = (nonNull(publishStatus) && isNotTrue(publishStatus
                    .equalsIgnoreCase(IN_PROGRESS.getStatus())))
                    ? publishStatus : FILE_LOAD_FAILED.getStatus();

            updateAsbStatus(getJobDetails(SELECT_JOB_STATUS_SQL).getLeft(), publishStatus);

            throw ex;
        }
    }

    private void jobBeforePublishingMessageToASB() {
        var params = new Object[]{new Timestamp(currentTimeMillis()),
                JobStatus.IN_PROGRESS.getStatus()};
        log.info("{}:: Batch Job execution Started", logComponentName);
        //Start Auditing Job Status
        jdbcTemplate.update(INSERT_AUDIT_JOB, params);
    }
    private Pair<String, String> getJobDetails(String jobStatusQuery) {
        Optional<Pair<String, String>> pair = getJobStatus(jobStatusQuery);

        final String jobId = pair.map(Pair::getLeft).orElse(ZERO);
        final String jobStatus = pair.map(Pair::getRight).orElse(EMPTY);
        return Pair.of(jobId, jobStatus);
    }

    private Optional<Pair<String, String>> getJobStatus(String jobStatusQuery) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(jobStatusQuery, (rs, i) ->
                    Pair.of(rs.getString(1), rs.getString(2))));
        } catch (EmptyResultDataAccessException ex) {
            log.info("No record found in table dataload_schedular_job");
            return Optional.empty();
        }
    }


    public void publishMessage(String status, List<String> sidamIds, String jobId) {
        try {
            if ((IN_PROGRESS.getStatus().equals(status))
                    || (FAILED.getStatus()).equals(status) && isNotEmpty(sidamIds)) {
                //Publish or retry Message in ASB
                log.info("{}:: Publishing/Retrying JRD messages in ASB for Job Id {}", logComponentName, jobId);
                elinkTopicPublisher.sendMessage(sidamIds, jobId);
                updateAsbStatus(jobId, SUCCESS.getStatus());
                log.info("{}:: Updated Total distinct Sidam Ids to ASB: {}", logComponentName, sidamIdcount);

            }
        } catch (Exception ex) {
            log.error("ASB Failure Root cause - {}", ex.getMessage());
            ElinkEmailConfiguration.MailTypeConfig mailTypeConfig = emailConfiguration.getMailTypes().get("asb");
            final String logMessage = String.format(mailTypeConfig.getSubject(), jobId);
            log.error("{}:: {}", logComponentName, logMessage);
            updateAsbStatus(jobId, FAILED.getStatus());
            if (mailTypeConfig.isEnabled()) {
                Email email = Email.builder()
                        .contentType(CONTENT_TYPE_PLAIN)
                        .from(mailTypeConfig.getFrom())
                        .to(mailTypeConfig.getTo())
                        .messageBody(String.format(mailTypeConfig.getBody(), jobId))
                        .subject(String.format(mailTypeConfig.getSubject(), environment))
                        .build();
                emailService.sendEmail(email);
            }
            throw ex;
        }
    }

    private void updateAsbStatus(String jobId,String jobStatus) {
        //Update elinks DB with Publishing Status
        String publishingStatus = StringUtils.isEmpty(jobStatus) ? SUCCESS.getStatus() : jobStatus;

        jdbcTemplate.update(UPDATE_JOB_SQL, publishingStatus, Integer.valueOf(jobId));

    }


}
