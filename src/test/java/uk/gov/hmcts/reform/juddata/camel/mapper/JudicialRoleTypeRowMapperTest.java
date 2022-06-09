package uk.gov.hmcts.reform.juddata.camel.mapper;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserRoleType;
import uk.gov.hmcts.reform.juddata.camel.util.CommonUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserRoleType;

class JudicialRoleTypeRowMapperTest {

    JudicialRoleTypeRowMapper judicialRoleTypeRowMapper = new JudicialRoleTypeRowMapper();

    @Test
    void should_return_JudicialRoleTypeRowMapper_response() {
        JudicialUserRoleType judicialContractType = createJudicialUserRoleType();
        Map<String, Object> response = judicialRoleTypeRowMapper.getMap(judicialContractType);

        assertEquals("46804", response.get("per_Id"));
        assertEquals("Family Course Tutor (JC)", response.get("title"));
        assertEquals("Nationwide", response.get("location"));
        assertEquals(CommonUtils.getDateTimeStamp("2018-05-02 00:00:00.0"), response.get("start_date"));
        assertEquals(CommonUtils.getDateTimeStamp("2022-05-01 00:00:00"), response.get("end_date"));

    }
}
