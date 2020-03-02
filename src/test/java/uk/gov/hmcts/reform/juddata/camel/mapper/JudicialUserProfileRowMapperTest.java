package uk.gov.hmcts.reform.juddata.camel.mapper;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper.createJudicialUserProfileMock;

import java.time.LocalDate;
import java.util.Map;
import org.junit.Test;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialUserProfile;

public class JudicialUserProfileRowMapperTest {
    @Test
    public void should_return_JudicialUserProfileRow_response() {

        LocalDate currentDate = LocalDate.now();

        JudicialUserProfile judicialUserProfileMock = createJudicialUserProfileMock(currentDate);

        JudicialUserProfileRowMapper judicialUserProfileRowMapper = new JudicialUserProfileRowMapper();
        Map<String, Object> response = judicialUserProfileRowMapper.getMap(judicialUserProfileMock);

        assertEquals(response.get("elinks_id"), "elinksid_1");
        assertEquals(response.get("personal_code"), "personalCode_1");
        assertEquals(response.get("title"), "title");
        assertEquals(response.get("known_as"), "knownAs");
        assertEquals(response.get("surname"), "surname");
        assertEquals(response.get("full_name"), "fullName");
        assertEquals(response.get("post_nominals"), "postNominals");
        assertEquals(response.get("contract_type"), "contractTypeId");
        assertEquals(response.get("work_pattern"), "workpatterns");
        assertEquals(response.get("email_id"), "some@hmcts.net");
        assertEquals(response.get("joining_date"), currentDate);
        assertEquals(response.get("last_working_date"), currentDate);
        assertEquals(response.get("active_flag"), true);
        assertEquals(response.get("extracted_date"), currentDate.toString());

    }
}
         
         