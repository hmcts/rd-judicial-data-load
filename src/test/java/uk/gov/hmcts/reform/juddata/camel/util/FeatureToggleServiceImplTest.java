package uk.gov.hmcts.reform.juddata.camel.util;

import com.launchdarkly.sdk.server.LDClient;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class FeatureToggleServiceImplTest {

    LDClient ldClient = mock(LDClient.class);
    FeatureToggleServiceImpl flaFeatureToggleService = mock(FeatureToggleServiceImpl.class);

    @Test
    public void testIsFlagEnabled() {
        flaFeatureToggleService = new FeatureToggleServiceImpl(ldClient, "rd");
        assertFalse(flaFeatureToggleService.isFlagEnabled("test"));
    }

    @Test
    public void mapServiceToFlagTest() {
        flaFeatureToggleService = new FeatureToggleServiceImpl(ldClient, "rd");
        flaFeatureToggleService.mapServiceToFlag();
        assertTrue(flaFeatureToggleService.getLaunchDarklyFlags().size() >= 1);
    }
}
