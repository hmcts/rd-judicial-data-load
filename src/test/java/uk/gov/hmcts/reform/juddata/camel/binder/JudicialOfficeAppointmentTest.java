package uk.gov.hmcts.reform.juddata.camel.binder;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.PERID_1;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialOfficeAppointmentMock;

class JudicialOfficeAppointmentTest {

    @Test
    void test_objects_JudicialOfficeAppointment_correctly() {
        Date currentDate = new Date();
        LocalDateTime dateTime = LocalDateTime.now();
        JudicialOfficeAppointment judicialOfficeAppointment = createJudicialOfficeAppointmentMock(currentDate,
                dateTime, PERID_1);
        assertEquals(PERID_1, judicialOfficeAppointment.getPerId());
        assertEquals("baseLocationId_1", judicialOfficeAppointment.getBaseLocationId());
        assertEquals("regionId_1", judicialOfficeAppointment.getRegionId());
        assertEquals("111", judicialOfficeAppointment.getPersonalCode());
        assertEquals("28-04-2022 00:00:00", judicialOfficeAppointment.getStartDate());
        assertEquals("28-04-2022 00:00:00", judicialOfficeAppointment.getEndDate());
        assertEquals("28-04-2022 00:00:00", judicialOfficeAppointment.getExtractedDate());
        assertTrue(judicialOfficeAppointment.getIsPrincipalAppointment());
        assertTrue(judicialOfficeAppointment.isActiveFlag());

        judicialOfficeAppointment.setActiveFlag(false);
        judicialOfficeAppointment.setIsPrincipalAppointment(false);
        assertFalse(judicialOfficeAppointment.isActiveFlag());
        assertFalse(judicialOfficeAppointment.getIsPrincipalAppointment());
        assertEquals("779321b3-3170-44a0-bc7d-b4decc2aea10", judicialOfficeAppointment.getObjectId());
        assertEquals("Magistrate", judicialOfficeAppointment.getAppointment());
        assertEquals("1", judicialOfficeAppointment.getAppointmentType());
        assertEquals("primary_location_1", judicialOfficeAppointment.getPrimaryLocation());
        assertEquals("secondary_location_1", judicialOfficeAppointment.getSecondaryLocation());
        assertEquals("tertiary_location_1", judicialOfficeAppointment.getTertiaryLocation());
        assertEquals("28-04-2022 00:00:00",judicialOfficeAppointment.getMrdCreatedTime());
        assertEquals("28-05-2022 00:00:00",judicialOfficeAppointment.getMrdUpdatedTime());
        assertEquals("28-06-2022 00:00:00",judicialOfficeAppointment.getMrdDeletedTime());


    }
}