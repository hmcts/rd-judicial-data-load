package uk.gov.hmcts.reform.juddata.camel.beans;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper.createJudicialContractType;

import org.junit.Test;

public class JudicialContractTypeTest {

    JudicialContractType judicialContractType = createJudicialContractType();


    @Test
    @SuppressWarnings("unchecked")
    public  void  test_objects_JudicialContractType_correctly() {

        assertEquals(judicialContractType.getContractTypeDescCy(),"contractTypeDescCy");
        assertEquals(judicialContractType.getContractTypeDescEn(),"contractTypeDescEn");
        assertEquals(judicialContractType.getContractTypeId(),"contractTypeId");

    }

}
