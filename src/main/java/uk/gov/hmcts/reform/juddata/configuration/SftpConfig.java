package uk.gov.hmcts.reform.juddata.configuration;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "sftp")
@Setter
public class SftpConfig {
    private String userName;
    private String userPassword;
    private String host;
    private Integer timeout;
    private List<String> fileNames;
}