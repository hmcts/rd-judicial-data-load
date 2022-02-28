package uk.gov.hmcts.reform.juddata.camel.binder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createCurrentLocalDate;

class JudicialBaseLocationTypeTest {

    @Test
    void  test_objects_JudicialBaseLocationType_correctly() {

        String currentDateInString = createCurrentLocalDate();
        JudicialBaseLocationType judicialBaseLocationType = createJudicialOfficeAppointmentMock();
        Assertions.assertEquals("area", judicialBaseLocationType.getArea());
        Assertions.assertEquals("baseLocationId", judicialBaseLocationType.getBaseLocationId());
        Assertions.assertEquals("circuit", judicialBaseLocationType.getCircuit());
        Assertions.assertEquals("courtName", judicialBaseLocationType.getCourtName());
        Assertions.assertEquals("courtType", judicialBaseLocationType.getCourtType());
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
