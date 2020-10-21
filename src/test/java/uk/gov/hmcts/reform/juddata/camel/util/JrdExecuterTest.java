package uk.gov.hmcts.reform.juddata.camel.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.ERROR_MESSAGE;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants;
import uk.gov.hmcts.reform.juddata.camel.service.JudicialAuditServiceImpl;

@RunWith(SpringRunner.class)
public class JrdExecuterTest {
    JrdExecutor jrdExecutor = new JrdExecutor();

    JrdExecutor jrdExecutorSpy = spy(jrdExecutor);

    CamelContext camelContext = new DefaultCamelContext();

    JudicialAuditServiceImpl auditService = mock(JudicialAuditServiceImpl.class);

    ProducerTemplate producerTemplate = mock(ProducerTemplate.class);

    @Before
    public void init() {
        setField(jrdExecutorSpy, "judicialAuditServiceImpl", auditService);
        camelContext.getGlobalOptions().put(ERROR_MESSAGE,ERROR_MESSAGE);
    }

    @Test
    public void testExecute() {
        doNothing().when(producerTemplate).sendBody(any());
        doNothing().when(auditService).auditSchedulerStatus(camelContext);
        jrdExecutorSpy.execute(camelContext, "test", "test");
        verify(jrdExecutorSpy, times(1))
                .execute(camelContext, "test", "test");
        verify(auditService, times(1))
                .auditSchedulerStatus(camelContext);
    }

    @Test
    public void testExecuteException() {
        doNothing().when(auditService).auditSchedulerStatus(camelContext);
        assertEquals(MappingConstants.FAILURE,
                jrdExecutorSpy.execute(camelContext, "test", "test"));
        verify(jrdExecutorSpy, times(1))
                .execute(camelContext, "test", "test");
        verify(auditService, times(1))
                .auditSchedulerStatus(camelContext);
        verify(auditService, times(1))
                .auditException(camelContext, ERROR_MESSAGE);

    }
}
