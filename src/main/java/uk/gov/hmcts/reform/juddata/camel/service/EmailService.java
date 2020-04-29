package uk.gov.hmcts.reform.juddata.camel.service;

import static org.apache.camel.Exchange.EXCEPTION_CAUGHT;

import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.juddata.camel.exception.EmailFailureException;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmailService implements Processor {

    @Autowired
    JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String mailFrom;

    @Value("${spring.mail.to}")
    private String mailTo;

    @Value("${spring.mail.subject}")
    private String mailsubject;

    @Value("${spring.mail.enabled}")
    private boolean mailEnabled;

    public void sendEmail(String messageBody) {
        try {
            //check mail flag and send mail
            if (mailEnabled) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(mailTo);
                message.setSubject(mailsubject);
                message.setText(messageBody);
                message.setFrom(mailFrom);
                mailSender.send(message);
            }
        } catch (MailException e) {
            throw new EmailFailureException(e);
        }
    }

    @Override
    public void process(Exchange exchange) {
        Exception exception = (Exception) exchange.getProperty(EXCEPTION_CAUGHT);
        sendEmail(exception.getMessage());
    }
}
