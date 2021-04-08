package uk.gov.hmcts.reform.juddata;

import io.restassured.RestAssured;
import net.serenitybdd.rest.SerenityRest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class SmokeTest {

    private final String targetInstance =
        StringUtils.defaultIfBlank(
            System.getenv("TEST_URL"),
            "http://localhost:8094"
        );

    @Test
    void should_prove_app_is_running_and_healthy() {

        RestAssured.baseURI = targetInstance;
        RestAssured.useRelaxedHTTPSValidation();

        String response = SerenityRest
            .when()
            .get("/health")
            .then()
            .statusCode(HttpStatus.OK.value())
            .and()
            .extract().body().asString();

        assertThat(response)
            .contains("UP");
    }
}
