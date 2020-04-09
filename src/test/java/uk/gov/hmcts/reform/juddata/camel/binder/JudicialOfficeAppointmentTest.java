package uk.gov.hmcts.reform.juddata.camel.binder;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialOfficeAppointmentMockMock;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateTimeWithFormat;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateWithFormat;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.DATE_FORMAT;

import java.time.LocalDateTime;
import java.util.Date;

import org.junit.Test;

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
        assertEquals(true, judicialOfficeAppointment.getIsPrincipalAppointment());
        assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), judicialOfficeAppointment.getStartDate());
        assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), judicialOfficeAppointment.getEndDate());
        assertEquals(true, judicialOfficeAppointment.isActiveFlag());
        assertEquals(getDateTimeWithFormat(dateTime), judicialOfficeAppointment.getExtractedDate());
    }
}
