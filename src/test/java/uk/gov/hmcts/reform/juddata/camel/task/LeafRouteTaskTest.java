package uk.gov.hmcts.reform.juddata.camel.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.repeat.RepeatStatus;
import uk.gov.hmcts.reform.data.ingestion.camel.route.DataLoadRoute;
import uk.gov.hmcts.reform.juddata.camel.util.JrdExecutor;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

class LeafRouteTaskTest {
    LeafRouteTask leafRouteTask = spy(new LeafRouteTask());

    DataLoadRoute dataLoadRoute = mock(DataLoadRoute.class);

    JrdExecutor jrdExecutor = mock(JrdExecutor.class);

    @BeforeEach
    public void init() {
        setField(leafRouteTask, "logComponentName", "testlogger");
        setField(leafRouteTask, "dataLoadRoute", dataLoadRoute);
        setField(leafRouteTask, "jrdExecutor", jrdExecutor);
    }

    @Test
    void testExecute() throws Exception {
        doNothing().when(dataLoadRoute).startRoute(anyString(), anyList());
        when(jrdExecutor.execute(any(), any(), any())).thenReturn("success");
        assertEquals(RepeatStatus.FINISHED, leafRouteTask.execute(any(), any()));
        verify(leafRouteTask, times(1)).execute(any(), any());
        verify(dataLoadRoute, times(1)).startRoute(any(), any());
    }
}
