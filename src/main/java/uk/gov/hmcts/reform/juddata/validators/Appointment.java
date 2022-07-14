package uk.gov.hmcts.reform.juddata.validators;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target({METHOD, FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AppointmentValidator.class)
public @interface Appointment {
    String message() default "{appointment value not allowed}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
