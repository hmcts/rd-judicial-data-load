package uk.gov.hmcts.reform.juddata.camel.mapper;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialContractType;

@Slf4j
@Component
public class JudicialContractTypeRowMapper {

    public Map<String, Object> getMap(JudicialContractType contractType) {

        Map<String, Object> roleRow = new HashMap<>();
        roleRow.put("contract_type_id", contractType.getContractTypeId());
        roleRow.put("contract_type_desc_en", contractType.getContractTypeDescEn());
        roleRow.put("contract_type_desc_cy", contractType.getContractTypeDescCy());
        return  roleRow;
    }

}
