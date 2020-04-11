package uk.gov.hmcts.reform.juddata.camel.email;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import uk.gov.hmcts.reform.juddata.camel.service.EmailData;

public class EmailDataTest {

    private static final String EMAIL_TO = "recipient@example.com";
    private static final String EMAIL_SUBJECT = "Exception received at Rout id";

    private EmailData emailDataUnderTest;

    @Test
    public void testEmailData() {
        EmailData emailData = TestEmailData.getDefault();
        assertEquals(EMAIL_TO, emailData.getRecipient());
        assertEquals(EMAIL_SUBJECT, emailData.getSubject());
        assertEquals("", emailData.getMessage());

    }

    static class TestEmailData {
        static EmailData getDefault() {
            return EmailData.builder()
                    .recipient(EMAIL_TO)
                    .subject(EMAIL_SUBJECT)
                    .message("")
                    .build();
        }
    }
}