package uk.gov.hmcts.reform.juddata.camel.binder;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper.createCurrentLocalDate;

import org.junit.Test;

public class JudicialBaseLocationTypeTest {

    @Test
    @SuppressWarnings("unchecked")
    public  void  test_objects_JudicialBaseLocationType_correctly() {

        String currentDateInString = createCurrentLocalDate();
        JudicialBaseLocationType judicialBaseLocationType = createJudicialOfficeAppointmentMock();
        assertEquals("area", judicialBaseLocationType.getArea());
        assertEquals("baseLocationId", judicialBaseLocationType.getBaseLocationId());
        assertEquals("circuit", judicialBaseLocationType.getCircuit());
        assertEquals("courtName", judicialBaseLocationType.getCourtName());
        assertEquals("courtType", judicialBaseLocationType.getCourtType());
    }

    public  JudicialBaseLocationType createJudicialOfficeAppointmentMock() {
        JudicialBaseLocationType judicialBaseLocationType = new JudicialBaseLocationType();

        judicialBaseLocationType.setArea("area");
        judicialBaseLocationType.setBaseLocationId("baseLocationId");
        judicialBaseLocationType.setCircuit("circuit");
        judicialBaseLocationType.setCourtName("courtName");
        judicialBaseLocationType.setCourtType("courtType");
        return  judicialBaseLocationType;
    }
}
