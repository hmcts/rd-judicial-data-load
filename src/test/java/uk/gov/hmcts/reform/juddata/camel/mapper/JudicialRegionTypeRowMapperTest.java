package uk.gov.hmcts.reform.juddata.camel.mapper;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialRegionType;

import java.util.Map;
import org.junit.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialRegionType;

public class JudicialRegionTypeRowMapperTest {

    JudicialRegionTypeRowMapper judicialRegionTypeRowMapper = new JudicialRegionTypeRowMapper();

    @Test
    @SuppressWarnings("unchecked")
    public void should_return_JudicialRegionType_response() {

        JudicialRegionType judicialRegionType = createJudicialRegionType();
        Map<String, Object>  response = judicialRegionTypeRowMapper.getMap(judicialRegionType);

        assertEquals("regionId", response.get("region_id"));
        assertEquals("region_desc_en", response.get("region_desc_en"));
        assertEquals("region_desc_cy", response.get("region_desc_cy"));
    }


} 
