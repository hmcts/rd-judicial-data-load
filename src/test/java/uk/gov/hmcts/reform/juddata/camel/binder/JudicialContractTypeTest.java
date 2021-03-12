package uk.gov.hmcts.reform.juddata.camel.binder;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialContractType;

class JudicialContractTypeTest {

    JudicialContractType judicialContractType = createJudicialContractType();


    @Test
    @SuppressWarnings("unchecked")
    void test_objects_JudicialContractType_correctly() {

        assertEquals("contractTypeDescCy", judicialContractType.getContractTypeDescCy());
        assertEquals("contractTypeDescEn", judicialContractType.getContractTypeDescEn());
        assertEquals("contractTypeId", judicialContractType.getContractTypeId());

    }

}
