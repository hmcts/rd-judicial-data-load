package uk.gov.hmcts.reform.juddata.camel.processor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ArchiveAzureFileProcessor implements Processor {

    @Value("${archival-file-names}")
    List<String> archivalFileNames;

    @Value("${active-blob-path}")
    String activeBlobs;

    @Value("${archival-cred}")
    String archivalCred;

    @Value("${archival-date-format}")
    String archivalDateFormat;

    @Value("${file-read-time-out}")
    int fileReadTimeOut;

    @Override
    public void process(Exchange exchange) throws Exception {
        Integer count = exchange.getProperty("CamelLoopIndex", Integer.class);
        String date = new SimpleDateFormat(archivalDateFormat).format(new Date());
        String fileName = archivalFileNames.get(count);
        exchange.getIn().setHeader("filename", "/" + fileName.substring(0,
                fileName.indexOf(".csv")).concat(date + ".csv"));
        CamelContext context = exchange.getContext();
        ConsumerTemplate consumer = context.createConsumerTemplate();
        exchange.getMessage().setBody(consumer.receiveBody(activeBlobs + "/" + archivalFileNames.get(count)
                        + "?" + archivalCred, fileReadTimeOut));
    }
}
