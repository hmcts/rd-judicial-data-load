package uk.gov.hmcts.reform.juddata.camel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmailService {

    @Autowired
    JavaMailSender mailSender;

    public void sendEmail(final String from,EmailData emailData) {
        try {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailData.recipient);
            message.setSubject(emailData.subject);
            message.setText(emailData.message);
            message.setFrom(from);

            mailSender.send(message);

        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }
}
