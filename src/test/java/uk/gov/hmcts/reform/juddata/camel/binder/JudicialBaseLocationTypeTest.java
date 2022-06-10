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
        assertEquals("28-04-2022 00:00:00", judicialBaseLocationType.getMrdCreatedTime());
        assertEquals("28-05-2022 00:00:00", judicialBaseLocationType.getMrdUpdatedTime());
        assertEquals("28-06-2022 00:00:00", judicialBaseLocationType.getMrdDeletedTime());

    }

    public  JudicialBaseLocationType createJudicialBaseLocationTypeMock() {
        JudicialBaseLocationType judicialBaseLocationType = new JudicialBaseLocationType();

        judicialBaseLocationType.setArea("area");
        judicialBaseLocationType.setBaseLocationId("baseLocationId");
        judicialBaseLocationType.setCircuit("circuit");
        judicialBaseLocationType.setCourtName("courtName");
        judicialBaseLocationType.setCourtType("courtType");
        judicialBaseLocationType.setMrdCreatedTime("28-04-2022 00:00:00");
        judicialBaseLocationType.setMrdUpdatedTime("28-05-2022 00:00:00");
        judicialBaseLocationType.setMrdDeletedTime("28-06-2022 00:00:00");
        return  judicialBaseLocationType;
    }
}
