package uk.gov.hmcts.reform.elinks.service.impl;

import com.launchdarkly.sdk.LDUser;
import com.launchdarkly.sdk.server.LDClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.elinks.service.FeatureToggleService;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

@Service("eLinkFeatureToggleServiceImpl")
@SuppressWarnings("all")
public class FeatureToggleServiceImpl implements FeatureToggleService {

    @Autowired
    @Qualifier("eLinksLdClient")
    private final LDClient ldClient;

    @Value("${launchdarkly.sdk.environment}")
    private String environment;

    private final String userName;

    private Map<String, String> launchDarklyMap;

    @Autowired
    public FeatureToggleServiceImpl(LDClient ldClient, @Value("${launchdarkly.sdk.user}") String userName) {
        this.ldClient = ldClient;
        this.userName = userName;
    }

    /**
     * add controller.method name, flag name  in map to apply ld flag on api like below
     * launchDarklyMap.put("OrganisationExternalController.retrieveOrganisationsByStatusWithAddressDetailsOptional",
     * "prd-aac-get-org-by-status");
     */
    @PostConstruct
    public void mapServiceToFlag() {
        String judApi = "rd_jud_elinks";
        launchDarklyMap = new HashMap<>();
      //  launchDarklyMap.put("JrdUsersController.fetchUsers", judApi); we need to edit here
      //  launchDarklyMap.put("JrdUsersController.searchUsers", judApi);
      //  launchDarklyMap.put("TestingSupportController.createIdamUserProfiles", "rd-judicial-api-test-idam-users");
      //  launchDarklyMap.put("JrdUsersController.refreshUserProfile", judApi);

        launchDarklyMap.put("ELinksController.loadRegionType", judApi);

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
    public Map<String, String> getLaunchDarklyMap() {
        return launchDarklyMap;
    }
}