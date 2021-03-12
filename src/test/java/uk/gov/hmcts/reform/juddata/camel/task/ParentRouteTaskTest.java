package uk.gov.hmcts.reform.juddata.camel.task;

import org.apache.camel.CamelContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import uk.gov.hmcts.reform.data.ingestion.camel.route.DataLoadRoute;
import uk.gov.hmcts.reform.juddata.camel.util.JrdExecutor;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class ParentRouteTaskTest {

    @InjectMocks
    ParentRouteTask parentRouteTask;

    @Mock
    DataLoadRoute dataLoadRoute;

    @Mock
    JrdExecutor jrdExecutor;

    Map<String, String> globalOptions = new HashMap<>();
    CamelContext camelContext = mock(CamelContext.class);
    StepContribution stepContribution = mock(StepContribution.class);
    ChunkContext chunkContext = mock(ChunkContext.class);

    @Test
    void testParentExecute() throws Exception {
        setField(parentRouteTask, "logComponentName", "testlogger");
        when(jrdExecutor.execute(any(), any(), any())).thenReturn("success");
        when(camelContext.getGlobalOptions()).thenReturn(globalOptions);
        assertEquals(RepeatStatus.FINISHED, parentRouteTask.execute(stepContribution, chunkContext));
        verify(jrdExecutor, times(1)).execute(any(), any(), any());
        verify(camelContext, times(2)).getGlobalOptions();
    }
}
