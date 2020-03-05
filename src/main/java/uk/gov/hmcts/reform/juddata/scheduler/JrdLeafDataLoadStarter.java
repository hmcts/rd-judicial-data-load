package uk.gov.hmcts.reform.juddata.scheduler;

import javax.annotation.PostConstruct;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.route.LeafTableRoute;

@Component
public class JrdLeafDataLoadStarter {

    @Autowired
    CamelContext camelContext;

    @Autowired
    LeafTableRoute leafTableRoutes;

    @Autowired
    ProducerTemplate producerTemplate;

    @Value("${start-leaf-route}")
    private String startLeafRoute;

    @PostConstruct
    public void postConstruct() throws Exception {
        camelContext.start();
        leafTableRoutes.startRoute();
        producerTemplate.sendBody(startLeafRoute, "starting JRD leaf routes");
    }

}
