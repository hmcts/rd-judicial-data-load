package uk.gov.hmcts.reform.elinks.service;

import java.util.Map;

public interface FeatureToggleService {

    boolean isFlagEnabled(String flagName);

    Map<String, String> getLaunchDarklyMap();

}
