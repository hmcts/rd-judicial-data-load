package uk.gov.hmcts.reform.juddata.camel.service;

import static org.apache.camel.Exchange.EXCEPTION_CAUGHT;
import static org.apache.camel.Exchange.FILE_NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import javax.mail.internet.MimeMessage;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.javamail.JavaMailSender;
import uk.gov.hmcts.reform.juddata.camel.exception.EmailFailureException;

public class EmailServiceTest extends CamelTestSupport {

    @InjectMocks
    EmailService emailService;

    @Mock
    JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

    }

    @Test
    public void sendEmail() {

        String mailFrom = "no-reply@reform.hmcts.net";
        String mailTo = "sushant.choudhari@hmcts.net,abhijit.diwan@hmcts.net";
        String mailsubject = "Test mail";
        final String  messageBody = "Test";
        final String  filename = "File1.csv";
        setField(emailService, "mailFrom", mailFrom);
        setField(emailService, "mailTo", mailTo);
        setField(emailService, "mailsubject", mailsubject);
        setField(emailService, "mailEnabled", Boolean.TRUE);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailService.sendEmail(messageBody, filename);

        assertEquals("Test", messageBody);
        assertEquals("File1.csv", filename);

    }


    @Test(expected = EmailFailureException.class)
    public void sendEmailException() {

        String mailFrom = "no-reply@reform.hmcts.net";
        String mailTo = "sushant.choudhari@hmcts.net,abhijit.diwan@hmcts.net";
        String mailsubject = "Test mail";
        final Boolean mailEnabled = true;
        final EmailFailureException emailFailureException = new EmailFailureException(new Throwable());

        setField(emailService, "mailFrom", mailFrom);
        setField(emailService, "mailTo", mailTo);
        setField(emailService, "mailsubject", mailsubject);
        setField(emailService, "mailEnabled", mailEnabled);
        doThrow(emailFailureException).when(mailSender).send(any(MimeMessage.class));

        emailService.sendEmail("Test", "File1.csv");
    }

    @Test
    public void process() throws Exception {
        String filename = "File1.csv";
        CamelContext context = new DefaultCamelContext();
        Exchange exchange = createExchangeWithBody(context, "body");
        exchange.setProperty(EXCEPTION_CAUGHT, new Exception());
        exchange.setProperty(FILE_NAME, filename);
        emailService.process(exchange);

    }
}