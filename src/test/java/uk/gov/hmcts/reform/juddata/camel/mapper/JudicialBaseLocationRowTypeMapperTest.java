package uk.gov.hmcts.reform.juddata.camel.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialBaseLocationType;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialBaseLocationMock;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateWithMillisValue;

class JudicialBaseLocationRowTypeMapperTest {
    private static String mrdCreatedTimeValue;
    private static String mrdUpdatedTimeValue;
    private static String mrdDeletedTimeValue;

    @BeforeAll
    public static void init() {
        mrdCreatedTimeValue = getDateWithMillisValue();
        mrdUpdatedTimeValue = getDateWithMillisValue();
        mrdDeletedTimeValue = getDateWithMillisValue();
    }

    @Test
    void should_return_JudicialBaseLocationType_response() {

        JudicialBaseLocationRowTypeMapper judicialBaseLocationRowTypeMapper = new JudicialBaseLocationRowTypeMapper();

        JudicialBaseLocationType judicialBaseLocationType = createJudicialBaseLocationMock(
                mrdCreatedTimeValue,mrdUpdatedTimeValue,mrdDeletedTimeValue);

        Map<String, Object> response = judicialBaseLocationRowTypeMapper.getMap(judicialBaseLocationType);
        assertEquals("area", response.get("area_of_expertise"));
        assertEquals("baseLocationId", response.get("base_location_id"));
        assertEquals("circuit", response.get("circuit"));
        assertEquals("courtName", response.get("court_name"));
        assertEquals("courtType", response.get("court_type"));
        assertEquals(mrdCreatedTimeValue, response.get("mrd_created_time"));
        assertEquals(mrdUpdatedTimeValue, response.get("mrd_updated_time"));
        assertEquals(mrdDeletedTimeValue, response.get("mrd_deleted_time"));
    }

}