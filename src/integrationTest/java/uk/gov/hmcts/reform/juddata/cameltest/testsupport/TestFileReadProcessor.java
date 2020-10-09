package uk.gov.hmcts.reform.juddata.cameltest.testsupport;

import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.BLOBPATH;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.IS_FILE_STALE;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.ROUTE_DETAILS;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.commons.lang.time.DateFormatUtils;
import uk.gov.hmcts.reform.data.ingestion.camel.processor.FileReadProcessor;
import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.RouteProperties;

/**
 * This class is overridden from ingestion lib for integration test cases
 * since Blob storage classes cant be mocked since those are final.
 */
@Slf4j
public class TestFileReadProcessor extends FileReadProcessor {

    @Override
    public void process(Exchange exchange) {
        log.info("{}:: FileReadProcessor starts::", logComponentName);
        String blobFilePath = (String) exchange.getProperty(BLOBPATH);
        CamelContext context = exchange.getContext();
        ConsumerTemplate consumer = context.createConsumerTemplate();
        RouteProperties routeProperties = (RouteProperties) exchange.getIn().getHeader(ROUTE_DETAILS);
        String fileName = routeProperties.getFileName();
        if (isFileTimeStampStale(fileName)) {
            exchange.getMessage().setHeader(IS_FILE_STALE, true);
            auditService.auditException(exchange.getContext(), String.format(
                    "%s file with timestamp %s not loaded due to file stale error",
                    routeProperties.getFileName(),
                    DateFormatUtils.format(fileTimeStamp, "yyyy-MM-dd HH:mm:SS")), true);
        } else {
            exchange.getMessage().setHeader(IS_FILE_STALE, false);
            exchange.getMessage().setBody(consumer.receiveBody(blobFilePath, fileReadTimeOut));
        }
    }

    private boolean isFileTimeStampStale(String fileName) {
        return false;
    }
}
