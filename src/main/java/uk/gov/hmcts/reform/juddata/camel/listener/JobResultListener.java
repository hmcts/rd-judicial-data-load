package uk.gov.hmcts.reform.juddata.camel.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import uk.gov.hmcts.reform.data.ingestion.camel.route.ArchivalRoute;
import uk.gov.hmcts.reform.juddata.camel.util.JobStatus;

import java.sql.Timestamp;
import java.util.List;

import static java.lang.System.currentTimeMillis;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.JOB_ID;


@Component
@Slf4j
public class JobResultListener implements JobExecutionListener {

    @Value("${archival-file-names}")
    List<String> archivalFileNames;

    @Autowired
    ArchivalRoute archivalRoute;

    @Autowired
    ProducerTemplate producerTemplate;

    @Value("${archival-route}")
    String archivalRouteName;

    @Value("${logging-component-name}")
    private String logComponentName;

    @Autowired
    @Qualifier("springJdbcTemplate")
    protected JdbcTemplate jdbcTemplate;

    @Value("${insert-job-sql}")
    String insertAuditJob;

    @Autowired
    CamelContext camelContext;

    @Autowired
    @Qualifier("springJdbcTransactionManager")
    protected PlatformTransactionManager transactionManager;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        var params = new Object[]{new Timestamp(currentTimeMillis()),
            JobStatus.IN_PROGRESS.getStatus()};
        log.info("{}:: Batch Job execution Started", logComponentName);
        //Start Auditing Job Status
        int jobID = jdbcTemplate.update(insertAuditJob, params);
        camelContext.getGlobalOptions().put(JOB_ID, String.valueOf(jobID));
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        archivalRoute.archivalRoute(archivalFileNames);
        producerTemplate.sendBody(archivalRouteName, "starting Archival");
    }
}
