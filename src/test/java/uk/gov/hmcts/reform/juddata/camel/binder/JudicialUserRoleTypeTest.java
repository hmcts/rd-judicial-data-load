package uk.gov.hmcts.reform.juddata.camel.binder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserRoleType;

class JudicialUserRoleTypeTest {

    @Test
    void test_objects_JudicialUserRoleTypeTest_correctly() {
        JudicialUserRoleType judicialUserRoleType = createJudicialUserRoleType();

        Assertions.assertEquals("46804", judicialUserRoleType.getPerId());
        Assertions.assertEquals("Family Course Tutor (JC)", judicialUserRoleType.getTitle());
        Assertions.assertEquals("Nationwide", judicialUserRoleType.getLocation());
        Assertions.assertEquals("2018-05-02 00:00:00.000", judicialUserRoleType.getStartDate());
        Assertions.assertEquals("2022-05-01 00:00:00", judicialUserRoleType.getEndDate());
    }

}
