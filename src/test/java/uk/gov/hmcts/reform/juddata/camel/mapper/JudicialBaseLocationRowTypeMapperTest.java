package uk.gov.hmcts.reform.juddata.camel.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialBaseLocationType;

import java.util.Map;

import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialOfficeAppointmentMock;

class JudicialBaseLocationRowTypeMapperTest {

    @Test
    void should_return_JudicialBaseLocationType_response() {

        JudicialBaseLocationRowTypeMapper judicialBaseLocationRowTypeMapper = new JudicialBaseLocationRowTypeMapper();

        JudicialBaseLocationType judicialBaseLocationType = createJudicialOfficeAppointmentMock();

        Map<String, Object> response = judicialBaseLocationRowTypeMapper.getMap(judicialBaseLocationType);
        Assertions.assertEquals("area", response.get("area_of_expertise"));
        Assertions.assertEquals("baseLocationId", response.get("base_location_id"));
        Assertions.assertEquals("circuit", response.get("circuit"));
        Assertions.assertEquals("courtName", response.get("court_name"));
        Assertions.assertEquals("courtType", response.get("court_type"));
    }

}