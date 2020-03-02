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

        JudicialOfficeAppointment judicialOfficeAppointment = createJudicialOfficeAppointmentMockMock(createCurrentLocalDate());
        assertEquals(judicialOfficeAppointment.getElinksId(), "elinksid_1");
        assertEquals(judicialOfficeAppointment.getRoleId(), "roleId_1");
        assertEquals(judicialOfficeAppointment.getContractType(),"contractTypeId_1");
        assertEquals(judicialOfficeAppointment.getBaseLocationId(), "baseLocationId_1");
        assertEquals(judicialOfficeAppointment.getRegionId(), "regionId_1");
        assertEquals(judicialOfficeAppointment.getIsPrincipalAppointment(), true);
        String currentDateString = createCurrentLocalDate();
        assertEquals(judicialOfficeAppointment.getStartDate(), LocalDate.parse(currentDateString, getDateFormatter()));
        assertEquals(judicialOfficeAppointment.getEndDate(), LocalDate.parse(currentDateString, getDateFormatter()));
        assertEquals(judicialOfficeAppointment.isActiveFlag(), true);
        assertEquals(judicialOfficeAppointment.getExtractedDate(),currentDateString);
    }
}
