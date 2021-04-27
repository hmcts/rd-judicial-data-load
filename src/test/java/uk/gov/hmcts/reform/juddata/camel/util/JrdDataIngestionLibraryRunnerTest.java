package uk.gov.hmcts.reform.juddata.camel.util;

import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.jdbc.core.JdbcTemplate;
import uk.gov.hmcts.reform.juddata.camel.servicebus.TopicPublisher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.juddata.camel.util.JobStatus.FAILED;
import static uk.gov.hmcts.reform.juddata.camel.util.JobStatus.IN_PROGRESS;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.JOB_ID;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.ROW_MAPPER;

@ExtendWith(MockitoExtension.class)
public class JrdDataIngestionLibraryRunnerTest {

    TopicPublisher topicPublisher = mock(TopicPublisher.class);

    @InjectMocks
    private JrdDataIngestionLibraryRunner jrdDataIngestionLibraryRunner;

    CamelContext camelContext = mock(CamelContext.class);

    JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

    Job job = mock(Job.class);

    JobParameters jobParameters = mock(JobParameters.class);

    JobLauncher jobLauncherMock = mock(JobLauncher.class);

    @BeforeEach
    public void beforeTest() {
        Map<String, String> options = new HashMap<>();
        options.put(JOB_ID, "1");
        List<String> sidamIds = new ArrayList<>();
        sidamIds.add(UUID.randomUUID().toString());
        jrdDataIngestionLibraryRunner.selectJobStatus = "dummyQuery";
        jrdDataIngestionLibraryRunner.getSidamIds = "dummyQuery";
        jrdDataIngestionLibraryRunner.updateJobStatus = "dummyQuery";

        when(jdbcTemplate.queryForObject("dummyQuery", String.class)).thenReturn(IN_PROGRESS.getStatus());
        jrdDataIngestionLibraryRunner.logComponentName = "loggingComponent";
        when(camelContext.getGlobalOptions()).thenReturn(options);
        when(jdbcTemplate.query("dummyQuery", ROW_MAPPER)).thenReturn(sidamIds);
        when(jdbcTemplate.update(anyString(), any(), anyInt())).thenReturn(1);
    }

    @SneakyThrows
    @Test
    public void testRun() {
        jrdDataIngestionLibraryRunner.run(job, jobParameters);
        verify(jobLauncherMock).run(any(),any());
        verify(topicPublisher, times(1)).sendMessage(any());
    }

    @SneakyThrows
    @Test
    public void testRunRetry() {
        when(jdbcTemplate.queryForObject("dummyQuery", String.class)).thenReturn(FAILED.getStatus());
        jrdDataIngestionLibraryRunner.run(job, jobParameters);
        verify(jobLauncherMock).run(any(),any());
        verify(topicPublisher, times(1)).sendMessage(any());
    }

    @SneakyThrows
    @Test
    public void testRunException() {
        doThrow(new RuntimeException("Some Exception")).when(topicPublisher).sendMessage(anyList());
        assertThrows(Exception.class, () -> jrdDataIngestionLibraryRunner.run(job, jobParameters));
        verify(topicPublisher, times(1)).sendMessage(any());
    }

    @SneakyThrows
    @Test
    public void testRunNoMessageToPublish() {
        List<String> sidamIds = new ArrayList<>();
        when(jdbcTemplate.query("dummyQuery", ROW_MAPPER)).thenReturn(sidamIds);
        jrdDataIngestionLibraryRunner.run(job, jobParameters);
        verify(jobLauncherMock).run(any(),any());
        verify(jdbcTemplate, times(1)).queryForObject("dummyQuery", String.class);
    }
}
