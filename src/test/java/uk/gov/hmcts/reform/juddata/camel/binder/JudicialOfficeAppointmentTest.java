package uk.gov.hmcts.reform.juddata.camel.binder;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.DATE_FORMAT;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.ELINKSID_1;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialOfficeAppointmentMock;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateTimeWithFormat;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateWithFormat;

class JudicialOfficeAppointmentTest {

    @Test
    void test_objects_JudicialOfficeAppointment_correctly() {
        Date currentDate = new Date();
        LocalDateTime dateTime = LocalDateTime.now();
        JudicialOfficeAppointment judicialOfficeAppointment = createJudicialOfficeAppointmentMock(currentDate,
            dateTime, ELINKSID_1);
        assertEquals(ELINKSID_1, judicialOfficeAppointment.getElinksId());
        assertEquals("roleId_1", judicialOfficeAppointment.getRoleId());
        assertEquals("contractTypeId_1", judicialOfficeAppointment.getContractType());
        assertEquals("baseLocationId_1", judicialOfficeAppointment.getBaseLocationId());
        assertEquals("regionId_1", judicialOfficeAppointment.getRegionId());
        assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), judicialOfficeAppointment.getStartDate());
        assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), judicialOfficeAppointment.getEndDate());
        assertEquals(getDateTimeWithFormat(dateTime), judicialOfficeAppointment.getExtractedDate());
        assertTrue(judicialOfficeAppointment.getIsPrincipalAppointment());
        assertTrue(judicialOfficeAppointment.isActiveFlag());

        judicialOfficeAppointment.setActiveFlag(false);
        judicialOfficeAppointment.setIsPrincipalAppointment(false);
        assertFalse(judicialOfficeAppointment.isActiveFlag());
        assertFalse(judicialOfficeAppointment.getIsPrincipalAppointment());

    }
}
