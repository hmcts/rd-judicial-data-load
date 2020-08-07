package uk.gov.hmcts.reform.juddata.camel.processor;

import static java.util.Collections.singletonList;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.processor.JsrValidationBaseProcessor;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.JsrValidatorInitializer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialContractType;

@Slf4j
@Component
public class JudicialContractTypeProcessor extends JsrValidationBaseProcessor<JudicialContractType> {

    @Autowired
    JsrValidatorInitializer<JudicialContractType> judicialContractTypeJsrValidatorInitializer;

    @Value("${logging-component-name}")
    private String logComponentName;

    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) throws Exception {

        List<JudicialContractType> judicialContractTypes;

        judicialContractTypes = (exchange.getIn().getBody() instanceof List)
            ? (List<JudicialContractType>) exchange.getIn().getBody()
            : singletonList((JudicialContractType) exchange.getIn().getBody());

        log.info("{}:: Contract type Records count before Validation {}:: ", logComponentName,
            judicialContractTypes.size());
        List<JudicialContractType> filteredJudicialContractTypes = validate(judicialContractTypeJsrValidatorInitializer,
            judicialContractTypes);
        log.info("{}:: Contract type Records count after Validation {}::", logComponentName,
            filteredJudicialContractTypes.size());
        audit(judicialContractTypeJsrValidatorInitializer, exchange);

        exchange.getMessage().setBody(filteredJudicialContractTypes);
    }
}
