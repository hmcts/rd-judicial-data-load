package uk.gov.hmcts.reform.juddata.camel.binder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.DATE_FORMAT;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.PERID_1;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserProfileMock;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateTimeWithFormat;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateWithFormat;

class JudicialUserProfileTest {

    @Test
    void test_objects_JudicialUserProfile_correctly() {

        Date currentDate = new Date();
        LocalDateTime dateTime = LocalDateTime.now();
        JudicialUserProfile judicialUserProfile = createJudicialUserProfileMock(currentDate, dateTime, PERID_1);

        Assertions.assertEquals(PERID_1, judicialUserProfile.getPerId());
        Assertions.assertEquals("personalCode_1", judicialUserProfile.getPersonalCode());
        Assertions.assertEquals("knownAs", judicialUserProfile.getKnownAs());
        Assertions.assertEquals("surname", judicialUserProfile.getSurName());
        Assertions.assertEquals("fullName", judicialUserProfile.getFullName());
        Assertions.assertEquals("postNominals", judicialUserProfile.getPostNominals());
        Assertions.assertEquals("workpatterns", judicialUserProfile.getWorkPattern());
        Assertions.assertEquals("some@hmcts.net", judicialUserProfile.getEjudiciaryEmail());
        Assertions.assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), judicialUserProfile.getJoiningDate());
        Assertions.assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), judicialUserProfile.getLastWorkingDate());
        Assertions.assertEquals(getDateTimeWithFormat(dateTime), judicialUserProfile.getExtractedDate());
        Assertions.assertEquals("779321b3-3170-44a0-bc7d-b4decc2aea10", judicialUserProfile.getObjectId());
        Assertions.assertTrue(judicialUserProfile.isActiveFlag());

        judicialUserProfile.setActiveFlag(false);
        Assertions.assertFalse(judicialUserProfile.isActiveFlag());
    }
}
