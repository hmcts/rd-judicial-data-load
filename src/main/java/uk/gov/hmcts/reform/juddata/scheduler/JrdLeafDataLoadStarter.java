package uk.gov.hmcts.reform.juddata.scheduler;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
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

    public static final long INITIAL_DELAY_SECS = 60000L;

    private TaskScheduler scheduler;

    @PostConstruct
    public void postConstruct() throws Exception {
        camelContext.start();
        leafTableRoutes.startRoute();
        executeTask();
    }

    /**
     * this will call camel routes once after 60 secs spring boot is started.
     */
    public void executeTask() {
        ScheduledExecutorService localExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduler = new ConcurrentTaskScheduler(localExecutor);
        scheduler.schedule(leafRoutsRunnable,
                new Date(System.currentTimeMillis() + INITIAL_DELAY_SECS));
    }

    Runnable leafRoutsRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                log.info("calling leaf routes");
                producerTemplate.sendBody(startLeafRoute, "starting JRD leaf routes");
            } catch (Exception ex) {
                log.error("some error while loading spring context", ex);
            }
        }
    };
}
