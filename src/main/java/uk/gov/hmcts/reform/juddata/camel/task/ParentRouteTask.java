package uk.gov.hmcts.reform.juddata.camel.task;

import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.ERROR_MESSAGE;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.IS_EXCEPTION_HANDLED;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.JUDICIAL_USER_PROFILE_ORCHESTRATION;
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
import uk.gov.hmcts.reform.juddata.camel.service.EmailService;
import uk.gov.hmcts.reform.juddata.camel.util.DataLoadUtil;

@Component
@Slf4j
public class ParentRouteTask implements Tasklet {

    @Autowired
    ProducerTemplate producerTemplate;

    @Value("${start-route}")
    private String startRoute;

    @Autowired
    CamelContext camelContext;

    @Autowired
    DataLoadUtil dataLoadUtil;

    @Autowired
    AuditProcessingService auditProcessingService;

    @Autowired
    EmailService emailService;


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        try {
            log.info("::ParentRouteTask starts::");
            camelContext.getGlobalOptions().remove(IS_EXCEPTION_HANDLED);
            camelContext.getGlobalOptions().remove(SCHEDULER_STATUS);
            dataLoadUtil.setGlobalConstant(camelContext, JUDICIAL_USER_PROFILE_ORCHESTRATION);
            producerTemplate.sendBody(startRoute, "starting JRD orchestration");
            log.info("::ParentRouteTask completes::");
        } catch (Exception ex) {
            //Camel override error stack with route failed hence grabbing exception form context
            String errorMessage = camelContext.getGlobalOptions().get(ERROR_MESSAGE);

            auditProcessingService.auditException(camelContext, errorMessage);
            log.error("::judicial-user-profile-orchestration route failed::",  errorMessage);
            //check mail flag and send mail
            emailService.sendEmail(errorMessage);
        } finally {
            //runs Job Auditing
            auditProcessingService.auditSchedulerStatus(camelContext);

        }
        return RepeatStatus.FINISHED;
    }
}
