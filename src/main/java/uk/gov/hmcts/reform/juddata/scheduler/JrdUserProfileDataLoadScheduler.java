package uk.gov.hmcts.reform.juddata.scheduler;

import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.JUDICIAL_USER_PROFILE_ORCHESTRATION;

import javax.annotation.PostConstruct;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.route.ParentOrchestrationRoute;

@Component
public class JrdUserProfileDataLoadScheduler {

    @Autowired
    CamelContext camelContext;

    @Autowired
    ParentOrchestrationRoute parentOrchestrationRoute;


    @Autowired
    ProducerTemplate producerTemplate;

    @Value("${start-route}")
    private String startRoute;

    @Value("${scheduler-name}")
    private String schedulerName;

    @PostConstruct
    public void postConstruct() throws Exception {
        camelContext.start();
        setGlobalOption();
        parentOrchestrationRoute.startRoute();
    }

    private void setGlobalOption() {
        camelContext.getGlobalOptions().put(uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.ORCHESTRATED_ROUTE, JUDICIAL_USER_PROFILE_ORCHESTRATION);
        camelContext.getGlobalOptions().put(uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SCHEDULER_START_TIME, uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.getCurrentTimeStamp().toString());
        camelContext.getGlobalOptions().put(uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SCHEDULER_NAME, schedulerName);
    }

    @Scheduled(cron = "${scheduler.camel-route-config}")
    public void runJrdScheduler() {
        producerTemplate.sendBody(startRoute, "starting JRD orchestration");
    }
}