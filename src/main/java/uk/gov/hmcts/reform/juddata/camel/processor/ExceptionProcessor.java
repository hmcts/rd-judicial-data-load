package uk.gov.hmcts.reform.juddata.camel.processor;

import static org.apache.camel.Exchange.EXCEPTION_CAUGHT;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.service.EmailData;
import uk.gov.hmcts.reform.juddata.camel.service.EmailService;

@Component
@Slf4j
public class ExceptionProcessor implements Processor {

    @Autowired
    EmailService emailService;

    @Value("${spring.mail.from}")
    String mailFrom;

    @Value("${spring.mail.to}")
    String mailTo;

    @Value("${spring.mail.subject}")
    String mailSubject;

    @Value("${spring.mail.enabled}")
    Boolean mailEnabled;

    @Override
    public void process(Exchange exchange) {
        Exception exception = (Exception) exchange.getProperty(EXCEPTION_CAUGHT);
        if (mailEnabled.booleanValue()) {
            EmailData emailData = new EmailData();
            emailData.setRecipient(mailTo);
            emailData.setMessage(exception.getMessage());
            emailData.setSubject(mailSubject);
            emailService.sendEmail(mailFrom, emailData);
        }
        log.error("::::exception in route for data processing::::" + exception);
    }
}
