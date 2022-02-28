package uk.gov.hmcts.reform.juddata.camel.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAuthorisation;

import java.sql.Timestamp;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialOfficeAuthorisation;

class JudicialOfficeAuthorisationRowMapperTest {

    final JudicialOfficeAuthorisationRowMapper judicialOfficeAuthorisationRowMapper =
        new JudicialOfficeAuthorisationRowMapper();

    @Test
    void should_return_JudicialOfficeAuthorizationtRowMapper_response() {

        JudicialOfficeAuthorisation judicialOfficeAuthorisation =
            createJudicialOfficeAuthorisation("2017-10-01 00:00:00.000");

        Map<String, Object> authMap = judicialOfficeAuthorisationRowMapper.getMap(judicialOfficeAuthorisation);

        Assertions.assertNotNull(authMap.get("judicial_office_auth_id"));
        Assertions.assertEquals("1", authMap.get("per_id"));
        Assertions.assertEquals("jurisdiction", authMap.get("jurisdiction"));
        Assertions.assertEquals(Timestamp.valueOf((judicialOfficeAuthorisation.getStartDate())),
                authMap.get("start_date"));
        Assertions.assertEquals(Timestamp.valueOf((judicialOfficeAuthorisation.getEndDate())), authMap.get("end_date"));
        Assertions.assertEquals(Long.valueOf("12345"), authMap.get("ticket_id"));
        Assertions.assertEquals("lowerLevel", authMap.get("lower_level"));
        Assertions.assertEquals("111", authMap.get("personal_code"));
        Assertions.assertEquals("779321b3-3170-44a0-bc7d-b4decc2aea10", authMap.get("object_id"));
    }

    @Test
    void should_generate_id() {
        int id = judicialOfficeAuthorisationRowMapper.generateId();

        assertThat(id).isEqualTo(1);
    }
}
