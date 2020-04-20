package uk.gov.hmcts.reform.juddata.camel.email;

import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;

import org.mockito.MockitoAnnotations;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import uk.gov.hmcts.reform.juddata.camel.service.EmailService;

public class EmailServiceTest extends CamelTestSupport {
    private static final String EMAIL_FROM = "no-reply@example.com";

    @Mock
    SimpleMailMessage mimeMessage;

    @Mock
    EmailService emailService;

    @Mock
    JavaMailSender mailSender;

    @Mock
    JavaMailSenderImpl javaMailSenderImpl;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldSendEmailSuccessfullyWhenEmailDataValid() {
        setField(emailService,"mailFrom","no-reply@reform.hmcts.net");
        setField(emailService,"mailTo","sushant.choudhari@hmcts.net,abhijit.diwan@hmcts.net");
        setField(emailService,"mailsubject", "Test Mail");
        setField(emailService,"mailEnabled", Boolean.TRUE);
        setField(emailService,"mailSender", javaMailSenderImpl);

        emailService.sendEmail(EMAIL_FROM,"Test");
        verify(emailService).sendEmail(EMAIL_FROM,"Test");
    }
}

