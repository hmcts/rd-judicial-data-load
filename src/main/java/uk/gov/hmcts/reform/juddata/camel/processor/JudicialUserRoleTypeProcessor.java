package uk.gov.hmcts.reform.juddata.camel.processor;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.MISSING_PER_ID;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.PER_ID;

import java.util.List;
import java.util.function.Predicate;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.processor.JsrValidationBaseProcessor;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.JsrValidatorInitializer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserRoleType;

@Component
@Slf4j
public class JudicialUserRoleTypeProcessor
        extends JsrValidationBaseProcessor<JudicialUserRoleType>
        implements ICustomValidationProcessor<JudicialUserRoleType> {

    @Autowired
    JsrValidatorInitializer<JudicialUserRoleType> judicialUserRoleTypeJsrValidatorInitializer;

    @Autowired
    JudicialUserProfileProcessor judicialUserProfileProcessor;


    @Value("${logging-component-name}")
    private String logComponentName;

    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) {

        List<JudicialUserRoleType> judicialUserRoleTypes;

        judicialUserRoleTypes = (exchange.getIn().getBody() instanceof List)
                ? (List<JudicialUserRoleType>) exchange.getIn().getBody()
                : singletonList((JudicialUserRoleType) exchange.getIn().getBody());

        log.info("{}:: Role type Records count before Validation {}::", logComponentName, judicialUserRoleTypes.size());

        List<JudicialUserRoleType> filteredJudicialRoleTypes = validate(judicialUserRoleTypeJsrValidatorInitializer,
                judicialUserRoleTypes);

        List<JudicialUserProfile> invalidJudicialUserProfileRecords = judicialUserProfileProcessor.getInvalidRecords();

        filterInvalidUserProfileRecords(filteredJudicialRoleTypes,
                invalidJudicialUserProfileRecords, judicialUserRoleTypeJsrValidatorInitializer, exchange,
                logComponentName);


        log.info("{}:: Judicial Role type Records count after Validation {}::", logComponentName,
                filteredJudicialRoleTypes.size());

        audit(judicialUserRoleTypeJsrValidatorInitializer, exchange);

        filterAuthorizationsRecordsForForeignKeyViolation(filteredJudicialRoleTypes, exchange);

        if (judicialUserRoleTypes.size() != filteredJudicialRoleTypes.size()) {
            setFileStatus(exchange, applicationContext);
        }

        log.info("{}:: Judicial Role type Records after JSR and foreign key Validation {}:: ",
                logComponentName, filteredJudicialRoleTypes.size());
        exchange.getMessage().setBody(filteredJudicialRoleTypes);
    }

    private void filterAuthorizationsRecordsForForeignKeyViolation(List<JudicialUserRoleType> filteredJudicialRoleTypes,
                                                                   Exchange exchange) {
        log.info("{} : starting filter Authorizations Records For Foreign Key Violation ", logComponentName);
        Predicate<JudicialUserRoleType> perViolations = c ->
                isFalse(judicialUserProfileProcessor.getValidPerIdInUserProfile().contains(c.getPerId()));

        //remove & audit missing personal e-links id
        removeForeignKeyElements(filteredJudicialRoleTypes, perViolations, PER_ID, exchange,
                judicialUserRoleTypeJsrValidatorInitializer, MISSING_PER_ID);
    }
}
