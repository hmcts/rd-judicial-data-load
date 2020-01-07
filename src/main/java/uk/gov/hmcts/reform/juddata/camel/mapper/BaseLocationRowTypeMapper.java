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
        locationRow.put("base_location_id", location.getBase_location_Id());
        locationRow.put("court_name", location.getCourt_name());
        locationRow.put("court_type", location.getCourt_type());
        locationRow.put("circuit", location.getCircuit());
        locationRow.put("area_of_expertise", location.getArea());
        return  locationRow;
    }

}
