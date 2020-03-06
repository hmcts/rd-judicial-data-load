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
import uk.gov.hmcts.reform.juddata.camel.util.MappingConstants;

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

    @PostConstruct
    public void postConstruct() throws Exception {
        camelContext.start();
        camelContext.getGlobalOptions().put(MappingConstants.ORCHESTRATED_ROUTE, JUDICIAL_USER_PROFILE_ORCHESTRATION);
        parentOrchestrationRoute.startRoute();
    }

    @Scheduled(cron = "${scheduler.camel-route-config}")
    public void runJrdScheduler() {
        producerTemplate.sendBody(startRoute, "starting JRD orchestration");
    }
}
