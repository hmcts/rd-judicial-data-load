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
        authorizationRow.put("authorisation_id", authorizationType.getAuthorisation_id());
        authorizationRow.put("authorisation_desc_en", authorizationType.getAuthorisation_desc_en());
        authorizationRow.put("authorisation_desc_cy", authorizationType.getAuthorisation_desc_cy());
        authorizationRow.put("jurisdiction_id", authorizationType.getJurisdiction_id());
        authorizationRow.put("jurisdiction_desc_en", authorizationType.getJurisdiction_desc_en());
        authorizationRow.put("jurisdiction_desc_cy", authorizationType.getJurisdiction_desc_cy());
        return  authorizationRow;
    }

}
