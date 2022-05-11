package uk.gov.hmcts.reform.juddata.camel.mapper;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.PERID_1;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserProfileMock;
import static uk.gov.hmcts.reform.juddata.camel.util.CommonUtils.getDateTimeStamp;

class JudicialUserProfileRowMapperTest {
    @Test
    void should_return_JudicialUserProfileRow_response() {

        Date currentDate = new Date();
        LocalDateTime dateTime = LocalDateTime.now();

        JudicialUserProfile judicialUserProfileMock = createJudicialUserProfileMock(currentDate, dateTime, PERID_1);

        JudicialUserProfileRowMapper judicialUserProfileRowMapper = new JudicialUserProfileRowMapper();
        Map<String, Object> response = judicialUserProfileRowMapper.getMap(judicialUserProfileMock);

        assertEquals(PERID_1, response.get("per_id"));
        assertEquals("personalCode_1", response.get("personal_code"));
        assertEquals("knownAs", response.get("known_as"));
        assertEquals("surname", response.get("surname"));
        assertEquals("fullName", response.get("full_name"));
        assertEquals("postNominals", response.get("post_nominals"));
        assertEquals("workpatterns", response.get("work_pattern"));
        assertEquals("some@hmcts.net", response.get("ejudiciary_email"));
        assertEquals(getDateTimeStamp("28-04-2022 00:00:00"), response.get("joining_date"));
        assertEquals(getDateTimeStamp("28-06-2022 00:00:00"), response.get("last_working_date"));
        assertEquals(true, response.get("active_flag"));
        assertEquals(getDateTimeStamp("28-05-2022 00:00:00"), response.get("extracted_date"));
        assertEquals("779321b3-3170-44a0-bc7d-b4decc2aea10", response.get("object_id"));
        assertEquals(true, response.get("is_judge"));
        assertEquals(true, response.get("is_panel_member"));
        assertEquals(false, response.get("is_magistrate"));
        assertEquals(getDateTimeStamp("28-04-2022 00:00:00"), response.get("mrd_created_time"));
        assertEquals(getDateTimeStamp("28-05-2022 00:00:00"), response.get("mrd_updated_time"));
        assertEquals(getDateTimeStamp("28-06-2022 00:00:00"), response.get("mrd_deleted_time"));
    }

    @Test
    void should_return_JudicialUserProfileRow_response_withKnown_as_filed_populated() {

        Date currentDate = new Date();
        LocalDateTime dateTime = LocalDateTime.now();

        JudicialUserProfile judicialUserProfileMock = createJudicialUserProfileMock(currentDate, dateTime, PERID_1);
        judicialUserProfileMock.setKnownAs(null);
        JudicialUserProfileRowMapper judicialUserProfileRowMapper = new JudicialUserProfileRowMapper();
        Map<String, Object> response = judicialUserProfileRowMapper.getMap(judicialUserProfileMock);

        assertEquals(PERID_1, response.get("per_id"));
        assertEquals("personalCode_1", response.get("personal_code"));
        assertEquals("fullName", response.get("known_as"));
        assertEquals("surname", response.get("surname"));
        assertEquals("fullName", response.get("full_name"));
        assertEquals("postNominals", response.get("post_nominals"));
        assertEquals("workpatterns", response.get("work_pattern"));
        assertEquals("some@hmcts.net", response.get("ejudiciary_email"));
        assertEquals(getDateTimeStamp("28-04-2022 00:00:00"), response.get("joining_date"));
        assertEquals(getDateTimeStamp("28-06-2022 00:00:00"), response.get("last_working_date"));
        assertEquals(true, response.get("active_flag"));
        assertEquals(getDateTimeStamp("28-05-2022 00:00:00"), response.get("extracted_date"));
        assertEquals("779321b3-3170-44a0-bc7d-b4decc2aea10", response.get("object_id"));
        assertEquals(true, response.get("is_judge"));
        assertEquals(true, response.get("is_panel_member"));
        assertEquals(false, response.get("is_magistrate"));
        assertEquals(getDateTimeStamp("28-04-2022 00:00:00"), response.get("mrd_created_time"));
        assertEquals(getDateTimeStamp("28-05-2022 00:00:00"), response.get("mrd_updated_time"));
        assertEquals(getDateTimeStamp("28-06-2022 00:00:00"), response.get("mrd_deleted_time"));
    }
}
         
         