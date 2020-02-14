package uk.gov.hmcts.reform.juddata.camel.mapper;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialAuthorisationType;

@Slf4j
@Component
public class JudicialAuthorisationTypeRowMapper {

    public Map<String, Object> getMap(JudicialAuthorisationType authorizationType) {

        Map<String, Object> authorizationRow = new HashMap<>();
        authorizationRow.put("authorisation_id", authorizationType.getAuthorisationId());
        authorizationRow.put("authorisation_desc_en", authorizationType.getAuthorisationDescEn());
        authorizationRow.put("authorisation_desc_cy", authorizationType.getAuthorisationDescCy());
        authorizationRow.put("jurisdiction_id", authorizationType.getJurisdictionId());
        authorizationRow.put("jurisdiction_desc_en", authorizationType.getJurisdictionDescEn());
        authorizationRow.put("jurisdiction_desc_cy", authorizationType.getJurisdictionDescCy());
        return  authorizationRow;
    }

}
