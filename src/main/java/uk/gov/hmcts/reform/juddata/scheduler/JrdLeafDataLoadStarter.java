package uk.gov.hmcts.reform.juddata.scheduler;

import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.IS_EXCEPTION_HANDLED;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.LEAF_ROUTE;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SCHEDULER_STATUS;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.route.LeafTableRoute;
import uk.gov.hmcts.reform.juddata.camel.util.DataLoadUtil;

@Component
@Slf4j
public class JrdLeafDataLoadStarter {

    @Autowired
    CamelContext camelContext;

    @Autowired
    LeafTableRoute leafTableRoutes;

    @Autowired
    ProducerTemplate producerTemplate;

    @Autowired
    DataLoadUtil dataLoadUtil;

    @Value("${start-leaf-route}")
    private String startLeafRoute;

    private TaskScheduler scheduler;

    @PostConstruct
    public void postConstruct() throws Exception {
        camelContext.start();
        leafTableRoutes.startRoute();
    }

    @Scheduled(cron = "${scheduler.camel-leaf-router-config}")
    public void runJrdLeafScheduler() {
        camelContext.getGlobalOptions().remove(IS_EXCEPTION_HANDLED);
        camelContext.getGlobalOptions().remove(SCHEDULER_STATUS);
        dataLoadUtil.setGlobalConstant(camelContext, LEAF_ROUTE);
        producerTemplate.sendBody(startLeafRoute, "starting JRD leaf routes though scheduler");
    }
}
