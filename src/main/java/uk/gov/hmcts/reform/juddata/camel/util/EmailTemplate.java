package uk.gov.hmcts.reform.juddata.camel.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import uk.gov.hmcts.reform.juddata.configuration.EmailConfiguration;
import uk.gov.hmcts.reform.juddata.exception.EmailException;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class EmailTemplate {

    @Autowired
    @Qualifier("emailConfigBean")
    Configuration freemarkerConfig;

    @Autowired
    EmailConfiguration emailConfiguration;

    public String getEmailBody(String template, Map<String, Object> model) {
        String emailBody = "";
        try {
            Template t = freemarkerConfig.getTemplate(template);
            emailBody = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
        } catch (IOException | TemplateException ex) {
            log.info("Exception while processing email template!", ex);
            throw new EmailException(ex.getMessage(), ex);
        }
        return emailBody;
    }

    public EmailConfiguration.MailTypeConfig getMailTypeConfig(Map<String, Object> model, String emailConfig) {
        EmailConfiguration.MailTypeConfig mailConfig = emailConfiguration.getMailTypes().get(emailConfig);
        mailConfig.setModel(model);
        return mailConfig;
    }
}
