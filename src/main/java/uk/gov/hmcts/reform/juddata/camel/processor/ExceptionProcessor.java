package uk.gov.hmcts.reform.juddata.camel.processor;

import static org.apache.camel.Exchange.EXCEPTION_CAUGHT;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
@Slf4j
public class ExceptionProcessor implements Processor {

    @Resource(name = "myConstraintValidatorFactory")
    ConstraintValidatorFactoryImpl constraintValidatorFactory;

    @Qualifier("txManager1")
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    CamelContext camelContext;

    @Override
    public void process(Exchange exchange) throws Exception {
        Exception exception = (Exception) exchange.getProperty(EXCEPTION_CAUGHT);
        log.error("::::exception in route for data processing::::" + exception);
    }

}
