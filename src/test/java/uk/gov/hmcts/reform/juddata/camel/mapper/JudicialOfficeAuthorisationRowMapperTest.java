package uk.gov.hmcts.reform.juddata.camel.mapper;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAuthorisation;

import java.sql.Timestamp;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialOfficeAuthorisation;

class JudicialOfficeAuthorisationRowMapperTest {

    JudicialOfficeAuthorisationRowMapper judicialOfficeAuthorisationRowMapper =
        new JudicialOfficeAuthorisationRowMapper();

    @Test
    void should_return_JudicialOfficeAuthorizationtRowMapper_response() {

        JudicialOfficeAuthorisation judicialOfficeAuthorisation =
            createJudicialOfficeAuthorisation("2017-10-01 00:00:00.000");

        Map<String, Object> authMap = judicialOfficeAuthorisationRowMapper.getMap(judicialOfficeAuthorisation);

        assertNotNull(authMap.get("judicial_office_auth_id"));
        assertEquals("1", authMap.get("per_id"));
        assertEquals("jurisdiction", authMap.get("jurisdiction"));
        assertEquals(Timestamp.valueOf((judicialOfficeAuthorisation.getStartDate())), authMap.get("start_date"));
        assertEquals(Timestamp.valueOf((judicialOfficeAuthorisation.getEndDate())), authMap.get("end_date"));
        assertEquals(Long.valueOf("12345"), authMap.get("ticket_id"));
        assertEquals("lowerLevel", authMap.get("lower_level"));
        assertEquals("111", authMap.get("personal_code"));
        assertEquals("779321b3-3170-44a0-bc7d-b4decc2aea10", authMap.get("object_id"));
    }

    @Test
    void should_generate_id() {
        int id = judicialOfficeAuthorisationRowMapper.generateId();

        assertThat(id).isEqualTo(1);
    }
}
