package uk.gov.hmcts.reform.juddata.camel.mapper;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialRegionType;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialRegionType;

class JudicialRegionTypeRowMapperTest {

    JudicialRegionTypeRowMapper judicialRegionTypeRowMapper = new JudicialRegionTypeRowMapper();

    @Test
    @SuppressWarnings("unchecked")
    void should_return_JudicialRegionType_response() {

        JudicialRegionType judicialRegionType = createJudicialRegionType();
        Map<String, Object> response = judicialRegionTypeRowMapper.getMap(judicialRegionType);

        assertEquals("regionId", response.get("region_id"));
        assertEquals("region_desc_en", response.get("region_desc_en"));
        assertEquals("region_desc_cy", response.get("region_desc_cy"));
    }


} 
