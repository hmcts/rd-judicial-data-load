package uk.gov.hmcts.reform.juddata.camel.email;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;

import org.junit.Before;
import org.junit.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper;
import uk.gov.hmcts.reform.juddata.camel.service.EmailData;
import uk.gov.hmcts.reform.juddata.camel.service.EmailService;

public class EmailServiceTest {
    private static final String EMAIL_TO = "recipient@example.com";
    private static final String EMAIL_FROM = "no-reply@exaple.com";
    private static final String EMAIL_SUBJECT = "Exception received at Rout id";

    @Mock
    SimpleMailMessage mimeMessage;

    @InjectMocks
    EmailService emailService;
    @Mock
    JavaMailSender mailSender;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldSendEmailSuccessfullyWhenEmailDataValid() {
        EmailData emailData = TestEmailData.getDefault();
        emailService.sendEmail(EMAIL_FROM, emailData);

        Mockito.verify(mailSender, Mockito.times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void shouldSendEmailWhenException() {
        EmailData emailData = JrdUnitTestHelper.getMockEmail();
        RuntimeException emailFailedSendException = Mockito.mock(RuntimeException.class);
        Mockito.doThrow(RuntimeException.class).when(mailSender).send(any(SimpleMailMessage.class));
        final Throwable raisedException = catchThrowable(() -> emailService.sendEmail(EMAIL_FROM, emailData));
        assertThat(raisedException).isExactlyInstanceOf(RuntimeException.class);
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
}
