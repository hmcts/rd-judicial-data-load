package uk.gov.hmcts.reform.juddata.camel.processor;

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
    private String mailFrom;

    @Value("${spring.mail.to}")
    private String mailTo;

    @Value("${spring.mail.subject}")
    private String mailsubject;

    @Override
    public void process(Exchange exchange) {
        Exception exception = (Exception) exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
        String failedRouteId = exchange.getProperty(Exchange.FAILURE_ROUTE_ID, String.class);
        emailService.sendEmail(mailFrom, EmailData.builder().message(exception.getMessage()).subject(mailsubject + failedRouteId).recipient(mailTo)
            .build());
        log.error("::::exception in route for data processing::::" + exception);
    }
}
