package uk.gov.hmcts.reform.juddata.camel.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JudicialUserProfileFileReadProcessor implements Processor {

    @Value("${router.user-profile-blob-path}")
    private String blobFilePath;

    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) {
        CamelContext context = exchange.getContext();
        ConsumerTemplate consumer = context.createConsumerTemplate();
        exchange.getOut().setBody(consumer.receiveBody(blobFilePath));
        log.info(" JudicialUserProfileProcessorWithTimer ::");
    }
}
