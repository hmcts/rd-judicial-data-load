package uk.gov.hmcts.reform.juddata.camel.processor;

import static org.mockito.ArgumentMatchers.any;

import org.apache.camel.Exchange;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.reform.juddata.camel.service.EmailData;
import uk.gov.hmcts.reform.juddata.camel.service.EmailService;

@TestPropertySource(properties = {"spring.config.location=classpath:application-integration.yml"})
public class ExceptionProcessorTest extends CamelTestSupport {
    @Mock
    EmailService emailService;

    @InjectMocks
    ExceptionProcessor exceptionProcessor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        exceptionProcessor.mailFrom = "no-reply@reform.hmcts.net";
    }

    @Test
    public void testProcess() throws Exception {
        Exchange exchange = ExchangeBuilder.anExchange(context)
            .withBody(" ")
            .build();
        exchange.setProperty(Exchange.EXCEPTION_CAUGHT, new Exception("Test"));
        exchange.setProperty(Exchange.FAILURE_ROUTE_ID, "1");
        ReflectionTestUtils.setField(exceptionProcessor, "mailEnabled", Boolean.TRUE);
        exceptionProcessor.process(exchange);
        Mockito.verify(emailService, Mockito.times(1)).sendEmail(any(String.class), any(EmailData.class));

    }

    @Test
    public void testProcessMailEnabledTrue() throws Exception {
        Exchange exchange = ExchangeBuilder.anExchange(context)
            .withBody(" ")
            .build();
        exchange.setProperty(Exchange.EXCEPTION_CAUGHT, new Exception("Test"));
        ReflectionTestUtils.setField(exceptionProcessor, "mailEnabled", Boolean.TRUE);
        exceptionProcessor.process(exchange);
        Mockito.verify(emailService, Mockito.times(1)).sendEmail(any(String.class), any(EmailData.class));
    }

    @Test
    public void testProcessMailEnabledFalse() throws Exception {
        Exchange exchange = ExchangeBuilder.anExchange(context)
            .withBody(" ")
            .build();
        exchange.setProperty(Exchange.EXCEPTION_CAUGHT, new Exception("Test"));
        exchange.setProperty(Exchange.FAILURE_ROUTE_ID, "1");
        ReflectionTestUtils.setField(exceptionProcessor, "mailEnabled", Boolean.FALSE);
        exceptionProcessor.process(exchange);
        Mockito.verify(emailService, Mockito.times(0)).sendEmail(any(String.class), any(EmailData.class));
    }
}