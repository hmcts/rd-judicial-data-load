package uk.gov.hmcts.reform.juddata.scheduler;

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

@Component
@Slf4j
public class JrdLeafDataLoadStarter {

    @Autowired
    CamelContext camelContext;

    @Autowired
    LeafTableRoute leafTableRoutes;

    @Autowired
    ProducerTemplate producerTemplate;

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
        producerTemplate.sendBody(startLeafRoute, "starting JRD leaf routes though scheduler");
    }
}
