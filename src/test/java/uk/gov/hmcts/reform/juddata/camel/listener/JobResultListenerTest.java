package uk.gov.hmcts.reform.juddata.camel.listener;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.JobExecution;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;
import uk.gov.hmcts.reform.data.ingestion.camel.route.ArchivalRoute;
import uk.gov.hmcts.reform.juddata.camel.util.JrdAsbPublisher;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.JOB_ID;

@ExtendWith(MockitoExtension.class)
class JobResultListenerTest {

    @InjectMocks
    JobResultListener jobResultListener = spy(JobResultListener.class);

    @Mock
    JobExecution jobExecutionMock;

    @Mock
    ArchivalRoute archivalRouteMock;

    @Mock
    ProducerTemplate producerTemplate;

    @Mock
    JdbcTemplate jdbcTemplate;

    @Mock
    CamelContext camelContext;

    @Mock
    JrdAsbPublisher jrdAsbPublisher;

    @Test
    void beforeJobTest() {
        jobResultListener.beforeJob(jobExecutionMock);
        verify(jobResultListener).beforeJob(any());
    }

    @Test
    void afterJobTest() {
        ReflectionTestUtils.setField(jobResultListener, "archivalRouteName", "archivalRouteName");
        doNothing().when(jrdAsbPublisher).executeAsbPublishing();
        Map<String, String> camelGlobalOptions = ImmutableMap.of(JOB_ID,"1");
        jobResultListener.updateJobStatus = "duummyQuery";
        when(camelContext.getGlobalOptions()).thenReturn(camelGlobalOptions);
        when(jdbcTemplate.update(anyString(), any(), anyInt())).thenReturn(1);
        jobResultListener.afterJob(jobExecutionMock);
        verify(archivalRouteMock).archivalRoute(any());
        verify(producerTemplate).sendBody("archivalRouteName",
            "starting Archival");
        verify(jrdAsbPublisher).executeAsbPublishing();
        verify(jdbcTemplate).update(anyString(), any(), anyInt());
    }
}
