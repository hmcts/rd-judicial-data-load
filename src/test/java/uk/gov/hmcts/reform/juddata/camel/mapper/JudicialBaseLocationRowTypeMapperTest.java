package uk.gov.hmcts.reform.juddata.camel.mapper;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialBaseLocationType;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialOfficeAppointmentMock;

class JudicialBaseLocationRowTypeMapperTest {

    @Test
    void should_return_JudicialBaseLocationType_response() {

        JudicialBaseLocationRowTypeMapper judicialBaseLocationRowTypeMapper = new JudicialBaseLocationRowTypeMapper();

        JudicialBaseLocationType judicialBaseLocationType = createJudicialOfficeAppointmentMock();

        Map<String, Object> response = judicialBaseLocationRowTypeMapper.getMap(judicialBaseLocationType);
        assertEquals("area", response.get("area_of_expertise"));
        assertEquals("baseLocationId", response.get("base_location_id"));
        assertEquals("circuit", response.get("circuit"));
        assertEquals("courtName", response.get("court_name"));
        assertEquals("courtType", response.get("court_type"));
    }

}