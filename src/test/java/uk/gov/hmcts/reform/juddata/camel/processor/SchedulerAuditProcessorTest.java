package uk.gov.hmcts.reform.juddata.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import uk.gov.hmcts.reform.juddata.camel.util.DataLoadAudit;

public class SchedulerAuditProcessorTest extends CamelTestSupport {

    private SchedulerAuditProcessor schedulerAuditProcessorUnderTest;

    @Before
    public void setUp() {
        schedulerAuditProcessorUnderTest = new SchedulerAuditProcessor();
        schedulerAuditProcessorUnderTest.dataLoadAudit = Mockito.mock(DataLoadAudit.class);
    }

    @Test
    public void testProcess() throws Exception {
        // Setup
        final Exchange exchange = new DefaultExchange(context);
        Message message = Mockito.mock(Message.class);

        schedulerAuditProcessorUnderTest.auditEnablel = Boolean.TRUE;
        // Run the test
        schedulerAuditProcessorUnderTest.process(exchange);

        // Verify the results
        Mockito.verify(schedulerAuditProcessorUnderTest.dataLoadAudit).schedularAuditUpdate(ArgumentMatchers.any(Exchange.class));
    }
}
