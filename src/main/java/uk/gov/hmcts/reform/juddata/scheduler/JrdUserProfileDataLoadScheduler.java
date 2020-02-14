package uk.gov.hmcts.reform.juddata.scheduler;

import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.JUDICIAL_USER_PROFILE_ORCHESTRATION;

import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.route.ParentRoute;
import uk.gov.hmcts.reform.juddata.camel.util.MappingConstants;

@Component
public class JrdUserProfileDataLoadScheduler {

    @Autowired
    CamelContext camelContext;

    @Autowired
    ParentRoute parentRoute;

    @Scheduled(cron = "0 0/30 * * * ?")
    public void runJrdScheduler() throws Exception {
        camelContext.getGlobalOptions().put(MappingConstants.PARENT_ROUTE_NAME, JUDICIAL_USER_PROFILE_ORCHESTRATION);
        parentRoute.startRoute();
    }
}
