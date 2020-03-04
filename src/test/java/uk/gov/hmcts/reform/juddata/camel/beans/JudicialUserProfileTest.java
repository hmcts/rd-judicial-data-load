package uk.gov.hmcts.reform.juddata.camel.beans;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper.createJudicialUserProfileMock;

import java.time.LocalDate;
import org.junit.Test;

public class JudicialUserProfileTest {

    @Test
    public void  test_objects_JudicialUserProfile_correctly() {

        LocalDate currentDate = LocalDate.now();
        JudicialUserProfile judicialUserProfile = createJudicialUserProfileMock(currentDate);

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
        assertEquals(currentDate, judicialUserProfile.getJoiningDate());
        assertEquals(currentDate, judicialUserProfile.getLastWorkingDate());
        assertEquals(true, judicialUserProfile.isActiveFlag());
        assertEquals(currentDate.toString(), judicialUserProfile.getExtractedDate());
    }
}
