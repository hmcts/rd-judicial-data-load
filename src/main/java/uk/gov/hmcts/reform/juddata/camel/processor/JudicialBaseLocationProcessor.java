package uk.gov.hmcts.reform.juddata.camel.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.processor.JsrValidationBaseProcessor;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.JsrValidatorInitializer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialBaseLocationType;

import java.util.List;

import static java.util.Collections.singletonList;

@Slf4j
@Component
public class JudicialBaseLocationProcessor extends JsrValidationBaseProcessor<JudicialBaseLocationType> {

    @Autowired
    JsrValidatorInitializer<JudicialBaseLocationType> judicialBaseLocationTypeJsrValidatorInitializer;

    @Value("${logging-component-name}")
    private String logComponentName;

    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) throws Exception {

        List<JudicialBaseLocationType> locationsRecords;

        locationsRecords = (exchange.getIn().getBody() instanceof List)
                ? (List<JudicialBaseLocationType>) exchange.getIn().getBody()
                : singletonList((JudicialBaseLocationType) exchange.getIn().getBody());

        log.info("{}:: Base Location Records count before Validation {}::", logComponentName, locationsRecords.size());
        List<JudicialBaseLocationType> filteredBaseLocationTypes
            = validate(judicialBaseLocationTypeJsrValidatorInitializer, locationsRecords);

        log.info("{}:: Base Location Records count after Validation {}:: ", logComponentName,
            filteredBaseLocationTypes.size());
        audit(judicialBaseLocationTypeJsrValidatorInitializer, exchange);

        exchange.getMessage().setBody(filteredBaseLocationTypes);
    }
}
