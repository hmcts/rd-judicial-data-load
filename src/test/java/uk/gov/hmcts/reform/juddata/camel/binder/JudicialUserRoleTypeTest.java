package uk.gov.hmcts.reform.juddata.camel.binder;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserRoleType;

class JudicialUserRoleTypeTest {

    @Test
    @SuppressWarnings("unchecked")
    void test_objects_JudicialUserRoleTypeTest_correctly() {
        JudicialUserRoleType judicialUserRoleType = createJudicialUserRoleType();

        assertEquals("roleDescCy", judicialUserRoleType.getRoleDescCy());
        assertEquals("roleDescEn", judicialUserRoleType.getRoleDescEn());
        assertEquals("roleId", judicialUserRoleType.getRoleId());
    }

}
