package uk.gov.hmcts.reform.juddata.scheduler;

import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.IS_EXCEPTION_HANDLED;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.JUDICIAL_USER_PROFILE_ORCHESTRATION;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.ORCHESTRATED_ROUTE;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SCHEDULER_STATUS;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.route.ParentOrchestrationRoute;
import uk.gov.hmcts.reform.juddata.camel.service.AuditProcessingService;
import uk.gov.hmcts.reform.juddata.camel.util.DataLoadUtil;

@Component
@Slf4j
public class JrdUserProfileDataLoadScheduler {

    @Autowired
    CamelContext camelContext;

    @Autowired
    ParentOrchestrationRoute parentOrchestrationRoute;

    @Autowired
    ProducerTemplate producerTemplate;

    @Value("${start-route}")
    private String startRoute;

    @Autowired
    DataLoadUtil dataLoadUtil;

    @Autowired
    AuditProcessingService schedulerAuditProcessingService;

    @PostConstruct
    public void postConstruct() throws Exception {
        camelContext.start();
        camelContext.getGlobalOptions().put(ORCHESTRATED_ROUTE, JUDICIAL_USER_PROFILE_ORCHESTRATION);
        parentOrchestrationRoute.startRoute();
    }

    @Scheduled(cron = "${scheduler.camel-route-config}")
    public void runJrdScheduler() {
        try {
            camelContext.getGlobalOptions().remove(IS_EXCEPTION_HANDLED);
            camelContext.getGlobalOptions().remove(SCHEDULER_STATUS);
            dataLoadUtil.setGlobalConstant(camelContext, JUDICIAL_USER_PROFILE_ORCHESTRATION);
            producerTemplate.sendBody(startRoute, "starting JRD orchestration");
        } catch (Exception ex) {
            log.error("::judicial-user-profile-orchestration route failed::",  ex.getMessage());
        } finally {
            //runs Job Auditing
            schedulerAuditProcessingService.auditSchedulerStatus(camelContext);
        }
    }
}