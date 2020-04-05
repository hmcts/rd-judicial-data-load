package uk.gov.hmcts.reform.juddata.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.juddata.camel.email.EmailService;
import uk.gov.hmcts.reform.juddata.camel.email.Mail;

@TestPropertySource(properties = {"spring.config.location=classpath:application-integration.yml"})
public class ExceptionProcessorTest extends CamelTestSupport {

    private ExceptionProcessor exceptionProcessorUnderTest;

    @Before
    public void setUp() {
        exceptionProcessorUnderTest = new ExceptionProcessor();
        exceptionProcessorUnderTest.emailService = Mockito.mock(EmailService.class);
    }

    @Test
    public void testProcess() {
        // Setup
        final Exchange exchange = new DefaultExchange(context);

        exchange.setProperty(Exchange.EXCEPTION_CAUGHT, new Exception("Test Exception"));
        exchange.setProperty(Exchange.FAILURE_ROUTE_ID, "Rout1");
        // Run the test
        exceptionProcessorUnderTest.process(exchange);

        // Verify the results
        Mockito.verify(exceptionProcessorUnderTest.emailService).sendSimpleMessage(ArgumentMatchers.any(Mail.class));
    }
}
