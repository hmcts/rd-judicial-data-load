package uk.gov.hmcts.reform.juddata.camel.util;

import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
@SuppressWarnings("unchecked")
class JrdDataIngestionLibraryRunnerTest {

    TopicPublisher topicPublisher = mock(TopicPublisher.class);

    @InjectMocks
    private JrdDataIngestionLibraryRunner jrdDataIngestionLibraryRunner;

    CamelContext camelContext = mock(CamelContext.class);

    JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

    Job job = mock(Job.class);

    JobParameters jobParameters = mock(JobParameters.class);

    JobLauncher jobLauncherMock = mock(JobLauncher.class);

    FeatureToggleService featureToggleService = mock(FeatureToggleService.class);

    @BeforeEach
    public void beforeTest() {
        Map<String, String> options = new HashMap<>();
        options.put(JOB_ID, "1");
        List<String> sidamIds = new ArrayList<>();
        sidamIds.add(UUID.randomUUID().toString());
        jrdDataIngestionLibraryRunner.selectJobStatus = "dummyjobstatus";
        jrdDataIngestionLibraryRunner.getSidamIds = "dummyQuery";
        jrdDataIngestionLibraryRunner.updateJobStatus = "dummyQuery";
        jrdDataIngestionLibraryRunner.failedAuditFileCount = "failedAuditFileCount";

        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class)))
            .thenReturn(Pair.of("1", IN_PROGRESS.getStatus()));
        when(jdbcTemplate.queryForObject("failedAuditFileCount", Integer.class)).thenReturn(Integer.valueOf(0));

        jrdDataIngestionLibraryRunner.logComponentName = "loggingComponent";
        when(camelContext.getGlobalOptions()).thenReturn(options);
        when(jdbcTemplate.query("dummyQuery", ROW_MAPPER)).thenReturn(sidamIds);
        when(jdbcTemplate.update(anyString(), any(), anyInt())).thenReturn(1);
        when(featureToggleService.isFlagEnabled(anyString())).thenReturn(true);
        jrdDataIngestionLibraryRunner.environment = "test";
    }

    @SneakyThrows
    @Test
    void testRun() {
        jrdDataIngestionLibraryRunner.run(job, jobParameters);
        verify(jobLauncherMock).run(any(), any());
        verify(topicPublisher, times(1)).sendMessage(any(), anyString());
    }

    @SneakyThrows
    @Test
    void testRunRetry() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class)))
            .thenReturn(Pair.of("1", FAILED.getStatus()));
        jrdDataIngestionLibraryRunner.run(job, jobParameters);
        verify(jobLauncherMock).run(any(), any());
        verify(topicPublisher, times(1)).sendMessage(any(), anyString());
    }

    @SneakyThrows
    @Test
    void testRunException() {
        doThrow(new RuntimeException("Some Exception")).when(topicPublisher).sendMessage(anyList(), anyString());
        assertThrows(Exception.class, () -> jrdDataIngestionLibraryRunner.run(job, jobParameters));
        verify(topicPublisher, times(1)).sendMessage(any(), anyString());
    }

    @SneakyThrows
    @Test
    void testRunNoMessageToPublish() {
        List<String> sidamIds = new ArrayList<>();
        when(jdbcTemplate.query("dummyQuery", ROW_MAPPER)).thenReturn(sidamIds);
        jrdDataIngestionLibraryRunner.run(job, jobParameters);
        verify(jobLauncherMock).run(any(), any());
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(RowMapper.class));
    }

    @SneakyThrows
    @Test
    void testRunFailedFiles() {
        when(jdbcTemplate.queryForObject("failedAuditFileCount", Integer.class)).thenReturn(Integer.valueOf(1));
        jrdDataIngestionLibraryRunner.run(job, jobParameters);
        verify(jobLauncherMock).run(any(), any());
    }
}
