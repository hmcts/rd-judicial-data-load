package uk.gov.hmcts.reform.juddata.camel.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialRegionType;

import java.util.Map;

import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialRegionType;

class JudicialRegionTypeRowMapperTest {

    final JudicialRegionTypeRowMapper judicialRegionTypeRowMapper = new JudicialRegionTypeRowMapper();

    @Test
    void should_return_JudicialRegionType_response() {

        JudicialRegionType judicialRegionType = createJudicialRegionType();
        Map<String, Object> response = judicialRegionTypeRowMapper.getMap(judicialRegionType);

        Assertions.assertEquals("regionId", response.get("region_id"));
        Assertions.assertEquals("region_desc_en", response.get("region_desc_en"));
        Assertions.assertEquals("region_desc_cy", response.get("region_desc_cy"));
    }


} 
