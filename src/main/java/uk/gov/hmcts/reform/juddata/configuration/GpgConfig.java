package uk.gov.hmcts.reform.juddata.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "gpg")
@Setter
public class GpgConfig {
    private String publicKey;
    private String privateKey;
    private String password;
}
