package uk.gov.hmcts.reform.juddata;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "uk.gov.hmcts.reform.juddata")
@EnableScheduling
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class
public class JudicialApplication implements CommandLineRunner {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    @Value("${batchjob-name}")
    String jobName;
    
    public static void main(final String[] args) {
        ApplicationContext context = SpringApplication.run(JudicialApplication.class, args);
        SpringApplication.exit(context);
    }

    @Override
    public void run(String... args) throws Exception {

        JobParameters params = new JobParametersBuilder()
                .addString(jobName, String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job, params);
    }
}
