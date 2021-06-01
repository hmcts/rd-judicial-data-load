package uk.gov.hmcts.reform.juddata.camel.util;

import java.util.List;

public interface FeatureToggleService {

    boolean isFlagEnabled(String flagName);

    List<String> getLaunchDarklyFlags();
}
