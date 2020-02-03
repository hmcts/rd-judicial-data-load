package uk.gov.hmcts.reform.juddata.camel.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@Slf4j
public class JudicialUserProfileFileReadProcessor implements Processor {

    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) {
        CamelContext context = exchange.getContext();
        ConsumerTemplate consumer = context.createConsumerTemplate();
        exchange.getOut().setBody(consumer.receiveBody("azure-blob://rdaat/jrdtest/judicial_userprofile.csv?"
               + "credentials=#credsreg&operation=updateBlockBlob"));
        log.info(" JudicialUserProfileProcessorWithTimer ::");
    }
}
