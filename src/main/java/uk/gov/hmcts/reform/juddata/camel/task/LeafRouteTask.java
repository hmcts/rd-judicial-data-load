package uk.gov.hmcts.reform.juddata.camel.task;

import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.LEAF_ROUTE;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.util.JrdTask;

@Component
@Slf4j
public class LeafRouteTask implements Tasklet {

    @Autowired
    CamelContext camelContext;

    @Value("${start-leaf-route}")
    private String startLeafRoute;

    @Autowired
    JrdTask jrdTask;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("::LeafRouteTask starts::");
        jrdTask.execute(camelContext, LEAF_ROUTE, startLeafRoute);
        log.info("::LeafRouteTask completes::");
        return RepeatStatus.FINISHED;
    }
}
