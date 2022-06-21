package uk.gov.hmcts.reform.juddata.camel.binder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createCurrentLocalDate;

class JudicialBaseLocationTypeTest {

    @Test
    void  test_objects_JudicialBaseLocationType_correctly() {

        String currentDateInString = createCurrentLocalDate();
        JudicialBaseLocationType judicialBaseLocationType = createJudicialBaseLocationTypeMock();
        assertEquals("area", judicialBaseLocationType.getArea());
        assertEquals("baseLocationId", judicialBaseLocationType.getBaseLocationId());
        assertEquals("circuit", judicialBaseLocationType.getCircuit());
        assertEquals("courtName", judicialBaseLocationType.getCourtName());
        assertEquals("courtType", judicialBaseLocationType.getCourtType());
    }

    public  JudicialBaseLocationType createJudicialBaseLocationTypeMock() {
        JudicialBaseLocationType judicialBaseLocationType = new JudicialBaseLocationType();

        judicialBaseLocationType.setArea("area");
        judicialBaseLocationType.setBaseLocationId("baseLocationId");
        judicialBaseLocationType.setCircuit("circuit");
        judicialBaseLocationType.setCourtName("courtName");
        judicialBaseLocationType.setCourtType("courtType");
        return  judicialBaseLocationType;
    }
}
