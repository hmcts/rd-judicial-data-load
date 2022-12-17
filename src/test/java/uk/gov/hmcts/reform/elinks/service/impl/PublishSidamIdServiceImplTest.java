package uk.gov.hmcts.reform.elinks.service.impl;

import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import uk.gov.hmcts.reform.data.ingestion.camel.service.EmailServiceImpl;
import uk.gov.hmcts.reform.data.ingestion.camel.service.dto.Email;
import uk.gov.hmcts.reform.elinks.configuration.ElinkEmailConfiguration;
import uk.gov.hmcts.reform.elinks.response.SchedulerJobStatusResponse;
import uk.gov.hmcts.reform.elinks.servicebus.ElinkTopicPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.elinks.util.JobStatus.FAILED;
import static uk.gov.hmcts.reform.elinks.util.JobStatus.IN_PROGRESS;
import static uk.gov.hmcts.reform.elinks.util.JobStatus.SUCCESS;
import static uk.gov.hmcts.reform.elinks.util.RefDataConstants.ROW_MAPPER;
import static uk.gov.hmcts.reform.elinks.util.SqlContants.GET_DISTINCT_SIDAM_ID;


@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unchecked")
class PublishSidamIdServiceImplTest {

    @InjectMocks
    PublishSidamIdServiceImpl publishSidamIdService;

    @Mock
    JdbcTemplate jdbcTemplate;

    @Mock
    ElinkTopicPublisher elinkTopicPublisher;

    @Mock
    EmailServiceImpl emailService;

    List<String> sidamIds = new ArrayList<>();

    @BeforeEach
    public void beforeTest() {

        sidamIds.add("edc4190e-8e31-47d5-af56-cb7784bcd3a9");
        publishSidamIdService.logComponentName = "RD_Elink_Judicial_Ref_Data";
    }

