package uk.gov.hmcts.reform.juddata.camel.task;

import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.LEAF_ROUTE;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.route.DataLoadRoute;
import uk.gov.hmcts.reform.juddata.camel.util.JrdExecutor;

@Component
@Slf4j
public class LeafRouteTask implements Tasklet {

    @Autowired
    CamelContext camelContext;

    @Value("${start-leaf-route}")
    private String startLeafRoute;

    @Autowired
    JrdExecutor jrdExecutor;

    @Value("${routes-to-execute-leaf}")
    List<String> routesToExecute;

    @Autowired
    DataLoadRoute dataLoadRoute;

    @Value("${logging-component-name}")
    private String logComponentName;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("{}:: LeafRouteTask starts::", logComponentName);
        dataLoadRoute.startRoute(startLeafRoute, routesToExecute);
        String status = jrdExecutor.execute(camelContext, LEAF_ROUTE, startLeafRoute);
        log.info("{}:: LeafRouteTask completes with {}::", logComponentName, status);
        return RepeatStatus.FINISHED;
    }
}
