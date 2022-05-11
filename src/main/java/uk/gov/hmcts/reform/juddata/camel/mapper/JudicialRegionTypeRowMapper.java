package uk.gov.hmcts.reform.juddata.camel.mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.mapper.IMapper;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialRegionType;
import uk.gov.hmcts.reform.juddata.camel.util.CommonUtils;

@Slf4j
@Component
public class JudicialRegionTypeRowMapper implements IMapper {

    public Map<String, Object> getMap(Object regionTypeObject) {

        JudicialRegionType regionType = (JudicialRegionType) regionTypeObject;

        Map<String, Object> roleRow = new HashMap<>();
        roleRow.put("region_id", regionType.getRegionId());
        roleRow.put("region_desc_en", regionType.getRegionDescEn());
        roleRow.put("region_desc_cy", regionType.getRegionDescCy());

        Optional<String> mrdCreatedTimeOptional =
                Optional.ofNullable(regionType.getMrdCreatedTime()).filter(Predicate.not(String::isEmpty));
        roleRow.put("mrd_created_time", mrdCreatedTimeOptional.map(CommonUtils::getDateTimeStamp)
                .orElse(null));

        Optional<String> mrdUpdatedTimeOptional =
                Optional.ofNullable(regionType.getMrdUpdatedTime()).filter(Predicate.not(String::isEmpty));
        roleRow.put("mrd_updated_time", mrdUpdatedTimeOptional.map(CommonUtils::getDateTimeStamp)
                .orElse(null));

        Optional<String> mrdDeletedTimeOptional =
                Optional.ofNullable(regionType.getMrdDeletedTime()).filter(Predicate.not(String::isEmpty));
        roleRow.put("mrd_deleted_time", mrdDeletedTimeOptional.map(CommonUtils::getDateTimeStamp)
                .orElse(null));
        return  roleRow;
    }

}
