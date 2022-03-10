package uk.gov.hmcts.reform.juddata.camel.binder;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.DATE_FORMAT;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.PERID_1;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialOfficeAppointmentMock;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateTimeWithFormat;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateWithFormat;

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
        assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), judicialOfficeAppointment.getStartDate());
        assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), judicialOfficeAppointment.getEndDate());
        assertEquals(getDateTimeWithFormat(dateTime), judicialOfficeAppointment.getExtractedDate());
        assertTrue(judicialOfficeAppointment.getIsPrincipalAppointment());
        assertTrue(judicialOfficeAppointment.isActiveFlag());

        judicialOfficeAppointment.setActiveFlag(false);
        judicialOfficeAppointment.setIsPrincipalAppointment(false);
        assertFalse(judicialOfficeAppointment.isActiveFlag());
        assertFalse(judicialOfficeAppointment.getIsPrincipalAppointment());
        assertEquals("779321b3-3170-44a0-bc7d-b4decc2aea10", judicialOfficeAppointment.getObjectId());
        assertEquals("Magistrate", judicialOfficeAppointment.getAppointment());
        assertEquals("1", judicialOfficeAppointment.getAppointmentType());
        assertEquals("primaryLocation_1", judicialOfficeAppointment.getPrimaryLocation());
        assertEquals("secondaryLocation_1", judicialOfficeAppointment.getSecondaryLocation());
        assertEquals("tertiaryLocation_1", judicialOfficeAppointment.getTertiaryLocation());
        assertEquals("2022-05-03 00:00:00", judicialOfficeAppointment.getMrdCreatedTime());
        assertEquals("2022-05-04 00:00:00", judicialOfficeAppointment.getMrdUpdatedTime());
        assertEquals("2022-05-05 00:00:00", judicialOfficeAppointment.getMrdDeletedTime());

    }
}
