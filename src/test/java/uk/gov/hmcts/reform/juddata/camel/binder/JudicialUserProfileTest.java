package uk.gov.hmcts.reform.juddata.camel.binder;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.DATE_FORMAT;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.PERID_1;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserProfileMock;
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
        assertEquals("appointment", judicialUserProfile.getAppointment());
        assertEquals("knownAs", judicialUserProfile.getKnownAs());
        assertEquals("surname", judicialUserProfile.getSurName());
        assertEquals("fullName", judicialUserProfile.getFullName());
        assertEquals("postNominals", judicialUserProfile.getPostNominals());
        assertEquals("appointmentTypeId", judicialUserProfile.getAppointmentTypeId());
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
}
