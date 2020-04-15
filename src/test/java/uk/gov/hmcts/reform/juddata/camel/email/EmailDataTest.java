package uk.gov.hmcts.reform.juddata.camel.email;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper;
import uk.gov.hmcts.reform.juddata.camel.service.EmailData;

public class EmailDataTest {

    private static final String EMAIL_TO = "recipient@example.com";
    private static final String EMAIL_SUBJECT = " Ref Data - Exception in JRD data load";

    private EmailData emailDataUnderTest;

    @Test
    public void testEmailData() {
        EmailData emailData = JrdUnitTestHelper.getMockEmail();
        assertEquals(EMAIL_TO, emailData.getRecipient());
        assertEquals(EMAIL_SUBJECT, emailData.getSubject());
        assertEquals("", emailData.getMessage());

    }
}