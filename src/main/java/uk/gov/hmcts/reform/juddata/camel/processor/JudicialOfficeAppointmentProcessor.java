package uk.gov.hmcts.reform.juddata.camel.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.processor.JsrValidationBaseProcessor;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.JsrValidatorInitializer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;

import java.util.List;
import java.util.function.Predicate;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.MISSING_BASE_LOCATION;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.MISSING_PER_ID;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.MISSING_LOCATION;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.BASE_LOCATION_ID;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.PER_ID;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.LOCATION_ID;

@Slf4j
@Component
public class JudicialOfficeAppointmentProcessor
    extends JsrValidationBaseProcessor<JudicialOfficeAppointment>
    implements ICustomValidationProcessor<JudicialOfficeAppointment> {

    @Autowired
    JsrValidatorInitializer<JudicialOfficeAppointment> judicialOfficeAppointmentJsrValidatorInitializer;

    @Autowired
    JudicialUserProfileProcessor judicialUserProfileProcessor;

    @Value("${logging-component-name}")
    private String logComponentName;

    @Value("${fetch-region-id}")
    String fetchLocations;

    @Value("${fetch-base-location-id}")
    String fetchBaseLocations;

    @Autowired
    @Qualifier("springJdbcTemplate")
    protected JdbcTemplate jdbcTemplate;

    @Value("${fetch-personal-per-id}")
    String loadPerId;

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

        filterInvalidUserProfileRecords(filteredJudicialAppointments,
            invalidJudicialUserProfileRecords, judicialOfficeAppointmentJsrValidatorInitializer, exchange,
            logComponentName);

        log.info("{}:: Judicial Appointment Records count after JSR Validation {}:: ", logComponentName,
            filteredJudicialAppointments.size());

        filterAppointmentsRecordsForForeignKeyViolation(filteredJudicialAppointments, exchange);

        audit(judicialOfficeAppointmentJsrValidatorInitializer, exchange);

        if (judicialOfficeAppointments.size() != filteredJudicialAppointments.size()) {
            setFileStatus(exchange, applicationContext);
        }

        log.info("{}:: Judicial Appointment Records count  after JSR and foreign key Validation {}:: ",
            logComponentName, filteredJudicialAppointments.size());

        exchange.getMessage().setBody(filteredJudicialAppointments);
    }


    private void filterAppointmentsRecordsForForeignKeyViolation(List<JudicialOfficeAppointment>
                                                                     filteredJudicialAppointments,
                                                                 Exchange exchange) {

        Predicate<JudicialOfficeAppointment> perViolations = c ->
            isFalse(judicialUserProfileProcessor.getValidPerIdInUserProfile().contains(c.getPerId()));

        //remove & audit missing personal per id
        removeForeignKeyElements(filteredJudicialAppointments, perViolations, PER_ID, exchange,
            judicialOfficeAppointmentJsrValidatorInitializer, MISSING_PER_ID);

        //remove & audit missing Locations
        List<String> locations = jdbcTemplate.queryForList(fetchLocations, String.class);
        Predicate<JudicialOfficeAppointment> locationsViolations = c -> isFalse(locations.contains(c.getRegionId()))
            && isFalse(c.getRegionId().equalsIgnoreCase("0"));
        removeForeignKeyElements(filteredJudicialAppointments, locationsViolations, LOCATION_ID, exchange,
            judicialOfficeAppointmentJsrValidatorInitializer, MISSING_LOCATION);

        //remove & audit missing BaseLocations
        List<String> baseLocations = jdbcTemplate.queryForList(fetchBaseLocations, String.class);
        Predicate<JudicialOfficeAppointment> baseLocationsViolations = c -> isFalse(baseLocations.contains(
            c.getBaseLocationId())) && isFalse(c.getBaseLocationId().equalsIgnoreCase("0"));
        removeForeignKeyElements(filteredJudicialAppointments, baseLocationsViolations, BASE_LOCATION_ID, exchange,
            judicialOfficeAppointmentJsrValidatorInitializer, MISSING_BASE_LOCATION);
    }
}
