package uk.gov.hmcts.reform.elinks.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import feign.Response;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.data.ingestion.configuration.AzureBlobConfig;
import uk.gov.hmcts.reform.elinks.ELinksController;
import uk.gov.hmcts.reform.elinks.configuration.ElinkEmailConfiguration;
import uk.gov.hmcts.reform.elinks.configuration.ElinksFeignInterceptorConfiguration;
import uk.gov.hmcts.reform.elinks.configuration.FeignHeaderConfig;
import uk.gov.hmcts.reform.elinks.response.SchedulerJobStatusResponse;
import uk.gov.hmcts.reform.elinks.service.impl.PublishSidamIdServiceImpl;
import uk.gov.hmcts.reform.juddata.elinks.controller.ELinksControllerTestConfig;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(properties = {"spring.config.location=classpath:application-integration-test.yml,"
        + "classpath:application-test.yml,"
        + "classpath:application-leaf-integration-test.yml"})
@MockEndpoints("log:*")


@ContextConfiguration(classes = {
        AzureBlobConfig.class,
        FeignHeaderConfig.class,
        ElinksFeignInterceptorConfiguration.class,
        FeignHeaderConfig.class,
        ElinkEmailConfiguration.class,
        ELinksControllerTestConfig.class,
        PublishSidamIdServiceImpl.class,
        ELinksController.class}, initializers = ConfigDataApplicationContextInitializer.class)


@EnableAutoConfiguration
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SuppressWarnings("unchecked")
class PublishSidamIdServiceIntegrationTest {

    @Autowired
    ELinksController elinksController;

    @Autowired
    PublishSidamIdServiceImpl publishSidamIdService;


    @InjectMocks
    JdbcTemplate jdbcTemplate;


    @Test
    void test_elinks_controller_loaded() {
        assertThat(elinksController).isNotNull();
    }

    @Test
    void test_elinks_service_publish_sidam_id_to_asb() throws Exception {

        List<String> sidamIds = new ArrayList<>();
        sidamIds.add("edc4190e-8e31-47d5-af56-cb7784bcd3a9");
        ObjectMapper mapper = new ObjectMapper();
        String body = sidamIds.get(0);

        Map<String, Collection<String>> map = new HashMap<>();
        Collection<String> list = new ArrayList<>();
        list.add("5");
        map.put("X-Total-Count", list);

        Response response = Response.builder().request(Request.create(Request.HttpMethod.GET, "", new HashMap<>(),
                        Request.Body.empty(), null)).headers(map).body(body, Charset.defaultCharset())
                .status(200).build();

        ResponseEntity<Object> responseEntity = elinksController.publishSidamIdToAsb();
        SchedulerJobStatusResponse useResponses =  (SchedulerJobStatusResponse) responseEntity.getBody();

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

    }
}
