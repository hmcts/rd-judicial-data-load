package uk.gov.hmcts.reform.juddata.camel.processor;

import static org.apache.camel.Exchange.EXCEPTION_CAUGHT;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.util.MappingConstants;

@Component
@Slf4j
public class ExceptionProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {
        Exception exception = (Exception) exchange.getProperty(EXCEPTION_CAUGHT);
        if (exception instanceof java.sql.SQLException || exception instanceof org.springframework.dao.DataIntegrityViolationException) {
            exchange.getContext().getGlobalOptions().put(MappingConstants.SCHEDULER_STATUS, MappingConstants.FAILURE);
        }
        log.error("::::exception in route for data processing::::" + exception);
    }
}
