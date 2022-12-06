package uk.gov.hmcts.reform.juddata.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class ElinkAccessApiService {

    private static final Logger logger = LogManager.getLogger(ElinkAccessApiService.class);

    @Value("${judicialUrl}")
    private String judicialUrl;
    @Value("${locationUrl}")
    private String locationUrl;

    public List<ResponseEntity> retrieveDetails() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        @SuppressWarnings("unchecked")
        HttpEntity entity = new HttpEntity(headers);

        logger.info("Judicial api  health check call: " + LocalDateTime.now());

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> regionIdResponseEntity = template.exchange(judicialUrl, HttpMethod.GET, entity,
                String.class);
        logger.info("Location api health check call: " + LocalDateTime.now());
        ResponseEntity<String> orgServiceResponseEntity = template.exchange(locationUrl, HttpMethod.GET, entity,
                String.class);


        return Arrays.asList(regionIdResponseEntity, orgServiceResponseEntity);


    }

}
