package uk.gov.hmcts.reform.juddata.camel.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.mapper.IMapper;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialContractType;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JudicialContractTypeRowMapper implements IMapper {

    public Map<String, Object> getMap(Object contractType) {

        var judicialContractType = (JudicialContractType) contractType;

        Map<String, Object> roleRow = new HashMap<>();
        roleRow.put("contract_type_id", judicialContractType.getContractTypeId());
        roleRow.put("contract_type_desc_en", judicialContractType.getContractTypeDescEn());
        roleRow.put("contract_type_desc_cy", judicialContractType.getContractTypeDescCy());
        return  roleRow;
    }

}
