package uk.gov.hmcts.reform.juddata.camel.binder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialOfficeAuthorisation;

class JudicialOfficeAuthorisationTest {

    @Test
    void test_pojo_JudicialOfficeAuthorisationTest() {
        String date = "2017-10-01 00:00:00.000";
        JudicialOfficeAuthorisation judicialOfficeAuthorisation = createJudicialOfficeAuthorisation(date);
        Assertions.assertEquals("1", judicialOfficeAuthorisation.getPerId());
        Assertions.assertEquals("jurisdiction", judicialOfficeAuthorisation.getJurisdiction());
        Assertions.assertEquals(date, judicialOfficeAuthorisation.getStartDate());
        Assertions.assertEquals(date, judicialOfficeAuthorisation.getEndDate());
        Assertions.assertEquals(Long.valueOf("12345"), judicialOfficeAuthorisation.getTicketId());
        Assertions.assertEquals("lowerLevel", judicialOfficeAuthorisation.getLowerLevel());
        Assertions.assertEquals("111", judicialOfficeAuthorisation.getPersonalCode());
        Assertions.assertEquals("779321b3-3170-44a0-bc7d-b4decc2aea10", judicialOfficeAuthorisation.getObjectId());
    }
}
