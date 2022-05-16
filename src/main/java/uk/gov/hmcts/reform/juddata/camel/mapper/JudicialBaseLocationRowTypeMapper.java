package uk.gov.hmcts.reform.juddata.camel.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.mapper.IMapper;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialBaseLocationType;
import uk.gov.hmcts.reform.juddata.camel.util.CommonUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@Component
public class JudicialBaseLocationRowTypeMapper implements IMapper {

    public Map<String, Object> getMap(Object location) {

        JudicialBaseLocationType locationType = (JudicialBaseLocationType) location;

        Map<String, Object> locationRow = new HashMap<>();
        locationRow.put("base_location_id", locationType.getBaseLocationId());
        locationRow.put("court_name", locationType.getCourtName());
        locationRow.put("court_type", locationType.getCourtType());
        locationRow.put("circuit", locationType.getCircuit());
        locationRow.put("area_of_expertise", locationType.getArea());

        Optional<String> mrdCreatedTimeOptional =
                Optional.ofNullable(locationType.getMrdCreatedTime()).filter(Predicate.not(String::isEmpty));
        locationRow.put("mrd_created_time", mrdCreatedTimeOptional.map(CommonUtils::getDateTimeStamp)
                .orElse(null));

        Optional<String> mrdUpdatedTimeOptional =
                Optional.ofNullable(locationType.getMrdUpdatedTime()).filter(Predicate.not(String::isEmpty));
        locationRow.put("mrd_updated_time", mrdUpdatedTimeOptional.map(CommonUtils::getDateTimeStamp)
                .orElse(null));

        Optional<String> mrdDeletedTimeOptional =
                Optional.ofNullable(locationType.getMrdDeletedTime()).filter(Predicate.not(String::isEmpty));
        locationRow.put("mrd_deleted_time", mrdDeletedTimeOptional.map(CommonUtils::getDateTimeStamp)
                .orElse(null));

        return  locationRow;
    }

}
