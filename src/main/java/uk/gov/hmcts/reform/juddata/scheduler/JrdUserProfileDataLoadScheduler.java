package uk.gov.hmcts.reform.juddata.scheduler;

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

    @Scheduled(cron = "0 0/3 * * * ?")
    public void runJrdScheduler() throws Exception {
        camelContext.getGlobalOptions().put(MappingConstants.PARENT_ROUTE_NAME,"judicial-user-profile-orchestartion");
        parentRoute.startRoute();
    }
}
