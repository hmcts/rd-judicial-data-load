package uk.gov.hmcts.reform.juddata.camel.processor;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialContractType;

@Slf4j
public class JudicialContractTypeProcessor implements Processor {

    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) throws Exception {

        List<JudicialContractType> contractTypes = new ArrayList<>();
        List<JudicialContractType> judicialContractTypes = (List<JudicialContractType>) exchange.getIn().getBody();

        log.info("JudicialContractTypes Records count before validation::" + judicialContractTypes.size());

        for (JudicialContractType contractType : judicialContractTypes) {

            JudicialContractType validUserRole = fetch(contractType);
            if (null != validUserRole) {

                contractTypes.add(validUserRole);
            } else {

                log.info("Invalid JudicialContractTypes record ");
            }

            exchange.getIn().setBody(contractTypes);

        }

        log.info("JudicialContractTypes Records count After Validation::" + contractTypes.size());
    }


    private JudicialContractType fetch(JudicialContractType contractType) {

        JudicialContractType contractTypeAfterValidation = null;
        if (null != contractType.getContractTypeId()) {
            contractTypeAfterValidation = contractType;
        }
        return contractTypeAfterValidation;
    }
}
