package uk.gov.hmcts.reform.juddata.camel.processor;

import java.util.List;

import org.apache.camel.Exchange;
import uk.gov.hmcts.reform.juddata.camel.validator.JsrValidatorInitializer;

public interface DefaultProcessor<T> {

    default List<T> validate(JsrValidatorInitializer<T> jsrValidatorInitializer, List<T> list) {
        return jsrValidatorInitializer.validate(list);
    }

    default void audit(JsrValidatorInitializer<T> jsrValidatorInitializer, Exchange exchange) {
        jsrValidatorInitializer.auditJsrExceptions(exchange);
    }
}
