package uk.gov.hmcts.reform.juddata.predicate;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.springframework.stereotype.Component;

@Component
public class BooleanPredicate implements Predicate {

    private boolean value;

    @Override
    public boolean matches(Exchange exchange) {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}