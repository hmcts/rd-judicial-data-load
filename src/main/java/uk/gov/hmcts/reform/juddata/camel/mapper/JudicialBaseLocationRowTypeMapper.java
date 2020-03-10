package uk.gov.hmcts.reform.juddata.camel.mapper;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialBaseLocationType;


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
        return  locationRow;
    }

}
