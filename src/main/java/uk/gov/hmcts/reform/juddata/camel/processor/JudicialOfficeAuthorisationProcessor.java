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
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAuthorisation;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;

@Slf4j
@Component
public class JudicialOfficeAuthorisationProcessor extends JsrValidationBaseProcessor<JudicialOfficeAuthorisation> {

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

        filterInvalidUserProfileRecords(filteredJudicialAuthorisations, invalidJudicialUserProfileRecords, exchange);

        log.info("{}:: Judicial Authorisation Records count after Validation {}:: ", logComponentName,
                filteredJudicialAuthorisations.size());

        audit(judicialOfficeAuthorisationJsrValidatorInitializer, exchange);

        exchange.getMessage().setBody(filteredJudicialAuthorisations);
    }

    private void filterInvalidUserProfileRecords(List<JudicialOfficeAuthorisation> filteredJudicialOfficeAuthorisations,
                                                 List<JudicialUserProfile> invalidJudicialUserProfileRecords,
                                                 Exchange exchange) {
        if (nonNull(invalidJudicialUserProfileRecords)) {

            List<String> invalidElinks = new ArrayList<>();

            invalidJudicialUserProfileRecords.forEach(invalidRecords -> {
                //Remove invalid Authorisations for user profile and add to invalidElinks List
                if (filteredJudicialOfficeAuthorisations.removeIf(filterInvalidUserProfAuthorisations ->
                        filterInvalidUserProfAuthorisations.getElinksId()
                                .equalsIgnoreCase(invalidRecords.getElinksId()))) {
                    invalidElinks.add(invalidRecords.getElinksId());
                }
            });

            //Auditing JSR skipped rows of user profile for Authorisation
            judicialOfficeAuthorisationJsrValidatorInitializer.auditJsrExceptions(invalidElinks, ELINKS_ID, exchange);

            log.info("{}:: Skipped invalid user profile elinks in Authorisation {} & total skipped count {}",
                    logComponentName,
                    invalidElinks.stream().collect(joining(",")),
                    invalidElinks.size());
        }
    }
}

