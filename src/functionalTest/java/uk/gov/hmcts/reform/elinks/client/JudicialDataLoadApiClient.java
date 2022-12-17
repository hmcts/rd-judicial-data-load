package uk.gov.hmcts.reform.elinks.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.rest.SerenityRest;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.hmcts.reform.elinks.idam.IdamOpenIdClient;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class JudicialDataLoadApiClient {

    @Value("${loggingComponentName}")
    private String loggingComponentName;

    private static final ObjectMapper mapper = new ObjectMapper();
    private final String judicialApiUrl;
    private static final String SERVICE_HEADER = "ServiceAuthorization";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final IdamOpenIdClient idamOpenIdClient;
    private static final String IDAM_ELASTIC_SEARCH_URI = "/refdata/jinternal/elink/idam/elastic/search";


    public JudicialDataLoadApiClient(String judicialApiUrl,
                             IdamOpenIdClient idamOpenIdClient) {
        this.judicialApiUrl = judicialApiUrl;
        this.idamOpenIdClient = idamOpenIdClient;
    }

    public String getWelcomePage() {
        return withUnauthenticatedRequest()
                .get("/")
                .then()
                .statusCode(OK.value())
                .and()
                .extract()
                .response()
                .body()
                .asString();
    }

    public String getHealthPage() {
        return withUnauthenticatedRequest()
                .get("/health")
                .then()
                .statusCode(OK.value())
                .and()
                .extract()
                .response()
                .body()
                .asString();
    }

    public RequestSpecification getMultipleAuthHeaders() {
        String userToken = idamOpenIdClient.getcwdAdminOpenIdToken("jrd-admin");
        return SerenityRest.with()
                .relaxedHTTPSValidation()
                .baseUri(judicialApiUrl)
                .header("Content-Type", APPLICATION_JSON_VALUE)
                .header("Accepts", APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION_HEADER, "Bearer " + userToken)
                .header("page_size", 1)
                .header("page_number", 1);
    }

    private RequestSpecification withUnauthenticatedRequest() {
        return SerenityRest.given()
                .relaxedHTTPSValidation()
                .baseUri(judicialApiUrl)
                .header("Accepts", APPLICATION_JSON_VALUE);
    }

    public Response getIdamElasticSearch() {

        Response response = withUnauthenticatedRequest()
                .get(IDAM_ELASTIC_SEARCH_URI)
                .andReturn();

        return response;
    }


}

