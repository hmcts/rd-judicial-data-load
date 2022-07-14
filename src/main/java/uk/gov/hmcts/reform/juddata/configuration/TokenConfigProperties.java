package uk.gov.hmcts.reform.juddata.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "idam.api")
@Setter
public class TokenConfigProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String authorization;
    private String clientAuthorization;
    private String url;
}
