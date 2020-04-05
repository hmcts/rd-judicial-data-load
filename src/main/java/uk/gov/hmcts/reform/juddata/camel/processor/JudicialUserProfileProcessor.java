package uk.gov.hmcts.reform.juddata.camel.processor;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.exception.RouteFailedException;
import uk.gov.hmcts.reform.juddata.camel.validator.JSRValidatorInitializer;

@Slf4j
@Component
public class JudicialUserProfileProcessor implements Processor, DefaultProcessor<JudicialUserProfile> {

    @Autowired
    JSRValidatorInitializer<JudicialUserProfile> judicialUserProfileJSRValidatorInitializer;

    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) {

        List<JudicialUserProfile> judicialUserProfiles;

        if (exchange.getIn().getBody() instanceof List) {
            judicialUserProfiles = (List<JudicialUserProfile>) exchange.getIn().getBody();
        } else {
            JudicialUserProfile judicialUserProfile = (JudicialUserProfile) exchange.getIn().getBody();
            judicialUserProfiles = new ArrayList<>();
            judicialUserProfiles.add(judicialUserProfile);
        }


        log.info("::JudicialUserProfile Records count::" + judicialUserProfiles.size());

        List<JudicialUserProfile> filteredJudicialUserProfiles = validate(judicialUserProfileJSRValidatorInitializer,
                judicialUserProfiles);

        if(judicialUserProfileJSRValidatorInitializer.getConstraintViolations().size() > 5) {
            throw new RouteFailedException("Jsr exception exceeds threshold limit in Judicial User Profile");
        } else {
            //Auditing JSR exceptions in exception table
            audit(judicialUserProfileJSRValidatorInitializer, exchange);
        }

        log.info("::JudicialUserProfile Records invalid for JSR::"
                + (judicialUserProfiles.size() - filteredJudicialUserProfiles.size()));

        exchange.getMessage().setBody(filteredJudicialUserProfiles);
    }
}
