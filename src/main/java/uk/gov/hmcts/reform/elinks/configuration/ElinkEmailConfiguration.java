package uk.gov.hmcts.reform.elinks.configuration;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "email")
public class ElinkEmailConfiguration {

    private Map<String,MailTypeConfig> mailTypes;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MailTypeConfig {
        private String from;
        private List<String> to;
        private String subject;
        private String body;
        private boolean enabled;
        private String template;
        private Map<String, Object> model;
    }
}
