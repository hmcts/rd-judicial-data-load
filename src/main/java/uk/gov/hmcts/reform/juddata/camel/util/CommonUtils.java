package uk.gov.hmcts.reform.juddata.camel.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import uk.gov.hmcts.reform.juddata.configuration.EmailConfiguration;
import uk.gov.hmcts.reform.juddata.exception.EmailException;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

@Slf4j
public class CommonUtils {
    private  CommonUtils() {
    }

    @Autowired
    @Qualifier("emailConfigBean")
    private static Configuration freemarkerConfig;

    @Autowired
    private static EmailConfiguration emailConfiguration;

    public static Timestamp getDateTimeStamp(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        } else {
            return Timestamp.valueOf(date);
        }
    }

    public static String getEmailBody(String template, Map<String, Object> model) {
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

    public static <T> EmailConfiguration.MailTypeConfig getMailTypeConfig(List<T> list, String emailConfig) {
        EmailConfiguration.MailTypeConfig mailConfig = emailConfiguration.getMailTypes().get(emailConfig);

        Map<String, Object> model = new HashMap<>();
        model.put("listOfObjects", list);

        mailConfig.setModel(model);
        return mailConfig;
    }
}