    @SneakyThrows
    @Test
    @DisplayName("Positive Scenario: Pusblish message to ASb when job status is in progress")
    void should_publish_when_job_status_is_inprogress() {

        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class)))
                .thenReturn(Pair.of("2", IN_PROGRESS.getStatus()));

        SchedulerJobStatusResponse res = publishSidamIdService.publishSidamIdToAsb();

        assertEquals("2",res.getId());
        assertEquals("IN_PROGRESS", res.getJobStatus());
        assertEquals(HttpStatus.OK.value(),res.getStatusCode());
        verify(jdbcTemplate, times(1)).update(anyString(), any(), anyInt());

    }

    @SneakyThrows
    @Test
    @DisplayName("Should retry when job status is_failed")
    void should_retry_when_job_status_is_failed() {

        when(jdbcTemplate.query(GET_DISTINCT_SIDAM_ID, ROW_MAPPER)).thenReturn(sidamIds);
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class)))
                .thenReturn(Pair.of("1", FAILED.getStatus()));

        SchedulerJobStatusResponse res = publishSidamIdService.publishSidamIdToAsb();

        assertEquals("FAILED",res.getJobStatus());
        assertEquals("1",res.getId());
        assertEquals(HttpStatus.OK.value(),res.getStatusCode());
        assertEquals(sidamIds.size(),res.getSidamIdsCount());

    }


    @SneakyThrows
    @Test
    @DisplayName("When no sidam id's is available then just update the status in db")
    void update_succes_as_job_status_when_no_sidam_ids_available() {
        List<String> sidamIds = new ArrayList<>();
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class)))
                .thenReturn(Pair.of("2", SUCCESS.getStatus()));
        when(jdbcTemplate.query(GET_DISTINCT_SIDAM_ID, ROW_MAPPER)).thenReturn(sidamIds);

        SchedulerJobStatusResponse res = publishSidamIdService.publishSidamIdToAsb();


        assertEquals("SUCCESS",res.getJobStatus());
        assertEquals(sidamIds.size(),res.getSidamIdsCount());
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        verify(jdbcTemplate, times(1)).update(anyString(), any(), anyInt());

    }


    @SneakyThrows
    @Test
    @DisplayName("Negative Scenario: should throw exception for any issue during get job status")
    void test_when_get_job_details_runs_into_an_exception() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class)))
                .thenThrow(new EmptyResultDataAccessException(1));
        publishSidamIdService.publishSidamIdToAsb();
        verify(elinkTopicPublisher, times(0)).sendMessage(any(), anyString());
        verify(jdbcTemplate, times(1)).update(anyString(), any(), anyInt());
    }


    @SneakyThrows
    @Test
    @DisplayName("Negative Scenario: should throw exception when email is not enabled")
    void should_throw_exception_when_email_is_enabled() {

        doThrow(new RuntimeException("Some Exception")).when(elinkTopicPublisher).sendMessage(sidamIds,"1");
        ElinkEmailConfiguration.MailTypeConfig mailTypeConfig = new ElinkEmailConfiguration.MailTypeConfig();
        mailTypeConfig.setEnabled(true);
        mailTypeConfig.setSubject("%s :: Publishing of JRD messages to ASB failed");
        mailTypeConfig.setBody("Publishing of JRD messages to ASB failed for Job Id %s");
        mailTypeConfig.setFrom("test@test.com");
        mailTypeConfig.setTo(List.of("test@test.com"));
        ElinkEmailConfiguration emailConfiguration = new ElinkEmailConfiguration();
        emailConfiguration.setMailTypes(Map.of("asb", mailTypeConfig));
        publishSidamIdService.emailConfiguration = emailConfiguration;

        assertThrows(Exception.class,
                () -> publishSidamIdService.publishMessage(IN_PROGRESS.getStatus(), sidamIds, "1"));
        verify(emailService, times(1)).sendEmail(any(Email.class));
        verify(elinkTopicPublisher, times(1)).sendMessage(any(), anyString());
        verify(jdbcTemplate, times(1)).update(anyString(), any(), anyInt());
    }

    @SneakyThrows
    @Test
    @DisplayName("Negative Scenario: should throw exception when email is not enabled")
    void should_throw_exception_when_email_is_not_enabled() {

        doThrow(new RuntimeException("Some Exception")).when(elinkTopicPublisher).sendMessage(sidamIds,"1");
        ElinkEmailConfiguration.MailTypeConfig mailTypeConfig = new ElinkEmailConfiguration.MailTypeConfig();
        mailTypeConfig.setEnabled(false);
        mailTypeConfig.setSubject("%s :: Publishing of JRD messages to ASB failed");
        mailTypeConfig.setBody("Publishing of JRD messages to ASB failed for Job Id %s");
        mailTypeConfig.setFrom("test@test.com");
        mailTypeConfig.setTo(List.of("test@test.com"));
        ElinkEmailConfiguration emailConfiguration = new ElinkEmailConfiguration();
        emailConfiguration.setMailTypes(Map.of("asb", mailTypeConfig));
        publishSidamIdService.emailConfiguration = emailConfiguration;

        assertThrows(Exception.class,
                () -> publishSidamIdService.publishMessage(IN_PROGRESS.getStatus(), sidamIds, "1"));
        verify(emailService, times(0)).sendEmail(any(Email.class));
        verify(elinkTopicPublisher, times(1)).sendMessage(any(), anyString());
        verify(jdbcTemplate, times(1)).update(anyString(), any(), anyInt());
    }

    @SneakyThrows
    @Test
    @DisplayName("Positive scenario when status is in progress and sidam id is not null")
    void whould_send_messages_when_sidam_id_not_null() {
        publishSidamIdService.publishMessage(IN_PROGRESS.getStatus(),sidamIds,"2");

        verify(elinkTopicPublisher).sendMessage(sidamIds,"2");
        verify(jdbcTemplate, times(1)).update(anyString(), any(), anyInt());
    }

}