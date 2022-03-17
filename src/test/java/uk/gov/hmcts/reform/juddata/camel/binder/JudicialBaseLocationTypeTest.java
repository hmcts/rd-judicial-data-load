package uk.gov.hmcts.reform.juddata.camel.binder;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createCurrentLocalDate;

class JudicialBaseLocationTypeTest {


    @Test
    @SuppressWarnings("unchecked")
    void  test_objects_JudicialBaseLocationType_correctly() {

        String currentDateInString = createCurrentLocalDate();
        JudicialBaseLocationType judicialBaseLocationType = createJudicialBaseLocationTypeMock();
        assertEquals("area", judicialBaseLocationType.getArea());
        assertEquals("baseLocationId", judicialBaseLocationType.getBaseLocationId());
        assertEquals("circuit", judicialBaseLocationType.getCircuit());
        assertEquals("courtName", judicialBaseLocationType.getCourtName());
        assertEquals("courtType", judicialBaseLocationType.getCourtType());
        assertEquals("2020-01-01 00:00:00", judicialBaseLocationType.getMrdCreatedTime());
        assertEquals("2020-01-01 00:00:00", judicialBaseLocationType.getMrdUpdatedTime());
        assertEquals("2020-01-01 00:00:00", judicialBaseLocationType.getMrdDeletedTime());

    }

    public  JudicialBaseLocationType createJudicialBaseLocationTypeMock() {
        JudicialBaseLocationType judicialBaseLocationType = new JudicialBaseLocationType();

        judicialBaseLocationType.setArea("area");
        judicialBaseLocationType.setBaseLocationId("baseLocationId");
        judicialBaseLocationType.setCircuit("circuit");
        judicialBaseLocationType.setCourtName("courtName");
        judicialBaseLocationType.setCourtType("courtType");
        judicialBaseLocationType.setMrdCreatedTime("2020-01-01 00:00:00");
        judicialBaseLocationType.setMrdUpdatedTime("2020-01-01 00:00:00");
        judicialBaseLocationType.setMrdDeletedTime("2020-01-01 00:00:00");
        return  judicialBaseLocationType;
    }
}
