package uk.gov.hmcts.reform.juddata.camel.email.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.juddata.email.config.EmailConfiguration;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class EmailConfigurationTest {

    @Test
    void testEmailConfiguration() {
        EmailConfiguration.MailTypeConfig mailTypeConfig = new EmailConfiguration.MailTypeConfig();
        mailTypeConfig.setEnabled(true);
        mailTypeConfig.setSubject("%s :: Publishing of JRD messages to ASB failed");
        mailTypeConfig.setBody("Publishing of JRD messages to ASB failed for Job Id %s");
        mailTypeConfig.setFrom("test@test.com");
        mailTypeConfig.setTo(List.of("test@test.com"));
        EmailConfiguration emailConfiguration = new EmailConfiguration();
        emailConfiguration.setMailTypes(Map.of("asb", mailTypeConfig));

        assertThat(emailConfiguration.getMailTypes().get("asb").isEnabled()).isTrue();
        assertThat(emailConfiguration.getMailTypes().get("asb").getSubject())
                .isEqualTo("%s :: Publishing of JRD messages to ASB failed");
        assertThat(emailConfiguration.getMailTypes().get("asb").getFrom()).isEqualTo("test@test.com");
        assertThat(emailConfiguration.getMailTypes().get("asb").getTo()).isEqualTo(List.of("test@test.com"));
        assertThat(emailConfiguration.getMailTypes().get("asb").getBody())
                .isEqualTo("Publishing of JRD messages to ASB failed for Job Id %s");

    }

}
