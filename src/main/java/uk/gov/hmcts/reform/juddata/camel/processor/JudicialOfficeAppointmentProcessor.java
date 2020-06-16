package uk.gov.hmcts.reform.juddata.camel.processor;

import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.ELINKS_ID;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.validator.JsrValidatorInitializer;

@Slf4j
@Component
public class JudicialOfficeAppointmentProcessor extends JsrValidationBaseProcessor<JudicialOfficeAppointment> {

    @Autowired
    JsrValidatorInitializer<JudicialOfficeAppointment> judicialOfficeAppointmentJsrValidatorInitializer;

    @Autowired
    JsrValidatorInitializer<JudicialUserProfile> judicialUserProfileJsrValidatorInitializer;

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

        List<JudicialUserProfile> invalidJudicialUserProfileRecords = judicialUserProfileJsrValidatorInitializer.getInvalidJsrRecords();

        filterInvalidUserProfileRecords(filteredJudicialAppointments, invalidJudicialUserProfileRecords, exchange);

        log.info("Judicial Appointment Records count after Validation:: " + filteredJudicialAppointments.size());

        audit(judicialOfficeAppointmentJsrValidatorInitializer, exchange);

        exchange.getMessage().setBody(filteredJudicialAppointments);
    }

    private void filterInvalidUserProfileRecords(List<JudicialOfficeAppointment> filteredJudicialAppointments,
                                                 List<JudicialUserProfile> invalidJudicialUserProfileRecords, Exchange exchange) {
        if (nonNull(invalidJudicialUserProfileRecords)) {
            invalidJudicialUserProfileRecords.forEach(invalidRecords -> {
                filteredJudicialAppointments.removeIf(filterInvalidUserProfAppointment ->
                        filterInvalidUserProfAppointment.getElinksId().equalsIgnoreCase(invalidRecords.getElinksId()));
            });

            //Auditing JSR skipped rows of user profile for Appointment
            judicialOfficeAppointmentJsrValidatorInitializer.auditJsrExceptions(invalidJudicialUserProfileRecords
                    .stream().map(e -> e.getElinksId()).collect(toList()), ELINKS_ID, exchange);

            log.info("Skipped invalid user profile elinks in Appointments {} & total skipped count {}",
                    invalidJudicialUserProfileRecords
                            .stream().map(e -> e.getElinksId()).collect(joining(",")),
                    invalidJudicialUserProfileRecords.size());
        }
    }
}
