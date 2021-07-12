package uk.gov.hmcts.reform.juddata.camel.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import feign.Response;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.juddata.client.IdamClient;
import uk.gov.hmcts.reform.juddata.configuration.TokenConfigProperties;
import uk.gov.hmcts.reform.juddata.exception.JudicialDataLoadException;
import uk.gov.hmcts.reform.juddata.response.OpenIdAccessTokenResponse;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.invokeMethod;

@ExtendWith(MockitoExtension.class)
class JrdSidamTokenServiceImplTest {

    private final IdamClient idamClientMock = mock(IdamClient.class);
    private final TokenConfigProperties tokenConfigProperties = new TokenConfigProperties();
    private final OpenIdAccessTokenResponse openIdTokenResponseMock = mock(OpenIdAccessTokenResponse.class);
    @InjectMocks
    private JrdSidamTokenServiceImpl jrdSidamTokenService;

    public static final String CLIENT_AUTHORIZATION =
        "eyjfddsfsdfsdfdj03903.dffkljfke932rjf032j02f3--fskfljdskls-fdkldskll";


    @BeforeEach
    void setUP() {
        final String clientId = "234342332";
        final String redirectUri = "http://idam-api.aat.platform.hmcts.net";
        final String authorization = "c2hyZWVkaGFyLmxvbXRlQGhtY3RzLm5ldDpITUNUUzEyMzQ=";
        final String clientAuth = "cmQteHl6LWFwaTp4eXo=";
        final String url = "http://127.0.0.1:5000";
        tokenConfigProperties.setClientId(clientId);
        tokenConfigProperties.setClientAuthorization(clientAuth);
        tokenConfigProperties.setAuthorization(authorization);
        tokenConfigProperties.setRedirectUri(redirectUri);
        tokenConfigProperties.setUrl(url);
        jrdSidamTokenService.props = tokenConfigProperties;
        jrdSidamTokenService.recordsPerPage = 1;
    }

    @Test
    void getBearerToken() {
        when(openIdTokenResponseMock.getAccessToken()).thenReturn(CLIENT_AUTHORIZATION);
        when(idamClientMock.getOpenIdToken(any())).thenReturn(openIdTokenResponseMock);
        String actualToken = jrdSidamTokenService.getBearerToken();
        assertThat(actualToken).isEqualTo(CLIENT_AUTHORIZATION);
        verify(openIdTokenResponseMock, times(1)).getAccessToken();
        verify(idamClientMock, times(1)).getOpenIdToken(any());
    }

    @Test
    void getBearerTokenWithException() {
        when(openIdTokenResponseMock.getAccessToken()).thenReturn(CLIENT_AUTHORIZATION);
        assertThrows(JudicialDataLoadException.class, () -> jrdSidamTokenService.getBearerToken());
    }

    @Test
    void testSyncFeed() throws JsonProcessingException {
        when(openIdTokenResponseMock.getAccessToken()).thenReturn(CLIENT_AUTHORIZATION);
        when(idamClientMock.getOpenIdToken(any())).thenReturn(openIdTokenResponseMock);

        List<IdamClient.User> users = new ArrayList<>();
        users.add(createUser("some@some.com"));
        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(users);

        Map<String, Collection<String>> map = new HashMap<>();
        Collection<String> list = new ArrayList<>();
        list.add("5");
        map.put("X-Total-Count", list);

        Response response = Response.builder().request(Request.create(Request.HttpMethod.GET, "", new HashMap<>(),
            Request.Body.empty(), null)).headers(map).body(body, Charset.defaultCharset())
            .status(200).build();
        when(idamClientMock.getUserFeed(anyString(), any())).thenReturn(response);
        Set<IdamClient.User> useResponses = jrdSidamTokenService.getSyncFeed();
        assertThat(response).isNotNull();
        useResponses.forEach(useResponse -> {
            assertThat(useResponse.getEmail()).isEqualTo("some@some.com");
        });
        verify(idamClientMock, times(5)).getUserFeed(anyString(), any());
    }

