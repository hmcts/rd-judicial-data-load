package uk.gov.hmcts.reform.juddata.camel.binder;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper.createJudicialUserRoleType;

import org.junit.Test;

public class JudicialUserRoleTypeTest {

    @Test
    @SuppressWarnings("unchecked")
    public void test_objects_JudicialUserRoleTypeTest_correctly() {
        JudicialUserRoleType judicialUserRoleType = createJudicialUserRoleType();

        assertEquals("roleDescCy", judicialUserRoleType.getRoleDescCy());
        assertEquals("roleDescEn", judicialUserRoleType.getRoleDescEn());
        assertEquals("roleId", judicialUserRoleType.getRoleId());
    }

}
