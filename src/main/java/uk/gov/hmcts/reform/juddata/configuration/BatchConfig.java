package uk.gov.hmcts.reform.juddata.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.juddata.camel.listener.JobResultListener;
import uk.gov.hmcts.reform.juddata.camel.service.JudicialAuditServiceImpl;
import uk.gov.hmcts.reform.juddata.camel.task.LeafRouteTask;
import uk.gov.hmcts.reform.juddata.camel.task.ParentRouteTask;

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    JudicialAuditServiceImpl schedulerJudicialAuditServiceImpl;

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

    @Autowired
    JobResultListener jobResultListener;

    @Autowired
    JobBuilderFactory jobBuilderFactory;


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
    public Job runRoutesJob() {

        return jobBuilderFactory.get(jobName)
                .start(stepLeafRoute())
                .listener(jobResultListener)
                .on("*").to(stepOrchestration())
                .end()
                .build();
    }
}
