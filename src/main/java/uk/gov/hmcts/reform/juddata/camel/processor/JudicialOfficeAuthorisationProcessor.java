package uk.gov.hmcts.reform.juddata.camel.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.processor.JsrValidationBaseProcessor;
import uk.gov.hmcts.reform.data.ingestion.camel.service.IEmailService;
import uk.gov.hmcts.reform.data.ingestion.camel.service.dto.Email;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.JsrValidatorInitializer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAuthorisation;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.util.EmailTemplate;
import uk.gov.hmcts.reform.juddata.configuration.EmailConfiguration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Predicate;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.MISSING_PER_ID;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.PER_ID;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.LOWER_LEVEL;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.NEW_LOWER_LEVEL;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.LOWER_LEVEL_AUTH;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.DATE_PATTERN;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.CONTENT_TYPE_HTML;

@Slf4j
@Component
public class JudicialOfficeAuthorisationProcessor
    extends JsrValidationBaseProcessor<JudicialOfficeAuthorisation>
    implements ICustomValidationProcessor<JudicialOfficeAuthorisation> {

    @Autowired
    JsrValidatorInitializer<JudicialOfficeAuthorisation> judicialOfficeAuthorisationJsrValidatorInitializer;

    @Autowired
    JudicialUserProfileProcessor judicialUserProfileProcessor;

    @Value("${logging-component-name}")
    private String logComponentName;

    @Autowired
    @Qualifier("springJdbcTemplate")
    protected JdbcTemplate jdbcTemplate;

    @Value("${fetch-lower-levels}")
    String fetchLowerLevels;

    @Autowired
    IEmailService emailService;

    @Autowired
    EmailTemplate emailTemplate;

    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) {

        List<JudicialOfficeAuthorisation> judicialOfficeAuthorisations;

        judicialOfficeAuthorisations = (exchange.getIn().getBody() instanceof List)
            ? (List<JudicialOfficeAuthorisation>) exchange.getIn().getBody()
            : singletonList((JudicialOfficeAuthorisation) exchange.getIn().getBody());

        log.info("{}:: Judicial Authorisation Records count before Validation:: {}", logComponentName,
            judicialOfficeAuthorisations.size());

        List<JudicialOfficeAuthorisation> filteredJudicialAuthorisations =
            validate(judicialOfficeAuthorisationJsrValidatorInitializer,
                judicialOfficeAuthorisations);

        List<JudicialUserProfile> invalidJudicialUserProfileRecords = judicialUserProfileProcessor.getInvalidRecords();

        filterInvalidUserProfileRecords(filteredJudicialAuthorisations,
            invalidJudicialUserProfileRecords, judicialOfficeAuthorisationJsrValidatorInitializer, exchange,
            logComponentName);

        log.info("{}:: Judicial Authorisation Records count after JSR Validation {}:: ", logComponentName,
            filteredJudicialAuthorisations.size());

        audit(judicialOfficeAuthorisationJsrValidatorInitializer, exchange);

        filterAuthorizationsRecordsForForeignKeyViolation(filteredJudicialAuthorisations, exchange);

        if (judicialOfficeAuthorisations.size() != filteredJudicialAuthorisations.size()) {
            setFileStatus(exchange, applicationContext);
        }

        log.info("{}:: Judicial Authorisation Records count after JSR and foreign key Validation {}:: ",
            logComponentName, filteredJudicialAuthorisations.size());

        exchange.getMessage().setBody(filteredJudicialAuthorisations);
    }

    private void filterAuthorizationsRecordsForForeignKeyViolation(List<JudicialOfficeAuthorisation>
                                                                       filteredJudicialAuthorisations,
                                                                   Exchange exchange) {
        log.info("{} : filter Authorizations Records For ForeignKey Violation",logComponentName);

        Predicate<JudicialOfficeAuthorisation> perViolations = c ->
            isFalse(judicialUserProfileProcessor.getValidPerIdInUserProfile().contains(c.getPerId()));

        //remove & audit missing personal e-links id
        removeForeignKeyElements(filteredJudicialAuthorisations, perViolations, PER_ID, exchange,
            judicialOfficeAuthorisationJsrValidatorInitializer, MISSING_PER_ID);

        List<JudicialOfficeAuthorisation> newLowerLevelAuths
                = retrieveNewLowerLevelAuthorisations(filteredJudicialAuthorisations);

        if (!newLowerLevelAuths.isEmpty()) {
            flagNewLowerLevelAuths(newLowerLevelAuths, exchange);
        }
    }

    public List<JudicialOfficeAuthorisation> retrieveNewLowerLevelAuthorisations(List<JudicialOfficeAuthorisation>
                                                            filteredJudicialAuthorisations) {
        log.info("{} : retrieve New Lower Level Authorisations",logComponentName);
        List<String> lowerLevels = jdbcTemplate.queryForList(fetchLowerLevels, String.class);

        Predicate<JudicialOfficeAuthorisation> lowerLevelPredicate =
            juAuth -> !lowerLevels.contains(juAuth.getLowerLevel());

        return filteredJudicialAuthorisations.stream()
                .filter(lowerLevelPredicate)
                .collect(toUnmodifiableList());
    }

    public void flagNewLowerLevelAuths(List<JudicialOfficeAuthorisation> newLowerLevelAuths,
                                       Exchange exchange) {
        log.info("{} : flag New Lower Level Auths",logComponentName);
        List<Pair<String, Long>> pairs = newLowerLevelAuths.stream()
                .map(auth -> Pair.of(auth.getPerId(), auth.getRowId()))
                .collect(toUnmodifiableList());

        judicialOfficeAuthorisationJsrValidatorInitializer
                .auditJsrExceptions(pairs, LOWER_LEVEL, NEW_LOWER_LEVEL, exchange);

        sendEmail(getLowerLevelAuthMailTypeConfig(newLowerLevelAuths));
    }

    public int sendEmail(EmailConfiguration.MailTypeConfig mailConfig) {
        log.info("{} : sendEmail",logComponentName);
        if (mailConfig.isEnabled()) {
            Email email = Email.builder()
                    .contentType(CONTENT_TYPE_HTML)
                    .from(mailConfig.getFrom())
                    .to(mailConfig.getTo())
                    .messageBody(emailTemplate.getEmailBody(mailConfig.getTemplate(), mailConfig.getModel()))
                    .subject(String.format(mailConfig.getSubject(), LocalDate.now()
                            .format(DateTimeFormatter.ofPattern(DATE_PATTERN))))
                    .build();
            return emailService.sendEmail(email);
        }

        return -1;
    }

    private EmailConfiguration.MailTypeConfig getLowerLevelAuthMailTypeConfig(
            List<JudicialOfficeAuthorisation> newLowerLevelAuths) {
        log.info("{} : get Lower Level Auth Mail Type Config",logComponentName);
        return emailTemplate.getMailTypeConfig(getLowerLevelAuthModel(newLowerLevelAuths), LOWER_LEVEL_AUTH);
    }

    private Map<String, Object> getLowerLevelAuthModel(List<JudicialOfficeAuthorisation> newLowerLevelAuths) {
        log.info("{} : get Lower Level Auth Model",logComponentName);
        Map<String, Object> model = new HashMap<>();
        model.put("newLowerLevelAuths", newLowerLevelAuths);
        return model;
    }
}