    @Test
    void testSyncFeedResponseError() throws JsonProcessingException {
        when(openIdTokenResponseMock.getAccessToken()).thenReturn(CLIENT_AUTHORIZATION);
        when(idamClientMock.getOpenIdToken(any())).thenReturn(openIdTokenResponseMock);

        List<IdamClient.User> users = new ArrayList<>();
        users.add(createUser("some@some.com"));
        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(users);

        Response response = Response.builder().request(Request.create(Request.HttpMethod.GET, "", new HashMap<>(),
            Request.Body.empty(), null)).body(body, Charset.defaultCharset())
            .status(500).build();
        when(idamClientMock.getUserFeed(anyString(), any())).thenReturn(response);
        assertThrows(JudicialDataLoadException.class, () -> jrdSidamTokenService.getSyncFeed());
    }

    @Test
    void testSyncFeedExceptionCounter() throws JsonProcessingException {
        when(openIdTokenResponseMock.getAccessToken()).thenReturn(CLIENT_AUTHORIZATION);
        when(idamClientMock.getOpenIdToken(any())).thenReturn(openIdTokenResponseMock);

        List<IdamClient.User> users = new ArrayList<>();
        users.add(createUser("some@some.com"));
        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(users);

        Map<String, Collection<String>> map = new HashMap<>();
        Collection<String> list = new ArrayList<>();
        list.add("dummy");
        map.put("X-Total-Count", list);

        Response response = Response.builder().request(Request.create(Request.HttpMethod.GET, "", new HashMap<>(),
            Request.Body.empty(), null)).headers(map).body(body, Charset.defaultCharset())
            .status(200).build();
        when(idamClientMock.getUserFeed(anyString(), any())).thenReturn(response);
        assertThrows(JudicialDataLoadException.class, () -> jrdSidamTokenService.getSyncFeed());
    }


    private IdamClient.User createUser(String email) {
        IdamClient.User profile = new IdamClient.User();
        profile.setActive(true);
        profile.setEmail(email);
        profile.setForename("some");
        profile.setId(UUID.randomUUID().toString());
        profile.setActive(true);
        return profile;
    }

    @Test
    @SneakyThrows
    void testLogResponse() {
        List<IdamClient.User> users = new ArrayList<>();
        users.add(createUser("some@some.com"));
        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(users);
        Map<String, Collection<String>> map = new HashMap<>();
        Collection<String> list = new ArrayList<>();
        list.add("5");
        map.put("X-Total-Count", list);
        Response response = spy(Response.builder().request(Request.create(Request.HttpMethod.GET, "", new HashMap<>(),
            Request.Body.empty(), null)).headers(map).body(body, Charset.defaultCharset())
            .status(200).build());
        invokeMethod(jrdSidamTokenService, "logIdamResponse", response);
        verify(response, times(2)).status();
    }

    @Test
    @SneakyThrows
    void testLogEmptyResponse() {
        Response nullResponse = spy(Response.builder().request(Request.create(Request.HttpMethod.GET, "",
            new HashMap<>(),
            Request.Body.create((byte[]) null), null)).build());
        invokeMethod(jrdSidamTokenService, "logIdamResponse", nullResponse);
        assertNull(nullResponse.body());
    }

    @Test
    @SneakyThrows
    void testErrorStatus() {
        List<IdamClient.User> users = new ArrayList<>();
        users.add(createUser("some@some.com"));
        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(users);
        Map<String, Collection<String>> map = new HashMap<>();
        Collection<String> list = new ArrayList<>();
        list.add("5");
        map.put("X-Total-Count", list);
        Response response = spy(Response.builder().request(Request.create(Request.HttpMethod.GET, "",
            new HashMap<>(),
            Request.Body.empty(), null)).headers(map).body(body, Charset.defaultCharset())
            .status(500).build());
        invokeMethod(jrdSidamTokenService, "logIdamResponse", response);
        verify(response, times(3)).status();
    }
}
