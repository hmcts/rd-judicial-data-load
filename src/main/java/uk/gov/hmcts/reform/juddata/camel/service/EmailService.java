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

    public void sendEmail(final String from) {
        try {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("xyz@hmcts.net");
            message.setSubject("Test");
            message.setText("Test");
            message.setFrom(from);

            mailSender.send(message);

        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }
}
