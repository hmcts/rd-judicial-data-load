package uk.gov.hmcts.reform.juddata.camel.util;

import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.commons.lang3.tuple.MutablePair;
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
import org.testcontainers.shaded.com.google.common.collect.ImmutableSet;
import uk.gov.hmcts.reform.juddata.camel.servicebus.TopicPublisher;
import uk.gov.hmcts.reform.juddata.client.IdamClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
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

    JrdSidamTokenService jrdSidamTokenService = mock(JrdSidamTokenServiceImpl.class);

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
        when(jdbcTemplate.queryForObject("dummyjobstatus", Pair.class))
            .thenReturn(new MutablePair("1", "2"));
        when(jdbcTemplate.update(anyString(), any(), anyInt())).thenReturn(1);
        when(featureToggleService.isFlagEnabled(anyString())).thenReturn(true);
        jrdDataIngestionLibraryRunner.environment = "test";
        IdamClient.User user = new IdamClient.User();
        user.setSsoId(UUID.randomUUID().toString());
        user.setId(UUID.randomUUID().toString());
        Set<IdamClient.User> sidamUsers = ImmutableSet.of(user);
        when(jrdSidamTokenService.getSyncFeed()).thenReturn(sidamUsers);
        jrdDataIngestionLibraryRunner.updateSidamIds = "updateSidamIds";
        int[][] intArray = new int[1][];
        when(jdbcTemplate.batchUpdate(anyString(), anyList(), anyInt(), any())).thenReturn(intArray);

        Map<String, String> camelGlobalOptions = new HashMap<>();
        camelGlobalOptions.put(JOB_ID, "1");
        jrdDataIngestionLibraryRunner.updateJobStatus = "dummyQuery";
        when(camelContext.getGlobalOptions()).thenReturn(camelGlobalOptions);
        when(jdbcTemplate.update(anyString(), any(), anyInt())).thenReturn(1);
    }

    @SneakyThrows
    @Test
    void testRun() {
        jrdDataIngestionLibraryRunner.run(job, jobParameters);
        verify(jobLauncherMock).run(any(), any());
        verify(topicPublisher, times(1)).sendMessage(any(), anyString());
        verify(jdbcTemplate).update(anyString(), any(), anyInt());
        verify(jdbcTemplate).queryForObject(anyString(), (Class<Object>) any());
        verify(jdbcTemplate).queryForObject("failedAuditFileCount", Integer.class);
        verify(jdbcTemplate).batchUpdate(anyString(), anyCollection(), anyInt(), any());
    }

    @SneakyThrows
    @Test
    void testRunWithException() {
        when(featureToggleService.isFlagEnabled(anyString())).thenThrow(new RuntimeException("exception"));
        assertThrows(Exception.class, () -> jrdDataIngestionLibraryRunner.run(job, jobParameters));
        verify(jdbcTemplate).update(anyString(), any(), anyInt());
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
        verify(jdbcTemplate, times(2)).update(anyString(), any(), anyInt());
    }

    @SneakyThrows
    @Test
    void testRunNoMessageToPublish() {
        List<String> sidamIds = new ArrayList<>();
        when(jdbcTemplate.query("dummyQuery", ROW_MAPPER)).thenReturn(sidamIds);
        jrdDataIngestionLibraryRunner.run(job, jobParameters);
        verify(jobLauncherMock).run(any(), any());
        verify(jdbcTemplate, times(2)).queryForObject(anyString(), any(RowMapper.class));
        verify(jdbcTemplate).update(anyString(), any(), anyInt());
    }

    @SneakyThrows
    @Test
    void testRunFailedFiles() {
        when(jdbcTemplate.queryForObject("failedAuditFileCount", Integer.class)).thenReturn(Integer.valueOf(1));
        jrdDataIngestionLibraryRunner.run(job, jobParameters);
        verify(jobLauncherMock).run(any(), any());
    }
}
