package uk.gov.hmcts.reform.juddata.camel.binder;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialContractType;

import org.junit.Test;

public class JudicialContractTypeTest {

    JudicialContractType judicialContractType = createJudicialContractType();


    @Test
    @SuppressWarnings("unchecked")
    public  void  test_objects_JudicialContractType_correctly() {

        assertEquals("contractTypeDescCy", judicialContractType.getContractTypeDescCy());
        assertEquals("contractTypeDescEn", judicialContractType.getContractTypeDescEn());
        assertEquals("contractTypeId",  judicialContractType.getContractTypeId());

    }

}
