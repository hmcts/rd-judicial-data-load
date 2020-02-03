package uk.gov.hmcts.reform.juddata.camel.mapper;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialUserRoleType;

@Slf4j
@Component
public class JudicialRoleTypeRowMapper {

    public Map<String, Object> getMap(JudicialUserRoleType role) {

        Map<String, Object> roleRow = new HashMap<>();
        roleRow.put("role_id", role.getRoleId());
        roleRow.put("role_desc_en", role.getRoleDescEn());
        roleRow.put("role_desc_cy", role.getRoleDescCy());
        return  roleRow;
    }

}
