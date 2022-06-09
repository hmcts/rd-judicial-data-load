package uk.gov.hmcts.reform.juddata.camel.binder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserRoleType;

class JudicialUserRoleTypeTest {

    @Test
    void test_objects_JudicialUserRoleTypeTest_correctly() {
        JudicialUserRoleType judicialUserRoleType = createJudicialUserRoleType();

        assertEquals("46804", judicialUserRoleType.getPerId());
        assertEquals("Family Course Tutor (JC)", judicialUserRoleType.getTitle());
        assertEquals("Nationwide", judicialUserRoleType.getLocation());
        assertEquals("28-04-2022 00:00:00", judicialUserRoleType.getStartDate());
        assertEquals("28-06-2022 00:00:00", judicialUserRoleType.getEndDate());
        assertEquals("28-04-2022 00:00:00", judicialUserRoleType.getMrdCreatedTime());
        assertEquals("28-05-2022 00:00:00", judicialUserRoleType.getMrdUpdatedTime());
        assertEquals("28-06-2022 00:00:00", judicialUserRoleType.getMrdDeletedTime());
    }

}
