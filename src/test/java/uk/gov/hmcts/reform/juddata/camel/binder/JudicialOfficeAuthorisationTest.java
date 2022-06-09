package uk.gov.hmcts.reform.juddata.camel.binder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialOfficeAuthorisation;

class JudicialOfficeAuthorisationTest {

    @Test
    void test_pojo_JudicialOfficeAuthorisationTest() {
        String date = "2017-10-01 00:00:00.000";
        JudicialOfficeAuthorisation judicialOfficeAuthorisation = createJudicialOfficeAuthorisation(date);
        assertEquals("1", judicialOfficeAuthorisation.getPerId());
        assertEquals("jurisdiction", judicialOfficeAuthorisation.getJurisdiction());
        assertEquals(date, judicialOfficeAuthorisation.getStartDate());
        assertEquals(date, judicialOfficeAuthorisation.getEndDate());
        assertEquals(Long.valueOf("12345"), judicialOfficeAuthorisation.getTicketId());
        assertEquals("lowerLevel", judicialOfficeAuthorisation.getLowerLevel());
        assertEquals("111", judicialOfficeAuthorisation.getPersonalCode());
        assertEquals("779321b3-3170-44a0-bc7d-b4decc2aea10", judicialOfficeAuthorisation.getObjectId());
    }
}
