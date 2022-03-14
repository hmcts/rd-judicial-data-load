package uk.gov.hmcts.reform.juddata.camel.binder;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createCurrentLocalDate;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateWithMillisValue;

class JudicialBaseLocationTypeTest {

    private static String mrdCreatedTimeValue;
    private static String mrdUpdatedTimeValue;
    private static String mrdDeletedTimeValue;

    @BeforeAll
    public static void init(){
        mrdCreatedTimeValue = getDateWithMillisValue();
        mrdUpdatedTimeValue = getDateWithMillisValue();
        mrdDeletedTimeValue = getDateWithMillisValue();
    }

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
        assertEquals(mrdCreatedTimeValue, judicialBaseLocationType.getMrdCreatedTime());
        assertEquals(mrdUpdatedTimeValue, judicialBaseLocationType.getMrdUpdatedTime());
        assertEquals(mrdDeletedTimeValue, judicialBaseLocationType.getMrdDeletedTime());

    }

    public  JudicialBaseLocationType createJudicialBaseLocationTypeMock() {
        JudicialBaseLocationType judicialBaseLocationType = new JudicialBaseLocationType();

        judicialBaseLocationType.setArea("area");
        judicialBaseLocationType.setBaseLocationId("baseLocationId");
        judicialBaseLocationType.setCircuit("circuit");
        judicialBaseLocationType.setCourtName("courtName");
        judicialBaseLocationType.setCourtType("courtType");
        judicialBaseLocationType.setMrdCreatedTime(mrdCreatedTimeValue);
        judicialBaseLocationType.setMrdUpdatedTime(mrdUpdatedTimeValue);
        judicialBaseLocationType.setMrdDeletedTime(mrdDeletedTimeValue);
        return  judicialBaseLocationType;
    }
}
