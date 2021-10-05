package uk.gov.hmcts.reform.juddata.camel.mapper;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.mapper.IMapper;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserRoleType;

@Component
public class JudicialRoleTypeRowMapper implements IMapper {

    public Map<String, Object> getMap(Object roleObject) {

        JudicialUserRoleType role = (JudicialUserRoleType) roleObject;

        Map<String, Object> roleRow = new HashMap<>();
        roleRow.put("per_Id", role.getPerId());
        roleRow.put("title", role.getTitle());
        roleRow.put("location", role.getLocation());
        roleRow.put("start_date", role.getStartDate());
        roleRow.put("end_date", role.getEndDate());
        return  roleRow;
    }
}

