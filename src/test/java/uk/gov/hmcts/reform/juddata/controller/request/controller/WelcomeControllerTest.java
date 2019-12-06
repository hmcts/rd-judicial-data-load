package uk.gov.hmcts.reform.juddata.controller.request.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.juddata.controller.WelcomeController;

public class WelcomeControllerTest {

    private final WelcomeController welcomeController = new WelcomeController();

    @Test
    public void should_return_welcome_response() {

        ResponseEntity<String> responseEntity = welcomeController.welcome();
        String expectedMessage = "Welcome to the Judicial API";
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertThat(
            responseEntity.getBody(),
            containsString(expectedMessage)
        );
    }
}
