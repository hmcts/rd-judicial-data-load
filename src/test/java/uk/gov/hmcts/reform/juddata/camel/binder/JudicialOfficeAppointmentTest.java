package uk.gov.hmcts.reform.juddata.camel.binder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.DATE_FORMAT;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialOfficeAppointmentMockMock;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateTimeWithFormat;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateWithFormat;

import java.time.LocalDateTime;
import java.util.Date;

import org.junit.jupiter.api.Test;

public class JudicialOfficeAppointmentTest {

    @Test
    public void test_objects_JudicialOfficeAppointment_correctly() {
        Date currentDate = new Date();
        LocalDateTime dateTime = LocalDateTime.now();
        JudicialOfficeAppointment judicialOfficeAppointment = createJudicialOfficeAppointmentMockMock(currentDate,
                dateTime);
        assertEquals("elinksid_1", judicialOfficeAppointment.getElinksId());
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
