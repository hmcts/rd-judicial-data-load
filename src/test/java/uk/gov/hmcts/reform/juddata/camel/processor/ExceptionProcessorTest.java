package uk.gov.hmcts.reform.juddata.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.juddata.camel.service.EmailService;

public class ExceptionProcessorTest extends CamelTestSupport {
    @Mock
    EmailService emailService;

    @InjectMocks
    ExceptionProcessor exceptionProcessor;

    String from = "Test";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcess() throws Exception {
        Exchange exchange = ExchangeBuilder.anExchange(context)
            .withBody(" ")
            .build();
        exchange.setProperty(Exchange.EXCEPTION_CAUGHT, new Exception("Test"));
        exchange.setProperty(Exchange.FAILURE_ROUTE_ID, "1");
        exceptionProcessor.process(exchange);
        assertEquals("1", ((java.lang.String) ((java.util.concurrent.ConcurrentHashMap) exchange.getProperties()).get("CamelFailureRouteId")));
        Object[] collection = ((org.apache.camel.impl.DefaultMessage) exchange.getIn()).getAttachmentNames().toArray();
        assertEquals(0, collection.length);
        assertEquals("1", ((java.lang.String) exchange.getProperties().get("CamelFailureRouteId")));
    }
}