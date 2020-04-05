package uk.gov.hmcts.reform.juddata.camel.processor;

import java.util.List;

import org.apache.camel.Exchange;
import uk.gov.hmcts.reform.juddata.camel.validator.JSRValidatorInitializer;

public interface DefaultProcessor<T> {

    default List<T> validate(JSRValidatorInitializer<T> jsrValidatorInitializer, List<T> list) {
        return jsrValidatorInitializer.validate(list);
    }

    default void audit(JSRValidatorInitializer<T> jsrValidatorInitializer, Exchange exchange) {
        jsrValidatorInitializer.auditJsrExceptions(exchange);
    }
}
