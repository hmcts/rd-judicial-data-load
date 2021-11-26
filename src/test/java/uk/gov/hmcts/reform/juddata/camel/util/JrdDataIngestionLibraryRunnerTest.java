package uk.gov.hmcts.reform.juddata.camel.util;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.BlobProperties;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.testcontainers.shaded.com.google.common.collect.ImmutableSet;
import uk.gov.hmcts.reform.data.ingestion.camel.service.AuditServiceImpl;
import uk.gov.hmcts.reform.data.ingestion.camel.service.EmailServiceImpl;
import uk.gov.hmcts.reform.data.ingestion.camel.service.dto.Email;
import uk.gov.hmcts.reform.data.ingestion.configuration.AzureBlobConfig;
import uk.gov.hmcts.reform.juddata.camel.servicebus.TopicPublisher;
import uk.gov.hmcts.reform.juddata.client.IdamClient;
import uk.gov.hmcts.reform.juddata.configuration.EmailConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.juddata.camel.util.JobStatus.FAILED;
import static uk.gov.hmcts.reform.juddata.camel.util.JobStatus.SUCCESS;
import static uk.gov.hmcts.reform.juddata.camel.util.JobStatus.IN_PROGRESS;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.JOB_ID;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.ROW_MAPPER;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unchecked")
class JrdDataIngestionLibraryRunnerTest {

    TopicPublisher topicPublisher = mock(TopicPublisher.class);

    @Mock
    CloudStorageAccount cloudStorageAccount;

    @Mock
    AzureBlobConfig azureBlobConfig;

    @Mock
    CloudBlobClient blobClient;

    @Mock
    CloudBlobContainer container;

    @Mock
    CloudBlockBlob cloudBlockBlob;

    @Mock
    BlobProperties blobProperties;

    @Mock
    AuditServiceImpl auditServiceImpl;

    @InjectMocks
    private JrdDataIngestionLibraryRunner jrdDataIngestionLibraryRunner;

    CamelContext camelContext = mock(CamelContext.class);

    JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

    Job job = mock(Job.class);

    JobParameters jobParameters = mock(JobParameters.class);

    JobLauncher jobLauncherMock = mock(JobLauncher.class);

    FeatureToggleService featureToggleService = mock(FeatureToggleService.class);

    JrdSidamTokenService jrdSidamTokenService = mock(JrdSidamTokenServiceImpl.class);

    EmailServiceImpl emailService = mock(EmailServiceImpl.class);

    @BeforeEach
    public void beforeTest() throws Exception {
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
        when(jdbcTemplate.queryForObject("failedAuditFileCount", Integer.class)).thenReturn(0);

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

        when(cloudStorageAccount.createCloudBlobClient()).thenReturn(blobClient);
        when(azureBlobConfig.getContainerName()).thenReturn("test");
        when(blobClient.getContainerReference(anyString())).thenReturn(container);
        when(container.getBlockBlobReference(any())).thenReturn(cloudBlockBlob);
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
        EmailConfiguration.MailTypeConfig mailTypeConfig = new EmailConfiguration.MailTypeConfig();
        mailTypeConfig.setEnabled(true);
        mailTypeConfig.setSubject("%s :: Publishing of JRD messages to ASB failed");
        mailTypeConfig.setBody("Publishing of JRD messages to ASB failed for Job Id %s");
        mailTypeConfig.setFrom("test@test.com");
        mailTypeConfig.setTo(List.of("test@test.com"));
        EmailConfiguration emailConfiguration = new EmailConfiguration();
        emailConfiguration.setMailTypes(Map.of("asb", mailTypeConfig));
        jrdDataIngestionLibraryRunner.emailConfiguration = emailConfiguration;
        assertThrows(Exception.class, () -> jrdDataIngestionLibraryRunner.run(job, jobParameters));
        verify(topicPublisher, times(1)).sendMessage(any(), anyString());
        verify(jdbcTemplate, times(2)).update(anyString(), any(), anyInt());
        verify(emailService, times(1)).sendEmail(any(Email.class));
    }

    @SneakyThrows
    @Test
    void testRunException_WhenEmailNotEnabled() {
        doThrow(new RuntimeException("Some Exception")).when(topicPublisher).sendMessage(anyList(), anyString());
        EmailConfiguration.MailTypeConfig mailTypeConfig = new EmailConfiguration.MailTypeConfig();
        mailTypeConfig.setEnabled(false);
        mailTypeConfig.setSubject("%s :: Publishing of JRD messages to ASB failed");
        mailTypeConfig.setBody("Publishing of JRD messages to ASB failed for Job Id %s");
        mailTypeConfig.setFrom("test@test.com");
        mailTypeConfig.setTo(List.of("test@test.com"));
        EmailConfiguration emailConfiguration = new EmailConfiguration();
        emailConfiguration.setMailTypes(Map.of("asb", mailTypeConfig));
        jrdDataIngestionLibraryRunner.emailConfiguration = emailConfiguration;
        assertThrows(Exception.class, () -> jrdDataIngestionLibraryRunner.run(job, jobParameters));
        verify(topicPublisher, times(1)).sendMessage(any(), anyString());
        verify(jdbcTemplate, times(2)).update(anyString(), any(), anyInt());
        verify(emailService, times(0)).sendEmail(any(Email.class));
    }

