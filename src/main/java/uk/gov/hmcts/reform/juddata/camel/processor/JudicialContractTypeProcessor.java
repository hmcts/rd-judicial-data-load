package uk.gov.hmcts.reform.juddata.camel.processor;

import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import uk.gov.hmcts.reform.juddata.camel.binder.JudicialContractType;

@Slf4j
@Component
public class JudicialContractTypeProcessor implements Processor {


    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) throws Exception {

        List<JudicialContractType> contractTypes = new ArrayList<>();
        List<JudicialContractType> judicialContractTypes = (List<JudicialContractType>) exchange.getIn().getBody();

        for (JudicialContractType contractType : judicialContractTypes) {

            JudicialContractType validUserRole = fetch(contractType);
            if (nonNull(validUserRole)) {

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
        if (nonNull(contractType.getContractTypeId())) {
            contractTypeAfterValidation = contractType;
        }
        return contractTypeAfterValidation;
    }
}
