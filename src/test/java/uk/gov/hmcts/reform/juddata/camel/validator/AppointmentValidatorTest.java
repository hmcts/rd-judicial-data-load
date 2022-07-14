package uk.gov.hmcts.reform.juddata.camel.validator;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.juddata.validators.AppointmentValidator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class AppointmentValidatorTest {

    AppointmentValidator appointmentValidator = new AppointmentValidator();

    @Test
    void should_return_true_for_valid_values() {
        assertThat(appointmentValidator.isValid(null, null), is(true));
        assertThat(appointmentValidator.isValid("", null), is(true));
        assertThat(appointmentValidator.isValid("Circuit Judge", null), is(true));
        assertThat(appointmentValidator.isValid("Tribunal Judge", null), is(true));
        assertThat(appointmentValidator.isValid("Magistrate", null), is(true));
    }

    @Test
    void should_return_false_for_invalid_values() {
        assertThat(appointmentValidator.isValid("CRTS TRIB - RS Admin User", null), is(false));
        assertThat(appointmentValidator.isValid("MAGS - AC Admin User", null), is(false));
        assertThat(appointmentValidator.isValid("Person on a List", null), is(false));
        assertThat(appointmentValidator.isValid("Unknown", null), is(false));
        assertThat(appointmentValidator.isValid("Area Coroner", null), is(false));
        assertThat(appointmentValidator.isValid("Senior Coroner", null), is(false));
        assertThat(appointmentValidator.isValid("Initial Automated Record", null), is(false));
    }
}