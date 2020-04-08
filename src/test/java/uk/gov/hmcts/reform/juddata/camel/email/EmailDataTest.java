package uk.gov.hmcts.reform.juddata.camel.email;

import static java.lang.String.join;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import uk.gov.hmcts.reform.juddata.camel.service.EmailData;

public class EmailDataTest {

    private static final String FAMILY_MAN_CASE_NUMBER = randomAlphabetic(12);
    private static final String EMAIL_TO = "recipient@example.com";
    private static final String EMAIL_FROM = "no-reply@exaple.com";
    private static final String EMAIL_SUBJECT = join("", "CaseSubmitted_", FAMILY_MAN_CASE_NUMBER);

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