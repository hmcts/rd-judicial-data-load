package uk.gov.hmcts.reform.juddata.camel.processor;

import static org.apache.camel.Exchange.EXCEPTION_CAUGHT;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExceptionProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {
        Exception exception = (Exception) exchange.getProperty(EXCEPTION_CAUGHT);
        String  schedulerStatus=(String )exchange.getIn().getHeader("SchedulerStatus");
        if (null==schedulerStatus) {
            exchange.getIn().setHeader("SchedulerStatus", "Failed");
        }else
        {
            exchange.getIn().setHeader("SchedulerStatus", "PartialSucess");
        }
        log.error("::::exception in route for data processing::::" + exception);
    }
}
