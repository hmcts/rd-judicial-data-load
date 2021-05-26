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
import uk.gov.hmcts.reform.data.ingestion.camel.route.ArchivalRoute;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

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

    @Test
    void beforeJobTest() {
        jobResultListener.beforeJob(jobExecutionMock);
        verify(jobResultListener).beforeJob(any());
    }

    @Test
    void afterJobTest() {
        ReflectionTestUtils.setField(jobResultListener, "archivalRouteName", "archivalRouteName");
        jobResultListener.afterJob(jobExecutionMock);
        verify(archivalRouteMock).archivalRoute(any());
        verify(producerTemplate).sendBody("archivalRouteName",
            "starting Archival");
    }
}
