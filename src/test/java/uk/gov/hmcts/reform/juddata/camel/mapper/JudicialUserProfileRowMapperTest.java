package uk.gov.hmcts.reform.juddata.camel.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.DATE_FORMAT;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.PERID_1;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserProfileMock;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateTimeWithFormat;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateWithFormat;

class JudicialUserProfileRowMapperTest {
    @Test
    void should_return_JudicialUserProfileRow_response() {

        Date currentDate = new Date();
        LocalDateTime dateTime = LocalDateTime.now();

        JudicialUserProfile judicialUserProfileMock = createJudicialUserProfileMock(currentDate, dateTime, PERID_1);

        JudicialUserProfileRowMapper judicialUserProfileRowMapper = new JudicialUserProfileRowMapper();
        Map<String, Object> response = judicialUserProfileRowMapper.getMap(judicialUserProfileMock);

        Assertions.assertEquals(PERID_1, response.get("per_id"));
        Assertions.assertEquals("personalCode_1", response.get("personal_code"));
        Assertions.assertEquals("knownAs", response.get("known_as"));
        Assertions.assertEquals("surname", response.get("surname"));
        Assertions.assertEquals("fullName", response.get("full_name"));
        Assertions.assertEquals("postNominals", response.get("post_nominals"));
        Assertions.assertEquals("workpatterns", response.get("work_pattern"));
        Assertions.assertEquals("some@hmcts.net", response.get("ejudiciary_email"));
        Assertions.assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), response.get("joining_date"));
        Assertions.assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), response.get("last_working_date"));
        Assertions.assertEquals(true, response.get("active_flag"));
        Assertions.assertEquals(getDateTimeWithFormat(dateTime), response.get("extracted_date"));
        Assertions.assertEquals("779321b3-3170-44a0-bc7d-b4decc2aea10", response.get("object_id"));
    }

    @Test
    void should_return_JudicialUserProfileRow_response_withKnown_as_filed_populated() {

        Date currentDate = new Date();
        LocalDateTime dateTime = LocalDateTime.now();

        JudicialUserProfile judicialUserProfileMock = createJudicialUserProfileMock(currentDate, dateTime, PERID_1);
        judicialUserProfileMock.setKnownAs(null);
        JudicialUserProfileRowMapper judicialUserProfileRowMapper = new JudicialUserProfileRowMapper();
        Map<String, Object> response = judicialUserProfileRowMapper.getMap(judicialUserProfileMock);

        Assertions.assertEquals(PERID_1, response.get("per_id"));
        Assertions.assertEquals("personalCode_1", response.get("personal_code"));
        Assertions.assertEquals("fullName", response.get("known_as"));
        Assertions.assertEquals("surname", response.get("surname"));
        Assertions.assertEquals("fullName", response.get("full_name"));
        Assertions.assertEquals("postNominals", response.get("post_nominals"));
        Assertions.assertEquals("workpatterns", response.get("work_pattern"));
        Assertions.assertEquals("some@hmcts.net", response.get("ejudiciary_email"));
        Assertions.assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), response.get("joining_date"));
        Assertions.assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), response.get("last_working_date"));
        Assertions.assertEquals(true, response.get("active_flag"));
        Assertions.assertEquals(getDateTimeWithFormat(dateTime), response.get("extracted_date"));
        Assertions.assertEquals("779321b3-3170-44a0-bc7d-b4decc2aea10", response.get("object_id"));
    }
}
         
         