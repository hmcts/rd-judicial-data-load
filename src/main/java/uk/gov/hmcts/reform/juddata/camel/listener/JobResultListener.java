package uk.gov.hmcts.reform.juddata.camel.listener;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.route.ArchivalRoute;


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

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("{}:: Batch Job execution Started", logComponentName);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        archivalRoute.archivalRoute(archivalFileNames);
        producerTemplate.sendBody(archivalRouteName, "starting Archival");
    }
}
