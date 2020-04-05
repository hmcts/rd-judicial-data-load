package uk.gov.hmcts.reform.juddata.camel.processor;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.exception.RouteFailedException;
import uk.gov.hmcts.reform.juddata.camel.validator.JSRValidatorInitializer;

@Slf4j
@Component
public class JudicialOfficeAppointmentProcessor implements Processor, DefaultProcessor<JudicialOfficeAppointment>  {


    @Autowired
    JSRValidatorInitializer<JudicialOfficeAppointment> judicialOfficeAppointmentJSRValidatorInitializer;

    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) {

        List<JudicialOfficeAppointment> judicialOfficeAppointments;

        if (exchange.getIn().getBody() instanceof List) {
            judicialOfficeAppointments = (List<JudicialOfficeAppointment>) exchange.getIn().getBody();
        } else {
            JudicialOfficeAppointment judicialOfficeAppointment = (JudicialOfficeAppointment) exchange.getIn().getBody();
            judicialOfficeAppointments = new ArrayList<>();
            judicialOfficeAppointments.add(judicialOfficeAppointment);
        }

        log.info("::JudicialOfficeAppointment Records count::" + judicialOfficeAppointments.size());

        List<JudicialOfficeAppointment> filteredJudicialAppointments = validate(judicialOfficeAppointmentJSRValidatorInitializer,
                judicialOfficeAppointments);

        exchange.getMessage().setBody(filteredJudicialAppointments);

        if(judicialOfficeAppointmentJSRValidatorInitializer.getConstraintViolations().size() > 5) {
            throw new RouteFailedException("Jsr exception exceeds threshold limit in Judicial User Profile");
        } else {
            //Auditing JSR exceptions in exception table
            audit(judicialOfficeAppointmentJSRValidatorInitializer, exchange);
        }

        log.info("::JudicialOfficeAppointment Records invalid for JSR::"
                + (judicialOfficeAppointments.size() - filteredJudicialAppointments.size()));
    }
}
