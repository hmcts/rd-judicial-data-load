package uk.gov.hmcts.reform.juddata.camel.binder;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserRoleType;

class JudicialUserRoleTypeTest {

    @Test
    @SuppressWarnings("unchecked")
    void test_objects_JudicialUserRoleTypeTest_correctly() {
        JudicialUserRoleType judicialUserRoleType = createJudicialUserRoleType();

        assertEquals("46804", judicialUserRoleType.getPerId());
        assertEquals("Family Course Tutor (JC)", judicialUserRoleType.getTitle());
        assertEquals("Nationwide", judicialUserRoleType.getLocation());
        assertEquals("2018-05-02 00:00:00.000", judicialUserRoleType.getStartDate());
        assertEquals("2022-05-01 00:00:00", judicialUserRoleType.getEndDate());
        assertEquals("2022-05-03 00:00:00", judicialUserRoleType.getMrdCreatedTime());
        assertEquals("2022-05-04 00:00:00", judicialUserRoleType.getMrdUpdatedTime());
        assertEquals("2022-05-05 00:00:00", judicialUserRoleType.getMrdDeletedTime());
    }

}
