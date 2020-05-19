package uk.gov.hmcts.reform.juddata.configuration;

import static net.logstash.logback.encoder.org.apache.commons.lang3.BooleanUtils.negate;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import uk.gov.hmcts.reform.juddata.camel.service.AuditProcessingService;
import uk.gov.hmcts.reform.juddata.camel.task.LeafRouteTask;
import uk.gov.hmcts.reform.juddata.camel.task.ParentRouteTask;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    AuditProcessingService schedulerAuditProcessingService;

    @Value("${leaf-route-task}")
    String taskLeaf;

    @Value("${parent-route-task}")
    String taskParent;

    @Value("${batchjob-name}")
    String jobName;

    @Autowired
    ParentRouteTask parentRouteTask;

    @Autowired
    LeafRouteTask leafRouteTask;


    @Bean
    public Step stepLeafRoute() {
        return steps.get(taskLeaf)
                .tasklet(leafRouteTask)
                .build();
    }

    @Bean
    public Step stepOrchestration() {
        return steps.get(taskParent)
                .tasklet(parentRouteTask)
                .build();
    }

    @Bean
    @Lazy
    public Job runRoutesJob() {

        if (negate(schedulerAuditProcessingService.isAuditingCompleted())) {
            return jobs.get(jobName)
                    .incrementer(new RunIdIncrementer())
                    .start(stepLeafRoute())
                    .next(stepOrchestration())
                    .build();
        }

        return new SimpleJob();
    }
}
