package uk.gov.hmcts.reform.juddata.camel.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.mapper.IMapper;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialRegionType;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JudicialRegionTypeRowMapper implements IMapper {

    public Map<String, Object> getMap(Object regionTypeObject) {

        JudicialRegionType regionType = (JudicialRegionType) regionTypeObject;

        Map<String, Object> roleRow = new HashMap<>();
        roleRow.put("region_id", regionType.getRegionId());
        roleRow.put("region_desc_en", regionType.getRegionDescEn());
        roleRow.put("region_desc_cy", regionType.getRegionDescCy());
        return  roleRow;
    }

}
