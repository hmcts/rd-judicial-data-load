package uk.gov.hmcts.reform.elinks.idam;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.reform.elinks.config.TestConfigProperties;
import uk.gov.hmcts.reform.lib.idam.IdamOpenId;


@Slf4j
public class IdamOpenIdClient extends IdamOpenId {

    private final TestConfigProperties testConfig;

    private final Gson gson = new Gson();

    public static String jrdAdminToken;

    private static String sidamPassword;

    public static String jrdSystemUserToken;

    public IdamOpenIdClient(TestConfigProperties testConfig) {
        super(testConfig);
        this.testConfig = testConfig;
    }

}