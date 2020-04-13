package uk.gov.hmcts.reform.juddata.scheduler;

import javax.annotation.PostConstruct;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.route.ParentOrchestrationRoute;
import uk.gov.hmcts.reform.juddata.camel.util.DataLoadUtil;
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

    @Value("${scheduler-name}")
    private String schedulerName;

    @Autowired
    DataLoadUtil dataLoadUtil;

    @PostConstruct
    public void postConstruct() throws Exception {
        camelContext.start();
        dataLoadUtil.setGlobalConstant(camelContext);
        parentOrchestrationRoute.startRoute();
    }

    private void setGlobalOption() {
        camelContext.getGlobalOptions().put(MappingConstants.ORCHESTRATED_ROUTE, MappingConstants.JUDICIAL_USER_PROFILE_ORCHESTRATION);
        camelContext.getGlobalOptions().put(MappingConstants.SCHEDULER_START_TIME, MappingConstants.getCurrentTimeStamp().toString());
        camelContext.getGlobalOptions().put(MappingConstants.SCHEDULER_NAME, schedulerName);
    }

    @Scheduled(cron = "${scheduler.camel-route-config}")
    public void runJrdScheduler() {
        producerTemplate.sendBody(startRoute, "starting JRD orchestration");
    }
}