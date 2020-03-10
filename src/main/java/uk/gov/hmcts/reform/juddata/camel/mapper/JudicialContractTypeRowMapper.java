package uk.gov.hmcts.reform.juddata.camel.mapper;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialContractType;

@Slf4j
@Component
public class JudicialContractTypeRowMapper implements IMapper {

    public Map<String, Object> getMap(Object contractType) {

        JudicialContractType judicialContractType = (JudicialContractType) contractType;

        Map<String, Object> roleRow = new HashMap<>();
        roleRow.put("contract_type_id", judicialContractType.getContractTypeId());
        roleRow.put("contract_type_desc_en", judicialContractType.getContractTypeDescEn());
        roleRow.put("contract_type_desc_cy", judicialContractType.getContractTypeDescCy());
        return  roleRow;
    }

}
