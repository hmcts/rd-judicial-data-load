package uk.gov.hmcts.reform.juddata.camel.task;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import uk.gov.hmcts.reform.data.ingestion.camel.route.DataLoadRoute;
import uk.gov.hmcts.reform.juddata.camel.util.JrdExecutor;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ParentRouteTaskTest {

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
    public void testParentExecute() throws Exception {
        setField(parentRouteTask, "logComponentName", "testlogger");
        when(jrdExecutor.execute(any(), any(), any())).thenReturn("success");
        when(camelContext.getGlobalOptions()).thenReturn(globalOptions);
        assertEquals(RepeatStatus.FINISHED, parentRouteTask.execute(stepContribution, chunkContext));
        verify(jrdExecutor, times(1)).execute(any(), any(), any());
        verify(camelContext, times(1)).getGlobalOptions();
    }
}
