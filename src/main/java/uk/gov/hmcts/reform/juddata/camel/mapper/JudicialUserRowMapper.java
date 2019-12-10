package uk.gov.hmcts.reform.juddata.camel.mapper;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialUserProfile;

@Slf4j
@Component
public class JudicialUserRowMapper {


    public Map<String, Object> getMap(JudicialUserProfile user) {

        Map<String, Object> judUserRow = new HashMap<>();
        judUserRow.put("sno", user.getSno());
        judUserRow.put("firstName", user.getFirstName());
        judUserRow.put("lastName", user.getLastName());
        judUserRow.put("circuit", user.getCircuit());
        judUserRow.put("area", user.getArea());
       return  judUserRow;
    }

}
