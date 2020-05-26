package uk.gov.hmcts.reform.juddata.camel.processor;

import static java.lang.Boolean.TRUE;
import static java.lang.String.valueOf;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.IS_EXCEPTION_HANDLED;

import org.apache.camel.Exchange;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.impl.DefaultCamelContext;
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


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcess() throws Exception {
        Exchange exchange = ExchangeBuilder.anExchange(new DefaultCamelContext())
                .withBody(" ")
                .build();
        exchange.setProperty(Exchange.EXCEPTION_CAUGHT, new Exception("Test"));
        exchange.getContext().getGlobalOptions().put(IS_EXCEPTION_HANDLED, valueOf(TRUE));
        exceptionProcessor.process(exchange);
    }
}