    @SneakyThrows
    @Test
    void testRunNoMessageToPublish() {
        List<String> sidamIds = new ArrayList<>();
        when(jdbcTemplate.query("dummyQuery", ROW_MAPPER)).thenReturn(sidamIds);
        jrdDataIngestionLibraryRunner.run(job, jobParameters);
        verify(jobLauncherMock).run(any(), any());
        verify(jdbcTemplate, times(3)).queryForObject(anyString(), any(RowMapper.class));
        verify(jdbcTemplate).update(anyString(), any(), anyInt());
    }

    @SneakyThrows
    @Test
    void testRunFailedFiles() {
        when(jdbcTemplate.queryForObject("failedAuditFileCount", Integer.class)).thenReturn(1);
        jrdDataIngestionLibraryRunner.run(job, jobParameters);
        verify(jobLauncherMock).run(any(), any());
    }

    @SneakyThrows
    @Test
    void test_when_get_job_details_runs_into_an_exception() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class)))
                .thenThrow(new EmptyResultDataAccessException(1));
        jrdDataIngestionLibraryRunner.run(job, jobParameters);
        verify(topicPublisher, times(0)).sendMessage(any(), anyString());
    }

    @SneakyThrows
    @Test
    void test_when_get_job_details_returns_no_exception() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class)))
                .thenReturn(Pair.of("1", SUCCESS.getStatus()));
        jrdDataIngestionLibraryRunner.run(job, jobParameters);
        verify(topicPublisher, times(0)).sendMessage(any(), anyString());
    }

    @Test
    void should_return_true_when_publishing_status_is_success_for_prev_day() throws Exception {
        when(jdbcTemplate.queryForObject(any(), eq(String.class))).thenReturn(SUCCESS.getStatus());
        when(auditServiceImpl.hasDataIngestionRunAfterFileUpload(any())).thenReturn(true);
        assertTrue(jrdDataIngestionLibraryRunner.noFileUploadAfterSuccessfulDataIngestionOnPreviousDay());
    }

    @Test
    void should_return_false_when_publishing_status_is_failed_for_prev_day() throws Exception {
        when(jdbcTemplate.queryForObject(any(), eq(String.class))).thenReturn(FAILED.getStatus());
        when(auditServiceImpl.hasDataIngestionRunAfterFileUpload(any())).thenReturn(true);
        assertFalse(jrdDataIngestionLibraryRunner.noFileUploadAfterSuccessfulDataIngestionOnPreviousDay());
    }

    @Test
    void should_return_false_when_publishing_status_is_success_and_file_uploaded_after_data_ingestion()
            throws Exception {
        when(jdbcTemplate.queryForObject(any(), eq(String.class))).thenReturn(SUCCESS.getStatus());
        when(auditServiceImpl.hasDataIngestionRunAfterFileUpload(any())).thenReturn(false);
        assertFalse(jrdDataIngestionLibraryRunner.noFileUploadAfterSuccessfulDataIngestionOnPreviousDay());
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void should_return_true_when_publishing_status_is_success_for_current_day() throws Exception {
        when(jdbcTemplate.queryForObject(any(), eq(String.class))).thenReturn(SUCCESS.getStatus());
        when(jdbcTemplate.queryForObject(any(), eq(Integer.class))).thenReturn(0);
        assertTrue(jrdDataIngestionLibraryRunner.currentDayPublishingStatusIsSuccessOrFileLoadFailed());
        verify(jdbcTemplate, times(0)).update(any(), any(), any());
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void should_return_false_when_publishing_status_is_failed_for_current_day() throws Exception {
        when(jdbcTemplate.queryForObject(any(), eq(String.class))).thenReturn(FAILED.getStatus());
        when(jdbcTemplate.queryForObject(any(), eq(Integer.class))).thenReturn(0);
        assertFalse(jrdDataIngestionLibraryRunner.currentDayPublishingStatusIsSuccessOrFileLoadFailed());
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void should_return_true_when_file_load_failed_for_current_day() throws Exception {
        when(jdbcTemplate.queryForObject(any(), eq(String.class))).thenReturn(null);
        when(jdbcTemplate.queryForObject(any(), eq(Integer.class))).thenReturn(1);
        assertTrue(jrdDataIngestionLibraryRunner.currentDayPublishingStatusIsSuccessOrFileLoadFailed());
        verify(jdbcTemplate, times(1)).update(anyString(), (Object[]) any());
    }
}
