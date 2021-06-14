package uk.gov.hmcts.reform.juddata.camel.util;

import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.juddata.camel.util.JobStatus.FAILED;
import static uk.gov.hmcts.reform.juddata.camel.util.JobStatus.IN_PROGRESS;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.JOB_ID;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.ROW_MAPPER;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unchecked")
class JrdAsbPublisherTest {

    TopicPublisher topicPublisher = mock(TopicPublisher.class);

    @InjectMocks
    private JrdAsbPublisher jrdAsbPublisher = spy(new JrdAsbPublisher());

    CamelContext camelContext = mock(CamelContext.class);

    JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

    FeatureToggleService featureToggleService = mock(FeatureToggleService.class);

    JrdSidamTokenService jrdSidamTokenService = mock(JrdSidamTokenServiceImpl.class);

    @BeforeEach
    public void beforeTest() {
        Map<String, String> options = new HashMap<>();
        options.put(JOB_ID, "1");
        List<String> sidamIds = new ArrayList<>();
        sidamIds.add(UUID.randomUUID().toString());
        jrdAsbPublisher.selectJobStatus = "dummyjobstatus";
        jrdAsbPublisher.getSidamIds = "dummyQuery";
        jrdAsbPublisher.failedAuditFileCount = "failedAuditFileCount";

        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class)))
            .thenReturn(Pair.of("1", IN_PROGRESS.getStatus()));
        when(jdbcTemplate.queryForObject("failedAuditFileCount", Integer.class)).thenReturn(Integer.valueOf(0));

        jrdAsbPublisher.logComponentName = "loggingComponent";
        when(camelContext.getGlobalOptions()).thenReturn(options);
        when(jdbcTemplate.query("dummyQuery", ROW_MAPPER)).thenReturn(sidamIds);
        when(jdbcTemplate.update(anyString(), any(), anyInt())).thenReturn(1);
        when(featureToggleService.isFlagEnabled(anyString())).thenReturn(true);
        jrdAsbPublisher.environment = "test";
        IdamClient.User user = new IdamClient.User();
        user.setSsoId(UUID.randomUUID().toString());
        user.setId(UUID.randomUUID().toString());
        Set<IdamClient.User> sidamUsers = ImmutableSet.of(user);
        when(jrdSidamTokenService.getSyncFeed()).thenReturn(sidamUsers);
        jrdAsbPublisher.updateSidamIds = "updateSidamIds";
        int[][] intArray = new int[1][];
        when(jdbcTemplate.batchUpdate(anyString(), anyList(), anyInt(), any())).thenReturn(intArray);
    }

    @SneakyThrows
    @Test
    void testRun() {
        jrdAsbPublisher.executeAsbPublishing();
        verify(topicPublisher, times(1)).sendMessage(any(), anyString());
        verify(jdbcTemplate).batchUpdate(anyString(), anyList(), anyInt(), any());
    }

    @SneakyThrows
    @Test
    void testRunRetry() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class)))
            .thenReturn(Pair.of("1", FAILED.getStatus()));
        jrdAsbPublisher.executeAsbPublishing();
        verify(topicPublisher, times(1)).sendMessage(any(), anyString());
    }

    @SneakyThrows
    @Test
    void testRunException() {
        doThrow(new RuntimeException("Some Exception")).when(topicPublisher).sendMessage(anyList(), anyString());
        assertThrows(Exception.class, () -> jrdAsbPublisher.executeAsbPublishing());
        verify(topicPublisher, times(1)).sendMessage(any(), anyString());
    }

    @SneakyThrows
    @Test
    void testRunNoMessageToPublish() {
        List<String> sidamIds = new ArrayList<>();
        when(jdbcTemplate.query("dummyQuery", ROW_MAPPER)).thenReturn(sidamIds);
        jrdAsbPublisher.executeAsbPublishing();
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(RowMapper.class));
    }

    @SneakyThrows
    @Test
    void testRunFailedFiles() {
        when(jdbcTemplate.queryForObject("failedAuditFileCount", Integer.class)).thenReturn(Integer.valueOf(1));
        jrdAsbPublisher.executeAsbPublishing();
    }

}
