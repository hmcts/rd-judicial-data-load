package uk.gov.hmcts.reform.juddata.camel.mapper;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialBaseLocationType;


@Slf4j
@Component
public class JudicialBaseLocationRowTypeMapper {

    public Map<String, Object> getMap(JudicialBaseLocationType location) {

        Map<String, Object> locationRow = new HashMap<>();
        locationRow.put("base_location_id", location.getBase_location_id());
        locationRow.put("court_name", location.getCourt_name());
        locationRow.put("court_type", location.getCourt_type());
        locationRow.put("circuit", location.getCircuit());
        locationRow.put("area_of_expertise", location.getArea());
        locationRow.put("national_court_code", location.getNational_court_code());
        return  locationRow;
    }

}
