package uk.gov.hmcts.reform.juddata.camel.listener;

import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.JobExecution;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.reform.data.ingestion.camel.route.ArchivalRoute;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class JobResultListenerTest {

    @InjectMocks
    JobResultListener jobResultListener;

    @Mock
    JobExecution jobExecutionMock;

    @Mock
    ArchivalRoute archivalRouteMock;

    @Mock
    ProducerTemplate producerTemplate;

    @Test
    public void beforeJobTest() {
        jobResultListener.beforeJob(jobExecutionMock);
    }

    @Test
    public void afterJobTest() {
        ReflectionTestUtils.setField(jobResultListener, "archivalRouteName", "archivalRouteName");
        jobResultListener.afterJob(jobExecutionMock);
        verify(archivalRouteMock, times(1)).archivalRoute(any());
        verify(producerTemplate, times(1)).sendBody("archivalRouteName",
                "starting Archival");
    }
}
