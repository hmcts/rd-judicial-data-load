package uk.gov.hmcts.reform.juddata.camel.processor;

import static org.apache.camel.Exchange.EXCEPTION_CAUGHT;
import static org.apache.camel.Exchange.FAILURE_ROUTE_ID;
import static org.apache.camel.Exchange.ROUTE_STOP;

import java.lang.reflect.Field;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.model.language.SimpleExpression;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorFactoryImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component
@Slf4j
public class ExceptionProcessor implements Processor {

    @Resource(name = "myConstraintValidatorFactory")
    ConstraintValidatorFactoryImpl constraintValidatorFactory;

    @Qualifier("txManager1")
    PlatformTransactionManager platformTransactionManager;

    @Override
    public void process(Exchange exchange) {
        Exception exception = (Exception) exchange.getProperty(EXCEPTION_CAUGHT);
        if(exception instanceof BeanValidationException) {
            jsrException(exchange, (BeanValidationException) exception);
        }
        log.error("::::exception in route for data processing::::" + exception);
    }


    private void jsrException(Exchange exchange, BeanValidationException ex) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("Jsr exception logs");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        String failedRoute = (String) exchange.getProperty(FAILURE_ROUTE_ID);
        Set<ConstraintViolation<Object>> constraintViolations = ex.getConstraintViolations();

        if(constraintViolations.iterator().hasNext()) {
            Object bean = ex.getConstraintViolations().iterator().next()
                    .getRootBean();
            Class objectClass = bean.getClass();
            for (Field field : objectClass.getDeclaredFields()) {
                DataField dataField = AnnotationUtils.findAnnotation(field,
                        DataField.class);
                if (dataField.pos() == 1) {
                    System.out.println(field.toString());
                }
            }
        }
        //exchange.getUnitOfWork().getRouteContext().getFrom().getUri();
    }

}
