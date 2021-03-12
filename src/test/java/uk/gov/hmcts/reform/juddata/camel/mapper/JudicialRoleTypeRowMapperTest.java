package uk.gov.hmcts.reform.juddata.camel.mapper;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserRoleType;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserRoleType;

class JudicialRoleTypeRowMapperTest {

    JudicialRoleTypeRowMapper judicialRoleTypeRowMapper = new JudicialRoleTypeRowMapper();

    @Test
    void should_return_JudicialRoleTypeRowMapper_response() {
        JudicialUserRoleType judicialContractType = createJudicialUserRoleType();
        Map<String, Object> response = judicialRoleTypeRowMapper.getMap(judicialContractType);

        assertEquals("roleId", response.get("role_id"));
        assertEquals("roleDescEn", response.get("role_desc_en"));
        assertEquals("roleDescCy", response.get("role_desc_cy"));

    }
}
