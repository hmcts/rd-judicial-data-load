package uk.gov.hmcts.reform.juddata.camel.util;

import freemarker.template.Configuration;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAuthorisation;
import uk.gov.hmcts.reform.juddata.configuration.EmailConfiguration;
import uk.gov.hmcts.reform.juddata.exception.EmailException;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailTemplateTest {

    EmailTemplate emailTemplate;
    final Configuration freemarkerConfig = mock(Configuration.class);
    final EmailConfiguration emailConfiguration = mock(EmailConfiguration.class);

    @BeforeEach
    public void setup() {
        emailTemplate = spy(new EmailTemplate());
        setField(emailTemplate, "freemarkerConfig", freemarkerConfig);
        setField(emailTemplate, "emailConfiguration", emailConfiguration);
    }

    @Test
    @SuppressWarnings("rawtypes")
    void getMailTypeConfigTest() {
        EmailConfiguration.MailTypeConfig mailConfig = new EmailConfiguration.MailTypeConfig();

        Map<String, EmailConfiguration.MailTypeConfig> mailTypes = Map.of("lowerLevelAuth", mailConfig);

        Map<String, Object> model = getModel();

        when(emailConfiguration.getMailTypes()).thenReturn(mailTypes);
        EmailConfiguration.MailTypeConfig mailTypeConfigReturned =
                emailTemplate.getMailTypeConfig(model, "lowerLevelAuth");
        assertEquals(model, mailTypeConfigReturned.getModel());
        assertThat(Arrays.asList(mailTypeConfigReturned.getModel().get("newLowerLevelAuths"))).hasSize(1);
    }

    @Test
    void getEmailBody_should_throw_exception() throws Exception {
        when(freemarkerConfig.getTemplate(anyString())).thenThrow(IOException.class);
        Map<String, Object> model = getModel();

        assertThrows(EmailException.class,
            () -> emailTemplate.getEmailBody("lower-level-auth.ftl", model));
    }

    @NotNull
    private Map<String, Object> getModel() {
        List<JudicialOfficeAuthorisation> newLowerLevelAuths = List.of(new JudicialOfficeAuthorisation());
        Map<String, Object> model = new HashMap<>();
        model.put("newLowerLevelAuths", newLowerLevelAuths);
        return model;
    }
}