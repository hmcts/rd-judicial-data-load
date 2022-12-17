package uk.gov.hmcts.reform.elinks.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class IdamOpenIdTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonCreator
    public IdamOpenIdTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
