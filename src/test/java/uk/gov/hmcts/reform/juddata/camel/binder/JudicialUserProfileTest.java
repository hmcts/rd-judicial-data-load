package uk.gov.hmcts.reform.juddata.camel.binder;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.PERID_1;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserProfileMock;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserProfileInactiveMock;

class JudicialUserProfileTest {

    @Test
    void test_objects_JudicialUserProfile_correctly() {

        Date currentDate = new Date();
        LocalDateTime dateTime = LocalDateTime.now();
        JudicialUserProfile judicialUserProfile = createJudicialUserProfileMock(currentDate, dateTime, PERID_1);

        assertEquals(PERID_1, judicialUserProfile.getPerId());
        assertEquals("personalCode_1", judicialUserProfile.getPersonalCode());
        assertEquals("knownAs", judicialUserProfile.getKnownAs());
        assertEquals("surname", judicialUserProfile.getSurName());
        assertEquals("fullName", judicialUserProfile.getFullName());
        assertEquals("postNominals", judicialUserProfile.getPostNominals());
        assertEquals("workpatterns", judicialUserProfile.getWorkPattern());
        assertEquals("some@hmcts.net", judicialUserProfile.getEjudiciaryEmail());
        assertEquals("28-04-2022 00:00:00", judicialUserProfile.getJoiningDate());
        assertEquals("28-06-2022 00:00:00", judicialUserProfile.getLastWorkingDate());
        assertEquals("28-05-2022 00:00:00", judicialUserProfile.getExtractedDate());
        assertEquals("779321b3-3170-44a0-bc7d-b4decc2aea10", judicialUserProfile.getObjectId());
        assertTrue(judicialUserProfile.isJudge());
        assertTrue(judicialUserProfile.isPanelMember());
        assertFalse(judicialUserProfile.isMagistrate());
        assertEquals("28-04-2022 00:00:00", judicialUserProfile.getMrdCreatedTime());
        assertEquals("28-05-2022 00:00:00", judicialUserProfile.getMrdUpdatedTime());
        assertEquals("28-06-2022 00:00:00", judicialUserProfile.getMrdDeletedTime());
        assertTrue(judicialUserProfile.isActiveFlag());

        judicialUserProfile.setActiveFlag(false);
        assertFalse(judicialUserProfile.isActiveFlag());
    }

    @Test
    void test_objects_JudicialUserProfile_Inactive() {

        Date currentDate = new Date();
        LocalDateTime dateTime = LocalDateTime.now();
        JudicialUserProfile judicialUserProfile = createJudicialUserProfileInactiveMock(currentDate, dateTime, PERID_1);

        assertEquals(PERID_1, judicialUserProfile.getPerId());
        assertEquals("personalCode_1", judicialUserProfile.getPersonalCode());
        assertEquals("knownAs", judicialUserProfile.getKnownAs());
        assertEquals("surname", judicialUserProfile.getSurName());
        assertEquals("fullName", judicialUserProfile.getFullName());
        assertEquals("postNominals", judicialUserProfile.getPostNominals());
        assertEquals("workpatterns", judicialUserProfile.getWorkPattern());
        assertEquals("some@hmcts.net", judicialUserProfile.getEjudiciaryEmail());
        assertEquals("28-04-2022 00:00:00", judicialUserProfile.getJoiningDate());
        assertEquals("28-06-2022 00:00:00", judicialUserProfile.getLastWorkingDate());
        assertEquals("28-05-2022 00:00:00", judicialUserProfile.getExtractedDate());
        assertEquals("779321b3-3170-44a0-bc7d-b4decc2aea10", judicialUserProfile.getObjectId());
        assertFalse(judicialUserProfile.isJudge());
        assertFalse(judicialUserProfile.isPanelMember());
        assertTrue(judicialUserProfile.isMagistrate());
        assertEquals("28-04-2022 00:00:00", judicialUserProfile.getMrdCreatedTime());
        assertEquals("28-05-2022 00:00:00", judicialUserProfile.getMrdUpdatedTime());
        assertEquals("28-06-2022 00:00:00", judicialUserProfile.getMrdDeletedTime());
        assertFalse(judicialUserProfile.isActiveFlag());
    }
}
