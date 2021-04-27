package uk.gov.hmcts.reform.juddata.camel.util;

import lombok.extern.slf4j.Slf4j;
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

import static org.apache.commons.lang.BooleanUtils.isFalse;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static uk.gov.hmcts.reform.juddata.camel.util.JobStatus.FAILED;
import static uk.gov.hmcts.reform.juddata.camel.util.JobStatus.IN_PROGRESS;
import static uk.gov.hmcts.reform.juddata.camel.util.JobStatus.SUCCESS;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.JOB_ID;

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
        String status = jdbcTemplate.queryForObject(selectJobStatus, String.class);
        String jobId = camelContext.getGlobalOptions().get(JOB_ID);

        List<String> sidamIds = jdbcTemplate.query(getSidamIds, JrdConstants.ROW_MAPPER);

        if (isFalse(validateSidamIdsExists(jobId, sidamIds))) {
            return;
        }
        //After Job completes Publish message in ASB
        publishMessage(status, sidamIds, jobId);
        jdbcTemplate.update(updateJobStatus, SUCCESS.getStatus(),
            Integer.valueOf(jobId));
        log.info("{}:: completed JrdDataIngestionLibraryRunner for JOB id {}", logComponentName, jobId);
    }

    private boolean validateSidamIdsExists(String jobId, List<String> sidamIds) {
        var returnFlag = true;
        if (isEmpty(sidamIds)) {
            log.warn("{}:: No Sidam id exists in JRD  for publishing in ASB for JOB id {}", logComponentName, jobId);
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
            jdbcTemplate.update(updateJobStatus, FAILED.getStatus(),
                Integer.valueOf(jobId));
            throw ex;
        }
    }
}
