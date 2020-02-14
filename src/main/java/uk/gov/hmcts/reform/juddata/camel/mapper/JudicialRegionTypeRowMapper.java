package uk.gov.hmcts.reform.juddata.camel.mapper;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialRegionType;

@Slf4j
@Component
public class JudicialRegionTypeRowMapper {

    public Map<String, Object> getMap(JudicialRegionType regionType) {

        Map<String, Object> roleRow = new HashMap<>();
        roleRow.put("region_id", regionType.getRegionId());
        roleRow.put("region_desc_en", regionType.getRegionDescEn());
        roleRow.put("region_desc_cy", regionType.getRegionDescCy());
        return  roleRow;
    }

}
