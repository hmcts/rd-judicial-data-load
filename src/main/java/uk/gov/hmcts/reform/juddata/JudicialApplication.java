package uk.gov.hmcts.reform.juddata;

import static net.logstash.logback.encoder.org.apache.commons.lang3.BooleanUtils.negate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import uk.gov.hmcts.reform.juddata.camel.service.AuditProcessingService;

@SpringBootApplication(scanBasePackages = "uk.gov.hmcts.reform.juddata")
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class
@Slf4j
public class JudicialApplication implements ApplicationRunner {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    @Value("${batchjob-name}")
    String jobName;

    @Autowired
    AuditProcessingService auditProcessingService;

    public static void main(final String[] args) {
        ApplicationContext context = SpringApplication.run(JudicialApplication.class);
        int exitCode = SpringApplication.exit(context);
        log.info("Judicial Application exiting with exit code " + exitCode);
        System.exit(exitCode);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString(jobName, String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        if (negate(auditProcessingService.isAuditingCompleted())) {
            jobLauncher.run(job, params);
            log.info("::Judicial Application running first time for day ::");
        } else {
            log.info("::no run of Judicial Application as it has ran for the day::");
        }
    }
}
