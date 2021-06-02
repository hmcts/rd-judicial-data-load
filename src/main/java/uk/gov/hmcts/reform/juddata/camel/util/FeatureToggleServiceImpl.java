package uk.gov.hmcts.reform.juddata.camel.util;

import com.google.common.collect.ImmutableList;
import com.launchdarkly.sdk.LDUser;
import com.launchdarkly.sdk.server.LDClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import javax.annotation.PostConstruct;

@Service
public class FeatureToggleServiceImpl implements FeatureToggleService {

    public static final String JRD_ASB_FLAG = "jrd-publish-user-profiles-flag";

    @Autowired
    private final LDClient ldClient;

    @Value("${launchdarkly.sdk.environment}")
    private String environment;

    private final String userName;

    private List<String> launchDarklyMap;

    @Autowired
    public FeatureToggleServiceImpl(LDClient ldClient, @Value("${launchdarkly.sdk.user}") String userName) {
        this.ldClient = ldClient;
        this.userName = userName;
    }

    @PostConstruct
    public void mapServiceToFlag() {
        launchDarklyMap = ImmutableList.of(JRD_ASB_FLAG);
    }

    @Override
    public boolean isFlagEnabled(String flagName) {
        LDUser user = new LDUser.Builder(userName)
            .firstName(userName)
            .custom("environment", environment)
            .build();

        return ldClient.boolVariation(flagName, user, false);
    }

    @Override
    public List<String> getLaunchDarklyFlags() {
        return launchDarklyMap;
    }

}




