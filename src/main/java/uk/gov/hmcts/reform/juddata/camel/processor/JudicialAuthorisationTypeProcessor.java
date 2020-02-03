package uk.gov.hmcts.reform.juddata.camel.processor;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialAuthorisationType;

@Slf4j
public class JudicialAuthorisationTypeProcessor implements Processor {

    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) {

        List<JudicialAuthorisationType> authorizationTypes = new ArrayList<>();
        List<JudicialAuthorisationType> judicialAuthorizationTypes = (List<JudicialAuthorisationType>) exchange.getIn().getBody();

        log.info("JudicialAuthorizationTypes Records count before validation::" + judicialAuthorizationTypes.size());

        for (JudicialAuthorisationType authorizationType : judicialAuthorizationTypes) {

            JudicialAuthorisationType validAuthorizationType = fetch(authorizationType);
            if (null != validAuthorizationType) {

                authorizationTypes.add(validAuthorizationType);
            } else {

                log.info("Invalid JudicialContractTypes record ");
            }

            exchange.getIn().setBody(authorizationTypes);

        }

        log.info("JudicialAuthorizationTypes Records count After Validation::" + authorizationTypes.size());
    }


    private JudicialAuthorisationType fetch(JudicialAuthorisationType authorizationType) {

        JudicialAuthorisationType authorizationTypeAfterValidation = null;
        if (null != authorizationType.getAuthorisationId()) {
            authorizationTypeAfterValidation = authorizationType;
        }
        return authorizationTypeAfterValidation;
    }
}
