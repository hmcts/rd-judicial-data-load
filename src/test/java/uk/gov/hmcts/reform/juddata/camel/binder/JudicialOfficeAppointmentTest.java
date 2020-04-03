package uk.gov.hmcts.reform.juddata.camel.binder;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper.createJudicialOfficeAppointmentMockMock;

import java.time.LocalDate;

import org.junit.Test;

public class JudicialOfficeAppointmentTest {


    @Test
    public void test_objects_JudicialOfficeAppointment_correctly() {
        LocalDate currentDate = LocalDate.now();
        JudicialOfficeAppointment judicialOfficeAppointment = createJudicialOfficeAppointmentMockMock(currentDate.toString());
        assertEquals("elinksid_1", judicialOfficeAppointment.getElinksId());
        assertEquals("roleId_1", judicialOfficeAppointment.getRoleId());
        assertEquals("contractTypeId_1", judicialOfficeAppointment.getContractType());
        assertEquals("baseLocationId_1", judicialOfficeAppointment.getBaseLocationId());
        assertEquals("regionId_1", judicialOfficeAppointment.getRegionId());
        assertEquals(true, judicialOfficeAppointment.getIsPrincipalAppointment());
        assertEquals(currentDate.toString(), judicialOfficeAppointment.getStartDate());
        assertEquals(currentDate.toString(), judicialOfficeAppointment.getEndDate());
        assertEquals(true, judicialOfficeAppointment.isActiveFlag());
        assertEquals(currentDate.toString(), judicialOfficeAppointment.getExtractedDate());
    }
}
