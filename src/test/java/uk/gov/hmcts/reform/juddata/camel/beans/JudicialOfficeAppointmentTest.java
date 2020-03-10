package uk.gov.hmcts.reform.juddata.camel.beans;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper.createCurrentLocalDate;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper.createJudicialOfficeAppointmentMockMock;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper.getDateFormatter;

import java.time.LocalDate;
import org.junit.Test;

public class JudicialOfficeAppointmentTest {


    @Test
    public void  test_objects_JudicialOfficeAppointment_correctly() {

        String currentDateInString = createCurrentLocalDate();

        JudicialOfficeAppointment judicialOfficeAppointment = createJudicialOfficeAppointmentMockMock(currentDateInString);
        assertEquals("elinksid_1", judicialOfficeAppointment.getElinksId());
        assertEquals("roleId_1", judicialOfficeAppointment.getRoleId());
        assertEquals("contractTypeId_1", judicialOfficeAppointment.getContractType());
        assertEquals("baseLocationId_1", judicialOfficeAppointment.getBaseLocationId());
        assertEquals("regionId_1", judicialOfficeAppointment.getRegionId());
        assertEquals(true, judicialOfficeAppointment.getIsPrincipalAppointment());
        assertEquals(LocalDate.parse(currentDateInString, getDateFormatter()), judicialOfficeAppointment.getStartDate());
        assertEquals(LocalDate.parse(currentDateInString, getDateFormatter()), judicialOfficeAppointment.getEndDate());
        assertEquals(true, judicialOfficeAppointment.isActiveFlag());
        assertEquals(currentDateInString, judicialOfficeAppointment.getExtractedDate());
    }
}
