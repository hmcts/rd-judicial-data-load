package uk.gov.hmcts.reform.juddata.scheduler;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.route.LeafTableRoute;
import uk.gov.hmcts.reform.juddata.camel.service.AuditProcessingService;
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

    @Autowired
    AuditProcessingService schedulerAuditProcessingService;

    @PostConstruct
    public void postConstruct() throws Exception {
        camelContext.start();
        leafTableRoutes.startRoute();
    }
}
