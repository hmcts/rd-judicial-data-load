package uk.gov.hmcts.reform.juddata.camel.util;

import com.fasterxml.jackson.core.type.TypeReference;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.client.IdamClient;
import uk.gov.hmcts.reform.juddata.configuration.TokenConfigProperties;
import uk.gov.hmcts.reform.juddata.exception.JudicialDataLoadException;
import uk.gov.hmcts.reform.juddata.response.OpenIdAccessTokenResponse;

import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class JrdSidamTokenServiceImpl implements JrdSidamTokenService {

    @Autowired
    IdamClient idamClient;

    @Autowired
    TokenConfigProperties props;

    @Value("${logging-component-name}")
    String loggingComponentName;

    @Value("${elastic.search.query}")
    String searchQuery;

    @Value("${elastic.search.recordsPerPage}")
    int recordsPerPage;


    @Override
    public String getBearerToken() throws JudicialDataLoadException {

        byte[] base64UserDetails = Base64.getDecoder().decode(props.getAuthorization());
        Map<String, String> formParams = new HashMap<>();
        formParams.put("grant_type", "password");
        String[] userDetails = new String(base64UserDetails).split(":");
        formParams.put("username", userDetails[0].trim());
        formParams.put("password", userDetails[1].trim());
        formParams.put("client_id", props.getClientId());
        byte[] base64ClientAuth = Base64.getDecoder().decode(props.getClientAuthorization());
        String[] clientAuth = new String(base64ClientAuth).split(":");
        formParams.put("client_secret", clientAuth[1]);
        formParams.put("redirect_uri", props.getRedirectUri());
        formParams.put("scope", "openid profile roles manage-user create-user search-user");


        OpenIdAccessTokenResponse openIdTokenResponse = idamClient.getOpenIdToken(formParams);

        if (openIdTokenResponse == null) {
            throw new JudicialDataLoadException("Idam Service Failed while bearer token generate");
        }
        return openIdTokenResponse.getAccessToken();
    }

    @SuppressWarnings("unchecked")
    public Set<IdamClient.User> getSyncFeed() throws JudicialDataLoadException {
        Map<String, String> formParams = new HashMap<>();
        formParams.put("query",searchQuery);
        log.info("{}:: search elk query {}", loggingComponentName, searchQuery);
        Set<IdamClient.User> judicialUsers = new HashSet<>();
        int totalCount = 0;
        int counter = 0;

        do {
            formParams.put("page", String.valueOf(counter));
            String bearerToken = "Bearer ".concat(getBearerToken());
            Response response = idamClient.getUserFeed(bearerToken, formParams);
            logIdamResponse(response);

            ResponseEntity<Object> responseEntity = JsonFeignResponseUtil.toResponseEntity(response,
                new TypeReference<Set<IdamClient.User>>() {
                });

            if (response.status() == 200) {

                Set<IdamClient.User> users = (Set<IdamClient.User>) responseEntity.getBody();
                judicialUsers.addAll(users);

                try {
                    List<String> headerCount = responseEntity.getHeaders().get("X-Total-Count");
                    if (headerCount != null && !headerCount.isEmpty()
                        && !headerCount.get(0).isEmpty()) {

                        totalCount = Integer.parseInt(headerCount.get(0));
                        log.info("{}:: Header Records count from Idam :: " + totalCount, loggingComponentName);
                    }

                } catch (Exception ex) {
                    //There is No header.
                    log.error("{}:: X-Total-Count header not return Idam Search Service::{}", loggingComponentName, ex);
                    throw new JudicialDataLoadException("Idam search query failure");
                }
            } else {
                log.error("{}:: Idam Search Service Failed :: ", loggingComponentName);
                throw new JudicialDataLoadException("Idam search query failure with response status "
                    + response.status());
            }
            counter++;

        } while (totalCount > 0 && recordsPerPage * counter < totalCount);
        return judicialUsers;
    }

    private void logIdamResponse(Response response) {
        log.info("Logging Response from IDAM");
        if (response != null) {
            log.info("{}:: Response code from idamClient.getUserFeed {}", loggingComponentName, response.status());
            if (response.status() != 200 && response.body() != null) {
                log.info("{}:: Response body from Idam Client ::{}", loggingComponentName, response.status());
            }
        }
    }
}
