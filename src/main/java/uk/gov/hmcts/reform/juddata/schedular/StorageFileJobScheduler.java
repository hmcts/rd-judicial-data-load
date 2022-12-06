package uk.gov.hmcts.reform.juddata.schedular;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.service.AuditServiceImpl;
import uk.gov.hmcts.reform.juddata.camel.util.JrdDataIngestionLibraryRunner;

import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.DIRECT_JRD;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.START_ROUTE;

@Component
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class StorageFileJobScheduler {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    @Value("${batchjob-name}")
    String jobName;

    private static String logComponentName;

    @Autowired
    JrdDataIngestionLibraryRunner dataIngestionLibraryRunner;

    @Autowired
    AuditServiceImpl judicialAuditServiceImpl;

    @Value("${loggingComponentName}")
    private String loggingComponentName;

    @Scheduled(cron = "${file.schedule}")
    public void fileProcessingCronJob() throws Exception {

        JobParameters params = new JobParametersBuilder()
                .addString(jobName, String.valueOf(System.currentTimeMillis()))
                .addString(START_ROUTE, DIRECT_JRD)
                .toJobParameters();
        dataIngestionLibraryRunner.run(job, params);
    }
}
