package uk.gov.hmcts.reform.juddata.camel.processor;

import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    JudicialUserProfileProcessor judicialUserProfileProcessor;

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

        //Skip elinks records which are invalid in user profile due to JSR
        filteredJudicialAppointments = filterValidAppointmentsForUserProfile(filteredJudicialAppointments);

        audit(judicialOfficeAppointmentJsrValidatorInitializer, exchange);

        exchange.getMessage().setBody(filteredJudicialAppointments);
    }

    /**
     * Filters valid user profile elinks id records and ignore invalid user profile elinks record with JSR errors.
     *
     * @param filteredJudicialAppointments List
     * @return List JudicialOfficeAppointment
     */
    private List<JudicialOfficeAppointment> filterValidAppointmentsForUserProfile(List<JudicialOfficeAppointment> filteredJudicialAppointments) {
        if (nonNull(judicialUserProfileProcessor.getFilteredUserProfileKeys())) {
            filteredJudicialAppointments = filteredJudicialAppointments.stream().filter(appointments ->
                    judicialUserProfileProcessor.getFilteredUserProfileKeys().stream()
                            .noneMatch(skippedIdParent -> appointments.getElinksId().equalsIgnoreCase(skippedIdParent)))
                    .collect(Collectors.toList());

            log.info("Skipped invalid user profile elinks in Appointments {} & total skipped count {}",
                    judicialUserProfileProcessor.getFilteredUserProfileKeys()
                            .stream().collect(joining(",")),
                    judicialUserProfileProcessor.getFilteredUserProfileKeys().size());
        }
        return filteredJudicialAppointments;
    }
}
