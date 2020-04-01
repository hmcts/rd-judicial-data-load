package uk.gov.hmcts.reform.juddata.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Before;
import org.junit.Test;
import uk.gov.hmcts.reform.juddata.camel.util.JrdUtility;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class SchedulerAuditProcessorTest {

    private SchedulerAuditProcessor schedulerAuditProcessorUnderTest;

    @Before
    public void setUp() {
        schedulerAuditProcessorUnderTest = new SchedulerAuditProcessor();
        schedulerAuditProcessorUnderTest.jrdUtility = mock(JrdUtility.class);
    }

    @Test
    public void testProcess() throws Exception {
        // Setup
        final Exchange exchange = mock(Exchange.class);
        Message message = mock(Message.class);
        Map<String, Object> schedulerHeader=JrdUtility.getSchedulerHeader( "scheduler_Name", new Timestamp(System.currentTimeMillis()));
        message.setHeaders(schedulerHeader);
        when(exchange.getIn()).thenReturn(message);
        when(message.getHeader("SchedulerName")).thenReturn("schedulerHeader");
        when(message.getHeader("SchedulerStartTime")).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(message.getHeader("SchedulerStatus")).thenReturn("Failed");

        // Run the test
        schedulerAuditProcessorUnderTest.process(exchange);

        // Verify the results
        verify(schedulerAuditProcessorUnderTest.jrdUtility).schedularAuditUpdate(any(Exchange.class));
    }
}
