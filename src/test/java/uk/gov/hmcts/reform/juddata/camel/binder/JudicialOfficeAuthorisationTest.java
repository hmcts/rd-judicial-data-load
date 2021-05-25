package uk.gov.hmcts.reform.juddata.camel.binder;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
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
        assertEquals(date, judicialOfficeAuthorisation.getCreatedDate());
        assertEquals(date, judicialOfficeAuthorisation.getLastUpdated());
        assertEquals(Long.valueOf("12345"), judicialOfficeAuthorisation.getTicketId());
        assertEquals("lowerLevel", judicialOfficeAuthorisation.getLowerLevel());
    }
}
