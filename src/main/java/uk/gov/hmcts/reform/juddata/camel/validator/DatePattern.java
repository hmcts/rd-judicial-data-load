package uk.gov.hmcts.reform.juddata.camel.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({METHOD, FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DatePatternValidator.class)
public @interface DatePattern {

    String message() default "{date pattern should be yyyy-MM-dd hh:mm:ss}";
    String isNullAllowed();
    String regex();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
