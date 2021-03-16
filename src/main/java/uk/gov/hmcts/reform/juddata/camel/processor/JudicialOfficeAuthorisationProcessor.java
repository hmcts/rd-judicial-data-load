package uk.gov.hmcts.reform.juddata.camel.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.processor.JsrValidationBaseProcessor;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.JsrValidatorInitializer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAuthorisation;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;

import java.util.List;
import java.util.function.Predicate;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.MISSING_ELINKS;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.ELINKS_ID;

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

        Predicate<JudicialOfficeAuthorisation> elinksViolations = c ->
            isFalse(judicialUserProfileProcessor.getValidElinksInUserProfile().contains(c.getElinksId()));

        //remove & audit missing personal e-links id
        removeForeignKeyElements(filteredJudicialAuthorisations, elinksViolations, ELINKS_ID, exchange,
            judicialOfficeAuthorisationJsrValidatorInitializer, MISSING_ELINKS);
    }

}
