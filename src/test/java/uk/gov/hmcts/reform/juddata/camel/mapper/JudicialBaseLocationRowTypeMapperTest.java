package uk.gov.hmcts.reform.juddata.camel.mapper;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialBaseLocationType;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.DataLoadUtil.getDateTimeStamp;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialBaseLocationMock;

class JudicialBaseLocationRowTypeMapperTest {


    @Test
    void should_return_JudicialBaseLocationType_response() {

        JudicialBaseLocationRowTypeMapper judicialBaseLocationRowTypeMapper = new JudicialBaseLocationRowTypeMapper();

        JudicialBaseLocationType judicialBaseLocationType = createJudicialBaseLocationMock();

        Map<String, Object> response = judicialBaseLocationRowTypeMapper.getMap(judicialBaseLocationType);
        assertEquals("area", response.get("area_of_expertise"));
        assertEquals("baseLocationId", response.get("base_location_id"));
        assertEquals("circuit", response.get("circuit"));
        assertEquals("courtName", response.get("court_name"));
        assertEquals("courtType", response.get("court_type"));
        assertEquals(getDateTimeStamp("2020-01-01 00:00:00"), response.get("mrd_created_time"));
        assertEquals(getDateTimeStamp("2020-01-01 00:00:00"), response.get("mrd_updated_time"));
        assertEquals(getDateTimeStamp("2020-01-01 00:00:00"), response.get("mrd_deleted_time"));
    }

}