package uk.gov.hmcts.reform.juddata.camel.mapper;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.DATE_FORMAT;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserProfileMock;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateTimeWithFormat;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateWithFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import org.junit.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;

public class JudicialUserProfileRowMapperTest {
    @Test
    public void should_return_JudicialUserProfileRow_response() {

        Date currentDate = new Date();
        LocalDateTime dateTime = LocalDateTime.now();

        JudicialUserProfile judicialUserProfileMock = createJudicialUserProfileMock(currentDate, dateTime);

        JudicialUserProfileRowMapper judicialUserProfileRowMapper = new JudicialUserProfileRowMapper();
        Map<String, Object> response = judicialUserProfileRowMapper.getMap(judicialUserProfileMock);

        assertEquals("elinksid_1", response.get("elinks_id"));
        assertEquals("personalCode_1", response.get("personal_code"));
        assertEquals("title", response.get("title"));
        assertEquals("knownAs", response.get("known_as"));
        assertEquals("surname", response.get("surname"));
        assertEquals("fullName", response.get("full_name"));
        assertEquals("postNominals", response.get("post_nominals"));
        assertEquals("contractTypeId", response.get("contract_type"));
        assertEquals("workpatterns", response.get("work_pattern"));
        assertEquals("some@hmcts.net", response.get("email_id"));
        assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), response.get("joining_date"));
        assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), response.get("last_working_date"));
        assertEquals(true, response.get("active_flag"));
        assertEquals(getDateTimeWithFormat(dateTime), response.get("extracted_date"));
        assertEquals("779321b3-3170-44a0-bc7d-b4decc2aea10", response.get("object_id"));
    }

    @Test
    public void should_return_JudicialUserProfileRow_response_withKnown_as_filed_populated() {

        Date currentDate = new Date();
        LocalDateTime dateTime = LocalDateTime.now();

        JudicialUserProfile judicialUserProfileMock = createJudicialUserProfileMock(currentDate, dateTime);
        judicialUserProfileMock.setKnownAs(null);
        JudicialUserProfileRowMapper judicialUserProfileRowMapper = new JudicialUserProfileRowMapper();
        Map<String, Object> response = judicialUserProfileRowMapper.getMap(judicialUserProfileMock);

        assertEquals("elinksid_1", response.get("elinks_id"));
        assertEquals("personalCode_1", response.get("personal_code"));
        assertEquals("title", response.get("title"));
        assertEquals("fullName", response.get("known_as"));
        assertEquals("surname", response.get("surname"));
        assertEquals("fullName", response.get("full_name"));
        assertEquals("postNominals", response.get("post_nominals"));
        assertEquals("contractTypeId", response.get("contract_type"));
        assertEquals("workpatterns", response.get("work_pattern"));
        assertEquals("some@hmcts.net", response.get("email_id"));
        assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), response.get("joining_date"));
        assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), response.get("last_working_date"));
        assertEquals(true, response.get("active_flag"));
        assertEquals(getDateTimeWithFormat(dateTime), response.get("extracted_date"));
        assertEquals("779321b3-3170-44a0-bc7d-b4decc2aea10", response.get("object_id"));
    }
}
         
         