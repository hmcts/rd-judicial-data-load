package uk.gov.hmcts.reform.juddata.camel.processor;

import java.sql.Timestamp;
import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import uk.gov.hmcts.reform.juddata.camel.util.JrdUtility;
import uk.gov.hmcts.reform.juddata.camel.util.MappingConstants;

public class SchedulerAuditProcessorTest {

    private SchedulerAuditProcessor schedulerAuditProcessorUnderTest;

    @Before
    public void setUp() {
        schedulerAuditProcessorUnderTest = new SchedulerAuditProcessor();
        schedulerAuditProcessorUnderTest.jrdUtility = Mockito.mock(JrdUtility.class);
    }

    @Test
    public void testProcess() throws Exception {
        // Setup
        final Exchange exchange = Mockito.mock(Exchange.class);
        Message message = Mockito.mock(Message.class);
        Map<String, Object> schedulerHeader = JrdUtility.getSchedulerHeader(MappingConstants.SCHEDULER_NAME, MappingConstants.getCurrentTimeStamp());
        message.setHeaders(schedulerHeader);
        Mockito.when(exchange.getIn()).thenReturn(message);
        Mockito.when(message.getHeader(MappingConstants.SCHEDULER_NAME)).thenReturn("schedulerHeader");
        Mockito.when(message.getHeader(MappingConstants.SCHEDULER_START_TIME)).thenReturn(new Timestamp(System.currentTimeMillis()));
        Mockito.when(message.getHeader(MappingConstants.SCHEDULER_STATUS)).thenReturn("Failed");

        // Run the test
        schedulerAuditProcessorUnderTest.process(exchange);

        // Verify the results
        Mockito.verify(schedulerAuditProcessorUnderTest.jrdUtility).schedularAuditUpdate(ArgumentMatchers.any(Exchange.class));
    }
}
