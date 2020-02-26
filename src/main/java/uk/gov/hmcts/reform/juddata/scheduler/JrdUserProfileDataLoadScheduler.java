package uk.gov.hmcts.reform.juddata.scheduler;

import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.JUDICIAL_USER_PROFILE_ORCHESTRATION;

import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Scheduled(cron = "${scheduler.camel-route-config}")
    public void runJrdScheduler() throws Exception {
        camelContext.getGlobalOptions().put(MappingConstants.ORCHESTRATED_ROUTE, JUDICIAL_USER_PROFILE_ORCHESTRATION);
        parentOrchestrationRoute.startRoute();
    }
}
