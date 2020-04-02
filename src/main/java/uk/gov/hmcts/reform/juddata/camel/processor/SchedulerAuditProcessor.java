package uk.gov.hmcts.reform.juddata.camel.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.gov.hmcts.reform.juddata.camel.util.JrdUtility;

@Component
@Slf4j
@EnableTransactionManagement
public class SchedulerAuditProcessor implements Processor {

    @Autowired
    JrdUtility jrdUtility;

    /**
     * Processes the message exchange.
     *
     * @param exchange the message exchange
     * @throws Exception if an internal processing error has occurred.
     */
    @SuppressWarnings("unchecked")
    @Override
    // we  need to  use UTC  GMT   time
    //in  day  light saving how this  works
    public void process(Exchange exchange) throws Exception {
        jrdUtility.schedularAuditUpdate(exchange);
    }
}
