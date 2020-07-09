package uk.gov.hmcts.reform.juddata.camel.processor;

import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.ELINKS_ID;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.processor.JsrValidationBaseProcessor;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.JsrValidatorInitializer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;


@Slf4j
@Component
public class JudicialOfficeAppointmentProcessor extends JsrValidationBaseProcessor<JudicialOfficeAppointment> {

    @Autowired
    JsrValidatorInitializer<JudicialOfficeAppointment> judicialOfficeAppointmentJsrValidatorInitializer;

    @Autowired
    JudicialUserProfileProcessor judicialUserProfileProcessor;

    @Value("${logging-component-name}")
    private String logComponentName;

    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) {

        List<JudicialOfficeAppointment> judicialOfficeAppointments;

        judicialOfficeAppointments = (exchange.getIn().getBody() instanceof List)
            ? (List<JudicialOfficeAppointment>) exchange.getIn().getBody()
            : singletonList((JudicialOfficeAppointment) exchange.getIn().getBody());

        log.info(" {} Judicial Appointment Records count before Validation {}::", logComponentName,
            judicialOfficeAppointments.size());

        List<JudicialOfficeAppointment> filteredJudicialAppointments
            = validate(judicialOfficeAppointmentJsrValidatorInitializer, judicialOfficeAppointments);


        List<JudicialUserProfile> invalidJudicialUserProfileRecords = judicialUserProfileProcessor.getInvalidRecords();

        filterInvalidUserProfileRecords(filteredJudicialAppointments, invalidJudicialUserProfileRecords, exchange);

        log.info("{}:: Judicial Appointment Records count after Validation {}:: ", logComponentName,
            filteredJudicialAppointments.size());

        audit(judicialOfficeAppointmentJsrValidatorInitializer, exchange);

        exchange.getMessage().setBody(filteredJudicialAppointments);
    }

    private void filterInvalidUserProfileRecords(List<JudicialOfficeAppointment> filteredJudicialAppointments,
                                                 List<JudicialUserProfile> invalidJudicialUserProfileRecords,
                                                 Exchange exchange) {
        if (nonNull(invalidJudicialUserProfileRecords)) {

            List<String> invalidElinks = new ArrayList<>();

            invalidJudicialUserProfileRecords.forEach(invalidRecords -> {
                //Remove invalid appointment for user profile and add to invalidElinks List
                if (filteredJudicialAppointments.removeIf(filterInvalidUserProfAppointment ->
                    filterInvalidUserProfAppointment.getElinksId().equalsIgnoreCase(invalidRecords.getElinksId()))) {
                    invalidElinks.add(invalidRecords.getElinksId());
                }
            });

            //Auditing JSR skipped rows of user profile for Appointment
            judicialOfficeAppointmentJsrValidatorInitializer.auditJsrExceptions(invalidElinks, ELINKS_ID, exchange);

            log.info("{}:: Skipped invalid user profile elinks in Appointments {} & total skipped count {}",
                logComponentName,
                invalidElinks.stream().collect(joining(",")),
                invalidElinks.size());
        }
    }
}
