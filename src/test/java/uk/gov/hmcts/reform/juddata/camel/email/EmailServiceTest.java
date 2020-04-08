package uk.gov.hmcts.reform.juddata.camel.email;

import static java.lang.String.join;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import uk.gov.hmcts.reform.juddata.camel.service.EmailData;
import uk.gov.hmcts.reform.juddata.camel.service.EmailFailedSendException;
import uk.gov.hmcts.reform.juddata.camel.service.EmailService;



public class EmailServiceTest {
    private static final String FAMILY_MAN_CASE_NUMBER = randomAlphabetic(12);
    private static final String EMAIL_TO = "recipient@example.com";
    private static final String EMAIL_FROM = "no-reply@exaple.com";
    private static final String EMAIL_SUBJECT = join("", "CaseSubmitted_", FAMILY_MAN_CASE_NUMBER);
    @Mock
    SimpleMailMessage mimeMessage;
    @InjectMocks
    EmailService emailService;
    @Mock
    private JavaMailSender mailSender;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldSendEmailSuccessfullyWhenEmailDataValid() {
        EmailData emailData = TestEmailData.getDefault();
        emailService.sendEmail(EMAIL_FROM);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void shouldSendEmailWhenException() {
        EmailData emailData = TestEmailData.getDefault();
        EmailFailedSendException emailFailedSendException = mock(EmailFailedSendException.class);
        doThrow(EmailFailedSendException.class).when(mailSender).send(any(SimpleMailMessage.class));
        final Throwable raisedException = catchThrowable(() -> emailService.sendEmail(EMAIL_FROM));
        assertThat(raisedException).isExactlyInstanceOf(EmailFailedSendException.class);
    }

    static class TestEmailData {
        static EmailData getDefault() {
            return EmailData.builder()
                    .recipient(EMAIL_TO)
                    .subject(EMAIL_SUBJECT)
                    .message("")
                    .build();
        }
    }

    static class TestEmailDataNull {
        static EmailData getDefault() {
            return EmailData.builder()
                    .recipient(null)
                    .subject(EMAIL_SUBJECT)
                    .message("")
                    .build();
        }
    }
}
