package uk.gov.hmcts.reform.juddata.camel.task;

import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.IS_EXCEPTION_HANDLED;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.LEAF_ROUTE;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SCHEDULER_STATUS;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.service.AuditProcessingService;
import uk.gov.hmcts.reform.juddata.camel.util.DataLoadUtil;

@Component
@Slf4j
public class LeafRouteTask implements Tasklet {

    @Autowired
    CamelContext camelContext;

    @Autowired
    DataLoadUtil dataLoadUtil;

    @Autowired
    ProducerTemplate producerTemplate;

    @Value("${start-leaf-route}")
    private String startLeafRoute;

    @Autowired
    AuditProcessingService schedulerAuditProcessingService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            camelContext.getGlobalOptions().remove(IS_EXCEPTION_HANDLED);
            camelContext.getGlobalOptions().remove(SCHEDULER_STATUS);
            dataLoadUtil.setGlobalConstant(camelContext, LEAF_ROUTE);
            producerTemplate.sendBody(startLeafRoute, "starting JRD leaf routes though scheduler");
        } catch (Exception ex) {
            log.error("::leaf-route failed::", ex.getMessage());
        } finally {
            //runs Job Auditing
            schedulerAuditProcessingService.auditSchedulerStatus(camelContext);
        }

        return RepeatStatus.FINISHED;
    }
}
