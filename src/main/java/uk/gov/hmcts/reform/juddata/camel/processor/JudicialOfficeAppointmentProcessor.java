package uk.gov.hmcts.reform.juddata.camel.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.processor.JsrValidationBaseProcessor;
import uk.gov.hmcts.reform.data.ingestion.camel.service.IEmailService;
import uk.gov.hmcts.reform.data.ingestion.camel.service.dto.Email;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.JsrValidatorInitializer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.util.EmailTemplate;
import uk.gov.hmcts.reform.juddata.configuration.EmailConfiguration;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.BASE_LOCATION;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.MISSING_BASE_LOCATION;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.MISSING_PER_ID;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.MISSING_LOCATION;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.REGION;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.BASE_LOCATION_ID;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.PER_ID;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.LOCATION_ID;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.CONTENT_TYPE_HTML;


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

    @Autowired
    EmailConfiguration emailConfiguration;

    @Autowired
    IEmailService emailService;

    @Autowired
    EmailTemplate emailTemplate;
    
    private final Map<String, String> emailConfigMapping = Map.of(LOCATION_ID, REGION,
            BASE_LOCATION_ID, BASE_LOCATION);

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
        log.info("{}:: Before filter Appointments Records For Foreign Key Violation {}:: ",
                logComponentName, filteredJudicialAppointments.size());

        Predicate<JudicialOfficeAppointment> perViolations = c ->
            isFalse(judicialUserProfileProcessor.getValidPerIdInUserProfile().contains(c.getPerId()));

        //remove & audit missing personal per id
        removeForeignKeyElements(filteredJudicialAppointments, perViolations, PER_ID, exchange,
            judicialOfficeAppointmentJsrValidatorInitializer, MISSING_PER_ID);

        //remove & audit missing Locations
        List<String> locations = jdbcTemplate.queryForList(fetchLocations, String.class);
        Predicate<JudicialOfficeAppointment> locationsViolations = c -> isFalse(locations.contains(c.getRegionId()))
            && isFalse("0".equalsIgnoreCase(c.getRegionId()));
        removeForeignKeyElements(filteredJudicialAppointments, locationsViolations, LOCATION_ID, exchange,
            judicialOfficeAppointmentJsrValidatorInitializer, MISSING_LOCATION);

        //remove & audit missing BaseLocations
        List<String> baseLocations = jdbcTemplate.queryForList(fetchBaseLocations, String.class);
        Predicate<JudicialOfficeAppointment> baseLocationsViolations = c -> isFalse(baseLocations.contains(
            c.getBaseLocationId())) && isFalse(c.getBaseLocationId().equalsIgnoreCase("0"));
        removeForeignKeyElements(filteredJudicialAppointments, baseLocationsViolations, BASE_LOCATION_ID, exchange,
            judicialOfficeAppointmentJsrValidatorInitializer, MISSING_BASE_LOCATION);

        log.info("{}:: After filter Appointments Records For Foreign Key Violation {}:: ",
                logComponentName, filteredJudicialAppointments.size());
    }

    @Override
    public int sendEmail(Set<JudicialOfficeAppointment> data, String type, Object... params) {
        log.info("{} : send Email",logComponentName);
        EmailConfiguration.MailTypeConfig config = emailConfiguration.getMailTypes()
                .get(emailConfigMapping.get(type));
        if (config != null && config.isEnabled()) {
            Email email = Email.builder()
                    .contentType(CONTENT_TYPE_HTML)
                    .from(config.getFrom())
                    .to(config.getTo())
                    .subject(String.format(config.getSubject(), params))
                    .messageBody(emailTemplate.getEmailBody(config.getTemplate(), Map.of("appointments", data)))
                    .build();
            return emailService.sendEmail(email);
        }
        return -1;
    }

}
