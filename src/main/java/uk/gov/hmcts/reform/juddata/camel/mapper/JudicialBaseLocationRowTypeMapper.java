package uk.gov.hmcts.reform.juddata.camel.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.mapper.IMapper;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialBaseLocationType;

import java.util.HashMap;
import java.util.Map;

import static uk.gov.hmcts.reform.data.ingestion.camel.util.DataLoadUtil.getDateTimeStamp;


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
        locationRow.put("mrd_created_time", getDateTimeStamp(locationType.getMrdCreatedTime()));
        locationRow.put("mrd_updated_time", getDateTimeStamp(locationType.getMrdUpdatedTime()));
        locationRow.put("mrd_deleted_time", getDateTimeStamp(locationType.getMrdDeletedTime()));

        return  locationRow;
    }

}
