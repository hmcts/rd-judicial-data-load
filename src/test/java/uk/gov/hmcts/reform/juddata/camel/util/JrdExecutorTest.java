package uk.gov.hmcts.reform.juddata.camel.util;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.data.ingestion.camel.service.AuditServiceImpl;
import uk.gov.hmcts.reform.data.ingestion.camel.util.DataLoadUtil;
import uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.TRUE;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.ERROR_MESSAGE;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.SUCCESS;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.IS_PARENT;

//ignored this test for now it mostly for emailing exceptions which we disabled in prod
@RunWith(SpringRunner.class)
@Ignore
public class JrdExecutorTest {
    JrdExecutor jrdExecutor = new JrdExecutor();

    JrdExecutor jrdExecutorSpy = spy(jrdExecutor);

    CamelContext camelContext = new DefaultCamelContext();

    AuditServiceImpl auditService = mock(AuditServiceImpl.class);

    ProducerTemplate producerTemplate = mock(ProducerTemplate.class);

    DataLoadUtil dataLoadUtil = mock(DataLoadUtil.class);

    @Before
    public void init() {
        setField(jrdExecutorSpy, "judicialAuditServiceImpl", auditService);
        camelContext.getGlobalOptions().put(ERROR_MESSAGE, ERROR_MESSAGE);
        List<String> archivalFileNames = new ArrayList<>();
        archivalFileNames.add("test");
        setField(jrdExecutorSpy, "archivalFileNames", producerTemplate);
    }

    @Test
    public void testExecute() {
        camelContext.getGlobalOptions().put(IS_PARENT, String.valueOf(TRUE));
        setField(jrdExecutorSpy, "dataLoadUtil", dataLoadUtil);
        setField(jrdExecutorSpy, "producerTemplate", producerTemplate);

        doNothing().when(producerTemplate).sendBody(any());
        doNothing().when(auditService).auditSchedulerStatus(camelContext);
        assertEquals(SUCCESS, jrdExecutorSpy.execute(camelContext, "test", "test"));
        verify(jrdExecutorSpy, times(1))
            .execute(camelContext, "test", "test");
        verify(auditService, times(1))
            .auditSchedulerStatus(camelContext);
    }

    @Test
    public void testExecuteException() {
        camelContext.getGlobalOptions().put(IS_PARENT, String.valueOf(TRUE));
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

    @Test
    public void testExecuteExceptionWithLeaf() {
        doNothing().when(auditService).auditSchedulerStatus(camelContext);
        assertEquals(MappingConstants.FAILURE,
            jrdExecutorSpy.execute(camelContext, "test", "test"));
        verify(jrdExecutorSpy, times(1))
            .execute(camelContext, "test", "test");
    }
}
