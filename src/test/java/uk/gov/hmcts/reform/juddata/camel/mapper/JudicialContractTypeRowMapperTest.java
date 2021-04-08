package uk.gov.hmcts.reform.juddata.camel.mapper;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialContractType;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialContractType;

class JudicialContractTypeRowMapperTest {


    JudicialContractTypeRowMapper judicialContractTypeRowMapper = new JudicialContractTypeRowMapper();

    @Test
    void should_return_JudicialContractTypeRowMapper_response() {
        JudicialContractType judicialContractType = createJudicialContractType();
        Map<String, Object> response = judicialContractTypeRowMapper.getMap(judicialContractType);

        assertEquals("contractTypeDescCy", response.get("contract_type_desc_cy"));
        assertEquals("contractTypeDescEn", response.get("contract_type_desc_en"));
        assertEquals("contractTypeId", response.get("contract_type_id"));
    }
} 
