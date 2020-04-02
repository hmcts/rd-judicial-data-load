package uk.gov.hmcts.reform.juddata.camel.processor;

import static org.apache.camel.Exchange.EXCEPTION_CAUGHT;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.email.EmailService;
import uk.gov.hmcts.reform.juddata.camel.email.Mail;

@Component
@Slf4j
public class ExceptionProcessor implements Processor {

    @Autowired
    EmailService emailService;

    @Override
    public void process(Exchange exchange) {
        Exception exception = (Exception) exchange.getProperty(EXCEPTION_CAUGHT);
        Mail mail=new Mail();
        mail.setFrom("sushant.choudhari@hmcts.net");
        mail.setTo("sushant.choudhari@hmcts.net");
        mail.setSubject("Test Email");
        mail.setContent("Hi  this is  test email");
        emailService.sendSimpleMessage(mail);
        log.error("::::exception in route for data processing::::" + exception);
    }
}
