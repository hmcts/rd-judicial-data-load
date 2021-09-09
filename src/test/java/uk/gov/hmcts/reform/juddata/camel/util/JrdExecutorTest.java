package uk.gov.hmcts.reform.juddata.camel.util;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.FileStatus;
import uk.gov.hmcts.reform.data.ingestion.camel.service.AuditServiceImpl;
import uk.gov.hmcts.reform.data.ingestion.camel.service.EmailServiceImpl;
import uk.gov.hmcts.reform.data.ingestion.camel.service.dto.Email;
import uk.gov.hmcts.reform.data.ingestion.camel.util.DataLoadUtil;
import uk.gov.hmcts.reform.juddata.configuration.EmailConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.ERROR_MESSAGE;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.FAILURE;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.SUCCESS;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.IS_PARENT;

@ExtendWith(SpringExtension.class)
class JrdExecutorTest {
    JrdExecutor jrdExecutor = new JrdExecutor();

    JrdExecutor jrdExecutorSpy = spy(jrdExecutor);

    CamelContext camelContext = new DefaultCamelContext();

    AuditServiceImpl auditService = mock(AuditServiceImpl.class);

    ProducerTemplate producerTemplate = mock(ProducerTemplate.class);

    DataLoadUtil dataLoadUtil = mock(DataLoadUtil.class);
    MockedStatic<DataLoadUtil> dataLoadUtilMock = mockStatic(DataLoadUtil.class);

    EmailServiceImpl emailService = mock(EmailServiceImpl.class);

    @BeforeEach
    public void init() {
        setField(jrdExecutorSpy, "judicialAuditServiceImpl", auditService);
        setField(jrdExecutorSpy, "emailService", emailService);
        camelContext.getGlobalOptions().put(ERROR_MESSAGE, ERROR_MESSAGE);
        List<String> archivalFileNames = new ArrayList<>();
        archivalFileNames.add("test");
        setField(jrdExecutorSpy, "archivalFileNames", archivalFileNames);
    }

    @Test
    void testExecute() {
        camelContext.getGlobalOptions().put(IS_PARENT, String.valueOf(TRUE));
        setField(jrdExecutorSpy, "dataLoadUtil", dataLoadUtil);
        setField(jrdExecutorSpy, "producerTemplate", producerTemplate);

        doNothing().when(producerTemplate).sendBody(any());
        doNothing().when(auditService).auditSchedulerStatus(camelContext);
        dataLoadUtilMock.when(() -> DataLoadUtil.getFileDetails(any(), anyString())).thenReturn(FileStatus.builder()
                .auditStatus(SUCCESS)
                .fileName("Test.csv")
                .build());
        assertEquals(SUCCESS, jrdExecutorSpy.execute(camelContext, "test", "test"));
        verify(jrdExecutorSpy, times(1))
            .execute(camelContext, "test", "test");
        verify(auditService, times(1))
            .auditSchedulerStatus(camelContext);
        verify(emailService, times(0)).sendEmail(any(Email.class));
    }

    @Test
    void testExecute_WhenFileFailures() {
        EmailConfiguration.MailTypeConfig mailTypeConfig = new EmailConfiguration.MailTypeConfig();
        mailTypeConfig.setEnabled(true);
        mailTypeConfig.setSubject("%s :: Publishing of JRD messages to ASB failed");
        mailTypeConfig.setBody("Publishing of JRD messages to ASB failed for Job Id %s");
        mailTypeConfig.setFrom("test@test.com");
        mailTypeConfig.setTo(List.of("test@test.com"));
        EmailConfiguration emailConfiguration = new EmailConfiguration();
        emailConfiguration.setMailTypes(Map.of("report", mailTypeConfig));
        camelContext.getGlobalOptions().put(IS_PARENT, String.valueOf(TRUE));
        setField(jrdExecutorSpy, "dataLoadUtil", dataLoadUtil);
        setField(jrdExecutorSpy, "producerTemplate", producerTemplate);
        setField(jrdExecutorSpy, "emailConfiguration", emailConfiguration);

        doNothing().when(producerTemplate).sendBody(any());
        doNothing().when(auditService).auditSchedulerStatus(camelContext);
        when(emailService.sendEmail(any(Email.class))).thenReturn(200);
        dataLoadUtilMock.when(() -> DataLoadUtil.getFileDetails(any(), anyString())).thenReturn(FileStatus.builder()
                .auditStatus(FAILURE)
                .fileName("Test.csv")
                .build());
        assertEquals(SUCCESS, jrdExecutorSpy.execute(camelContext, "test", "test"));
        verify(jrdExecutorSpy, times(1))
                .execute(camelContext, "test", "test");
        verify(auditService, times(1))
                .auditSchedulerStatus(camelContext);
        verify(emailService, times(1)).sendEmail(any(Email.class));
    }

    @Test
    void testExecute_WhenFileFailures_EmailNotEnabled() {
        EmailConfiguration.MailTypeConfig mailTypeConfig = new EmailConfiguration.MailTypeConfig();
        mailTypeConfig.setEnabled(false);
        mailTypeConfig.setSubject("%s :: Publishing of JRD messages to ASB failed");
        mailTypeConfig.setBody("Publishing of JRD messages to ASB failed for Job Id %s");
        mailTypeConfig.setFrom("test@test.com");
        mailTypeConfig.setTo(List.of("test@test.com"));
        EmailConfiguration emailConfiguration = new EmailConfiguration();
        emailConfiguration.setMailTypes(Map.of("report", mailTypeConfig));
        camelContext.getGlobalOptions().put(IS_PARENT, String.valueOf(TRUE));
        setField(jrdExecutorSpy, "dataLoadUtil", dataLoadUtil);
        setField(jrdExecutorSpy, "producerTemplate", producerTemplate);
        setField(jrdExecutorSpy, "emailConfiguration", emailConfiguration);

        doNothing().when(producerTemplate).sendBody(any());
        doNothing().when(auditService).auditSchedulerStatus(camelContext);
        dataLoadUtilMock.when(() -> DataLoadUtil.getFileDetails(any(), anyString())).thenReturn(FileStatus.builder()
                .auditStatus(FAILURE)
                .fileName("Test.csv")
                .build());
        assertEquals(SUCCESS, jrdExecutorSpy.execute(camelContext, "test", "test"));
        verify(jrdExecutorSpy, times(1))
                .execute(camelContext, "test", "test");
        verify(auditService, times(1))
                .auditSchedulerStatus(camelContext);
        verify(emailService, times(0)).sendEmail(any(Email.class));
    }

    @AfterEach
    void tearDown() {
        dataLoadUtilMock.close();
    }
}
