package uk.gov.hmcts.reform.juddata.camel.mapper;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserRoleType;

@Component
public class JudicialRoleTypeRowMapper implements IMapper {

    public Map<String, Object> getMap(Object roleObject) {

        JudicialUserRoleType role = (JudicialUserRoleType) roleObject;

        Map<String, Object> roleRow = new HashMap<>();
        roleRow.put("role_id", role.getRoleId());
        roleRow.put("role_desc_en", role.getRoleDescEn());
        roleRow.put("role_desc_cy", role.getRoleDescCy());
        return  roleRow;
    }
}
