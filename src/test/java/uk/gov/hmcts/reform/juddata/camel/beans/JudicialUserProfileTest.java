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

        assertEquals(judicialUserProfile.getElinksId(), "elinksid_1");
        assertEquals(judicialUserProfile.getPersonalCode(), "personalCode_1");
        assertEquals(judicialUserProfile.getTitle(), "title");
        assertEquals(judicialUserProfile.getKnownAs(), "knownAs");
        assertEquals(judicialUserProfile.getSurName(), "surname");
        assertEquals(judicialUserProfile.getFullName(), "fullName");
        assertEquals(judicialUserProfile.getPostNominals(), "postNominals");
        assertEquals(judicialUserProfile.getContractTypeId(), "contractTypeId");
        assertEquals(judicialUserProfile.getWorkPattern(), "workpatterns");
        assertEquals(judicialUserProfile.getEmailId(), "some@hmcts.net");
        assertEquals(judicialUserProfile.getJoiningDate(), currentDate);
        assertEquals(judicialUserProfile.getLastWorkingDate(), currentDate);
        assertEquals(judicialUserProfile.isActiveFlag(), true);
        assertEquals(judicialUserProfile.getExtractedDate(), currentDate.toString());
    }
}
