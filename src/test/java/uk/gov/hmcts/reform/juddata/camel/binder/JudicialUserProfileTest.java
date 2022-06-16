package uk.gov.hmcts.reform.juddata.camel.binder;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.DATE_FORMAT;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.PERID_1;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserProfileMock;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserProfileInactiveMock;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateTimeWithFormat;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateWithFormat;

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
        assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), judicialUserProfile.getJoiningDate());
        assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), judicialUserProfile.getLastWorkingDate());
        assertEquals(getDateTimeWithFormat(dateTime), judicialUserProfile.getExtractedDate());
        assertEquals("779321b3-3170-44a0-bc7d-b4decc2aea10", judicialUserProfile.getObjectId());
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
        assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), judicialUserProfile.getJoiningDate());
        assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), judicialUserProfile.getLastWorkingDate());
        assertEquals(getDateTimeWithFormat(dateTime), judicialUserProfile.getExtractedDate());
        assertEquals("779321b3-3170-44a0-bc7d-b4decc2aea10", judicialUserProfile.getObjectId());
        assertFalse(judicialUserProfile.isActiveFlag());
    }
}
