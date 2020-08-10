package uk.gov.hmcts.reform.juddata.camel.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialOfficeAuthorisation;

import java.sql.Timestamp;
import java.util.Map;
import org.junit.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAuthorisation;

public class JudicialOfficeAuthorisationRowMapperTest {

    @Test
    public void should_return_JudicialOfficeAuthorizationtRowMapper_response() {

        JudicialOfficeAuthorisation judicialOfficeAuthorisation =
                createJudicialOfficeAuthorisation("2017-10-01 00:00:00.000");
        JudicialOfficeAuthorisationRowMapper judicialOfficeAuthorisationRowMapper =
                new JudicialOfficeAuthorisationRowMapper();
        Map<String, Object> authMap = judicialOfficeAuthorisationRowMapper.getMap(judicialOfficeAuthorisation);

        assertNotNull(authMap.get("judicial_office_auth_id"));
        assertEquals("1", authMap.get("elinks_id"));
        assertEquals("jurisdiction", authMap.get("jurisdiction"));
        assertEquals(Timestamp.valueOf((judicialOfficeAuthorisation.getStartDate())), authMap.get("start_date"));
        assertEquals(Timestamp.valueOf((judicialOfficeAuthorisation.getEndDate())), authMap.get("end_date"));
        assertEquals(Timestamp.valueOf((judicialOfficeAuthorisation.getCreatedDate())), authMap.get("created_date"));
        assertEquals(Timestamp.valueOf((judicialOfficeAuthorisation.getLastUpdated())), authMap.get("last_updated"));
        assertEquals(Long.valueOf("12345"), authMap.get("ticket_id"));
        assertEquals("lowerLevel", authMap.get("lower_level"));
    }
}
