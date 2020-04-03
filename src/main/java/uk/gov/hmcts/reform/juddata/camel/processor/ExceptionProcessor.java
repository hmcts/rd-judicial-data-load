package uk.gov.hmcts.reform.juddata.camel.processor;

import static org.apache.camel.Exchange.EXCEPTION_CAUGHT;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.email.EmailService;
import uk.gov.hmcts.reform.juddata.camel.email.Mail;

@Component
@Slf4j
public class ExceptionProcessor implements Processor {

    @Autowired
    EmailService emailService;

//    @Value("${mail.from}")
//    private String mailFrom;
//
//    @Value("${mail.to}")
//    private String mailTo;

    @Override
    public void process(Exchange exchange) {
        Exception exception = (Exception) exchange.getProperty(EXCEPTION_CAUGHT);
        String failedRouteId = exchange.getProperty(Exchange.FAILURE_ROUTE_ID, String.class);

        sendExceptionMail(exception, failedRouteId);
        log.error("::::exception in route for data processing::::" + exception);
    }

    private void sendExceptionMail(Exception exception, String failedRouteId) {
        Mail mail = new Mail();
        mail.setFrom("sushant.choudhari@hmcts.net");
        mail.setTo("sushant.choudhari@hmcts.net");
        mail.setSubject("::::exception in route for data processing::::");
        mail.setContent(exception.getMessage());
        emailService.sendSimpleMessage(mail);
    }
}
