package uk.gov.hmcts.reform.juddata.elinks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import feign.Response;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.reform.data.ingestion.configuration.AzureBlobConfig;
import uk.gov.hmcts.reform.elinks.ELinksController;
import uk.gov.hmcts.reform.elinks.configuration.ElinksFeignInterceptorConfiguration;
import uk.gov.hmcts.reform.elinks.configuration.FeignHeaderConfig;
import uk.gov.hmcts.reform.elinks.feign.IdamFeignClient;
import uk.gov.hmcts.reform.elinks.response.IdamOpenIdTokenResponse;
import uk.gov.hmcts.reform.elinks.response.IdamResponse;
import uk.gov.hmcts.reform.elinks.service.impl.IdamElasticSearchServiceImpl;
import uk.gov.hmcts.reform.idam.client.IdamApi;
import uk.gov.hmcts.reform.juddata.client.IdamClient;
import uk.gov.hmcts.reform.juddata.configuration.EmailConfiguration;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@TestPropertySource(properties = {"spring.config.location=classpath:application-integration-test.yml,"
        + "classpath:application-test.yml,"
        + "classpath:application-leaf-integration-test.yml"})
@MockEndpoints("log:*")


@ContextConfiguration(classes = {
        AzureBlobConfig.class,
        FeignHeaderConfig.class,
        ElinksFeignInterceptorConfiguration.class,
        FeignHeaderConfig.class,
        EmailConfiguration.class,
        ELinksControllerTestConfig.class,
        IdamElasticSearchServiceImpl.class,
        ELinksController.class}, initializers = ConfigDataApplicationContextInitializer.class)


@EnableAutoConfiguration
@EntityScan("uk.gov.hmcts.reform.elinks.domain")
@EnableJpaRepositories("uk.gov.hmcts.reform.elinks.repository")

@EnableFeignClients(clients = {
        IdamClient.class,
        IdamApi.class,
        IdamFeignClient.class})
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SuppressWarnings({"AbbreviationAsWordInName","MemberName"})
public class IdamElasticSearchIntegrationTest {

    @Autowired
    ELinksController eLinksController;

    @InjectMocks
    IdamElasticSearchServiceImpl idamElasticSearchServiceImpl;

    @Mock
    IdamFeignClient idamFeignClientOne;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(idamElasticSearchServiceImpl, "idamFeignClient",
                idamFeignClientOne
        );
    }

    @Test
    void test_elinksController_loaded() {
        assertThat(eLinksController).isNotNull();
    }

    @Test
    void test_elinksService_idam_elastic_search() throws IOException {

        List<IdamResponse> users = new ArrayList<>();
        users.add(createIdamUser("test@idamuser.com"));
        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(users);

        Map<String, Collection<String>> map = new HashMap<>();
        Collection<String> list = new ArrayList<>();
        list.add("5");
        map.put("X-Total-Count", list);

        IdamOpenIdTokenResponse accessToken = new  IdamOpenIdTokenResponse("token");

        Response response = Response.builder().request(Request.create(Request.HttpMethod.GET, "", new HashMap<>(),
                        Request.Body.empty(), null)).headers(map).body(body, Charset.defaultCharset())
                .status(200).build();
        when(idamFeignClientOne.getUserFeed(anyString(), any())).thenReturn(response);
        when(idamFeignClientOne.getOpenIdToken(anyMap())).thenReturn(accessToken);

        ResponseEntity<Object> responseEntity = eLinksController.idamElasticSearch();
        Set<IdamResponse> useResponses = (Set<IdamResponse>) responseEntity.getBody();
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    void test_elinksService_idam_elastic_search_empty() throws IOException {

        List<IdamResponse> users = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(users);

        Map<String, Collection<String>> map = new HashMap<>();
        Collection<String> list = new ArrayList<>();
        list.add("0");
        map.put("X-Total-Count", list);

        IdamOpenIdTokenResponse accessToken = new  IdamOpenIdTokenResponse("token");

        Response response = Response.builder().request(Request.create(Request.HttpMethod.GET, "", new HashMap<>(),
                        Request.Body.empty(), null)).headers(map).body(body, Charset.defaultCharset())
                .status(200).build();
        when(idamFeignClientOne.getUserFeed(anyString(), any())).thenReturn(response);
        when(idamFeignClientOne.getOpenIdToken(anyMap())).thenReturn(accessToken);

        ResponseEntity<Object> responseEntity = eLinksController.idamElasticSearch();
        Set<IdamResponse> useResponses = (Set<IdamResponse>) responseEntity.getBody();
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(useResponses).hasSize(0);

    }

    private IdamResponse createIdamUser(String email) {
        IdamResponse profile = new IdamResponse();
        profile.setActive(true);
        profile.setEmail(email);
        profile.setForename("some");
        profile.setId(UUID.randomUUID().toString());
        return profile;
    }

}
