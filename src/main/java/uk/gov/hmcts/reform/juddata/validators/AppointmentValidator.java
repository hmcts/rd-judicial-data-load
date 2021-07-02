package uk.gov.hmcts.reform.juddata.validators;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class AppointmentValidator implements ConstraintValidator<Appointment, String> {

    private static final String VALUE1 = "CRTS TRIB - RS Admin User";
    private static final String VALUE2 = "MAGS - AC Admin User";
    private static final String VALUE3 = "Person on a List";
    private static final String VALUE4 = "Unknown";

    private static final Set<String> notAllowedValues = Set.of(VALUE1, VALUE2, VALUE3, VALUE4);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.isBlank(value) || !notAllowedValues.contains(value.trim());
    }
}
