package uk.gov.hmcts.reform.juddata.camel.service;

import static org.apache.camel.Exchange.EXCEPTION_CAUGHT;
import static org.apache.camel.Exchange.FILE_NAME;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.juddata.camel.exception.EmailFailureException;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class EmailService implements Processor {

    @Autowired
    JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    String mailFrom;

    @Value("${spring.mail.to}")
    String mailTo;

    @Value("${spring.mail.subject}")
    String mailsubject;

    @Value("${spring.mail.enabled}")
    boolean mailEnabled;

    public void sendEmail(String messageBody, String filename) {
        if (mailEnabled) {
            try {
                //check mail flag and send mail
                MimeMessage message = mailSender.createMimeMessage();

                MimeMessageHelper mimeMsgHelperObj = new MimeMessageHelper(message, true);
                String[] split = mailTo.split(",");
                mimeMsgHelperObj.setTo(split);
                mimeMsgHelperObj.setSubject(mailsubject + filename);
                mimeMsgHelperObj.setText(messageBody);
                mimeMsgHelperObj.setFrom(mailFrom);
                mailSender.send(mimeMsgHelperObj.getMimeMessage());
                log.info("::::  sending mail ::::",mailTo );

            } catch (MailException | MessagingException e) {
                log.error("::::MailException  while  sending mail ::::" + e.toString());
                throw new EmailFailureException(e);
            }
        }
    }

    @Override
    public void process(Exchange exchange) {
        Exception exception = (Exception) exchange.getProperty(EXCEPTION_CAUGHT);
        String fileName = (String) exchange.getProperty(FILE_NAME);
        sendEmail(exception.getMessage(), fileName);
    }
}
