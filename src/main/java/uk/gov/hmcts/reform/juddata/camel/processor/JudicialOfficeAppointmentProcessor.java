package uk.gov.hmcts.reform.juddata.camel.processor;

import static java.util.Collections.singletonList;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.validator.JsrValidatorInitializer;

@Slf4j
@Component
public class JudicialOfficeAppointmentProcessor extends JsrValidationBaseProcessor<JudicialOfficeAppointment> {

    @Autowired
    JsrValidatorInitializer<JudicialOfficeAppointment> judicialOfficeAppointmentJsrValidatorInitializer;

    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) {

        List<JudicialOfficeAppointment> judicialOfficeAppointments;

        judicialOfficeAppointments = (exchange.getIn().getBody() instanceof List)
                ? (List<JudicialOfficeAppointment>) exchange.getIn().getBody()
                : singletonList((JudicialOfficeAppointment) exchange.getIn().getBody());

        log.info("Judicial Appointment Records count before Validation:: " + judicialOfficeAppointments.size());

        List<JudicialOfficeAppointment> filteredJudicialAppointments = validate(judicialOfficeAppointmentJsrValidatorInitializer,
                judicialOfficeAppointments);

        log.info("Judicial Appointment Records count after Validation:: " + filteredJudicialAppointments.size());

        audit(judicialOfficeAppointmentJsrValidatorInitializer, exchange);

        exchange.getMessage().setBody(filteredJudicialAppointments);
    }
}
