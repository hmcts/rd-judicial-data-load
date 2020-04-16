package uk.gov.hmcts.reform.juddata.camel.binder;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserProfileMock;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateTimeWithFormat;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateWithFormat;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.DATE_FORMAT;

import java.time.LocalDateTime;
import java.util.Date;

import org.junit.Test;

public class JudicialUserProfileTest {

    @Test
    public void  test_objects_JudicialUserProfile_correctly() {

        Date currentDate = new Date();
        LocalDateTime dateTime = LocalDateTime.now();
        JudicialUserProfile judicialUserProfile = createJudicialUserProfileMock(currentDate, dateTime);

        assertEquals("elinksid_1", judicialUserProfile.getElinksId());
        assertEquals("personalCode_1", judicialUserProfile.getPersonalCode());
        assertEquals("title", judicialUserProfile.getTitle());
        assertEquals("knownAs", judicialUserProfile.getKnownAs());
        assertEquals("surname", judicialUserProfile.getSurName());
        assertEquals("fullName", judicialUserProfile.getFullName());
        assertEquals("postNominals", judicialUserProfile.getPostNominals());
        assertEquals("contractTypeId", judicialUserProfile.getContractTypeId());
        assertEquals("workpatterns", judicialUserProfile.getWorkPattern());
        assertEquals("some@hmcts.net", judicialUserProfile.getEmailId());
        assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), judicialUserProfile.getJoiningDate());
        assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), judicialUserProfile.getLastWorkingDate());
        assertEquals(true, judicialUserProfile.isActiveFlag());
        assertEquals(getDateTimeWithFormat(dateTime), judicialUserProfile.getExtractedDate());
    }
}
