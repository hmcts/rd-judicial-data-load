package uk.gov.hmcts.reform.juddata.camel.mapper;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.BaseLocationType;


@Slf4j
@Component
public class BaseLocationRowTypeMapper {

    public Map<String, Object> getMap(BaseLocationType location) {

        Map<String, Object> locationRow = new HashMap<>();
        locationRow.put("base_location_id", location.getBaseLocationId());
        locationRow.put("court_name", location.getCourtName());
        locationRow.put("court_type", location.getCourtType());
        locationRow.put("circuit", location.getCircuit());
        locationRow.put("area_of_expertise", location.getArea());
        return  locationRow;
    }

}
