package uk.gov.hmcts.reform.juddata.camel.task;

import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.JUDICIAL_REF_DATA_ORCHESTRATION;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.ORCHESTRATED_ROUTE;

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
public class ParentRouteTask implements Tasklet {

    @Value("${start-route}")
    private String startRoute;

    @Autowired
    CamelContext camelContext;

    @Autowired
    JrdExecutor jrdExecutor;

    @Autowired
    DataLoadRoute dataLoadRoute;

    @Value("${routes-to-execute-orchestration}")
    List<String> routesToExecute;

    @Value("${logging-component-name}")
    private String logComponentName;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("{}:: ParentRouteTask starts::", logComponentName);
        camelContext.getGlobalOptions().put(ORCHESTRATED_ROUTE, JUDICIAL_REF_DATA_ORCHESTRATION);
        dataLoadRoute.startRoute(startRoute, routesToExecute);
        String status = jrdExecutor.execute(camelContext, JUDICIAL_REF_DATA_ORCHESTRATION, startRoute);
        log.info("{}:: ParentRouteTask completes with {}::", logComponentName, status);
        return RepeatStatus.FINISHED;
    }
}
