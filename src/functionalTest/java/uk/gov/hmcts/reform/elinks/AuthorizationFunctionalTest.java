package uk.gov.hmcts.reform.elinks;

import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.rest.SerenityRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.elinks.client.JudicialDataLoadApiClient;
import uk.gov.hmcts.reform.elinks.config.Oauth2;
import uk.gov.hmcts.reform.elinks.config.TestConfigProperties;
import uk.gov.hmcts.reform.elinks.idam.IdamOpenIdClient;

import javax.annotation.PostConstruct;

@ContextConfiguration(classes = {TestConfigProperties.class, Oauth2.class})
@ComponentScan("uk.gov.hmcts.reform.elinks")
@TestPropertySource("classpath:application-integration.yml")
@Slf4j
public class AuthorizationFunctionalTest {


    @Value("${targetInstance}")
    protected String jrdApiUrl;

    protected static JudicialDataLoadApiClient judicialDataLoadApiClient;

    protected static IdamOpenIdClient idamOpenIdClient;

    @Autowired
    protected TestConfigProperties configProperties;

    public static final String EMAIL = "EMAIL";
    public static final String CREDS = "CREDS";
    public static final String EMAIL_TEMPLATE = "test-user-%s@jrdfunctestuser.com";
    public static final String ROLE_JRD_ADMIN = "jrd-admin";
    public static final String ROLE_JRD_SYSTEM_USER = "jrd-system-user";

    @Autowired
    protected TestConfigProperties testConfigProperties;

    @PostConstruct
    public void beforeTestClass() {
        SerenityRest.useRelaxedHTTPSValidation();

        if (null == idamOpenIdClient) {
            idamOpenIdClient = new IdamOpenIdClient(testConfigProperties);
        }

        judicialDataLoadApiClient = new JudicialDataLoadApiClient(jrdApiUrl,idamOpenIdClient);
    }
}
