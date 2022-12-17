package uk.gov.hmcts.reform.elinks.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.elinks.configuration.IdamTokenConfigProperties;
import uk.gov.hmcts.reform.elinks.exception.JudicialDataLoadException;
import uk.gov.hmcts.reform.elinks.feign.IdamFeignClient;
import uk.gov.hmcts.reform.elinks.repository.DataloadSchedularAuditRepository;
import uk.gov.hmcts.reform.elinks.response.IdamOpenIdTokenResponse;
import uk.gov.hmcts.reform.elinks.response.IdamResponse;
import uk.gov.hmcts.reform.elinks.service.IdamElasticSearchService;
import uk.gov.hmcts.reform.elinks.util.JsonFeignResponseUtility;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.nonNull;


@Slf4j
@Component
public class IdamElasticSearchServiceImpl implements IdamElasticSearchService {

    @Value("${logging-component-name}")
    String loggingComponentName;

    @Value("${elastic.search.query}")
    String idamSearchQuery;

    @Value("${elastic.search.recordsPerPage}")
    int recordsPerPage;

    @Autowired
    IdamFeignClient idamFeignClient;

    @Autowired
    IdamTokenConfigProperties props;

    @Autowired
    DataloadSchedularAuditRepository dataloadSchedularAuditRepository;

    @Autowired
    @Qualifier("springJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @Override
    public String getIdamBearerToken() throws JudicialDataLoadException {

        byte[] base64UserDetails = Base64.getDecoder().decode(props.getAuthorization());
        Map<String, String> formParams = new HashMap<>();
        formParams.put("grant_type", "password");
        String[] userDetails = new String(base64UserDetails).split(":");
        formParams.put("username", userDetails[0].trim());
        formParams.put("password", userDetails[1].trim());
        formParams.put("client_id", props.getClientId());
        byte[] base64ClientAuth = Base64.getDecoder().decode(props.getClientAuthorization());
        String[] clientAuth = new String(base64ClientAuth).split(":");
        formParams.put("redirect_uri", props.getRedirectUri());
        formParams.put("client_secret", clientAuth[1]);
        formParams.put("scope", "openid profile roles manage-user create-user search-user");

        IdamOpenIdTokenResponse idamOpenIdTokenResponse = idamFeignClient.getOpenIdToken(formParams);

        if (idamOpenIdTokenResponse == null) {
            throw new JudicialDataLoadException("Idam Service Failed while bearer token generate");
        }
        return idamOpenIdTokenResponse.getAccessToken();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<IdamResponse> getIdamElasticSearchSyncFeed() throws JudicialDataLoadException {
        Map<String, String> params = new HashMap<>();
        idamSearchQuery = idamElasticSearchQuery();
        params.put("size",String.valueOf(recordsPerPage));
        params.put("query",idamSearchQuery);
        log.info("{}:: search elk query {}", loggingComponentName, idamSearchQuery);
        Set<IdamResponse> judicialUsers = new HashSet<>();
        int count = 0;
        int totalCount = 0;

        do {
            params.put("page", String.valueOf(count));
            String bearerToken = "Bearer ".concat(getIdamBearerToken());
            Response response = idamFeignClient.getUserFeed(bearerToken, params);
            logIdamResponses(response);

            ResponseEntity<Object> responseEntity = JsonFeignResponseUtility.toResponseEntity(response,
                new TypeReference<Set<IdamResponse>>() {
                });

            if (response.status() == 200) {

                Set<IdamResponse> users = (Set<IdamResponse>) responseEntity.getBody();
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
            count++;
            log.info("{}:: batch count :: ", count);
        } while (totalCount > 0 && recordsPerPage * count < totalCount);
        updateSidamIds(judicialUsers);

        return judicialUsers;
    }

    private void logIdamResponses(Response response) {
        log.info("Logging Response from IDAM");
        if (response != null) {
            log.info("{}:: Response code from idamClient.getUserFeed {}", loggingComponentName, response.status());
            if (response.status() != 200 && response.body() != null) {
                log.info("{}:: Response body from Idam Client ::{}", loggingComponentName, response.status());
            }
        }
    }

    private String idamElasticSearchQuery() {

        LocalDateTime maxSchedulerEndTime = dataloadSchedularAuditRepository.findByScheduleEndTime();

        return maxSchedulerEndTime == null ? String.format(idamSearchQuery,72) : String.format(idamSearchQuery,
                Math.addExact(ChronoUnit.HOURS.between(maxSchedulerEndTime, LocalDateTime.now()), 1));
    }

    public void updateSidamIds(Set<IdamResponse> sidamUsers) {
        List<Pair<String, String>> sidamObjectId = new ArrayList<>();
        String updateSidamIds = "UPDATE dbjudicialdata.judicial_user_profile SET sidam_id = ? "
                + "WHERE object_id = ? AND sidam_id IS NULL";
        sidamUsers.stream().filter(user -> nonNull(user.getSsoId())).forEach(s ->
                sidamObjectId.add(Pair.of(s.getId(), s.getSsoId())));
        log.info("Insert Query batch Response from IDAM" + sidamObjectId.size());
        int[][] records = jdbcTemplate.batchUpdate(
                updateSidamIds,
                sidamObjectId,
                10,
                new ParameterizedPreparedStatementSetter<Pair<String, String>>() {
                    public void setValues(PreparedStatement ps, Pair<String, String> argument) throws SQLException {
                        ps.setString(1, argument.getLeft());
                        ps.setString(2, argument.getRight());
                    }
                });

        log.info("Updated records Value: " + records);
    }
}
