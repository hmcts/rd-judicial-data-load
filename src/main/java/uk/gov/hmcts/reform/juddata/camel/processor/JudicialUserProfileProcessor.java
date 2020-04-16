package uk.gov.hmcts.reform.juddata.camel.processor;

import static java.util.Collections.singletonList;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.validator.JsrValidatorInitializer;

@Slf4j
@Component
public class JudicialUserProfileProcessor extends JsrValidationBaseProcessor<JudicialUserProfile> {

    @Autowired
    JsrValidatorInitializer<JudicialUserProfile> judicialUserProfileJsrValidatorInitializer;


    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) {

        List<JudicialUserProfile> judicialUserProfiles;

        judicialUserProfiles = (exchange.getIn().getBody() instanceof List)
                ? (List<JudicialUserProfile>) exchange.getIn().getBody()
                : singletonList((JudicialUserProfile) exchange.getIn().getBody());

        log.info("Judicial User Profile Records count before Validation:: " + judicialUserProfiles.size());

        List<JudicialUserProfile> filteredJudicialUserProfiles = validate(judicialUserProfileJsrValidatorInitializer,
                judicialUserProfiles);

        log.info("Judicial User Profile Records count after Validation:: " + filteredJudicialUserProfiles.size());

        audit(judicialUserProfileJsrValidatorInitializer, exchange);

        exchange.getMessage().setBody(filteredJudicialUserProfiles);
    }
}
