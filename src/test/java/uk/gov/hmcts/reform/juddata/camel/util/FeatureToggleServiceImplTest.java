package uk.gov.hmcts.reform.juddata.camel.util;

import com.launchdarkly.sdk.server.LDClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.juddata.camel.util.FeatureToggleServiceImpl.JRD_ASB_FLAG;

@ExtendWith(MockitoExtension.class)
class FeatureToggleServiceImplTest {

    LDClient ldClient =  spy(new LDClient("dummkey"));

    @InjectMocks
    FeatureToggleServiceImpl flaFeatureToggleService;

    @Test
    void testIsFlagEnabled() {

        assertFalse(flaFeatureToggleService.isFlagEnabled("test"));
        verify(ldClient).boolVariation(anyString(),any(),anyBoolean());
        assertFalse(ldClient.boolVariation(anyString(),any(),anyBoolean()));
    }

    @Test
    void testIsFlagEnabledTrue() {
        flaFeatureToggleService = spy(new FeatureToggleServiceImpl(ldClient, "rd"));
        flaFeatureToggleService.mapServiceToFlag();
        when(ldClient.boolVariation(anyString(),any(),anyBoolean())).thenReturn(true);
        assertTrue(flaFeatureToggleService.isFlagEnabled(JRD_ASB_FLAG));
        assertTrue(ldClient.boolVariation(anyString(),any(),anyBoolean()));
    }

    @Test
    void mapServiceToFlagTest() {
        flaFeatureToggleService = spy(new FeatureToggleServiceImpl(ldClient, "rd"));
        flaFeatureToggleService.mapServiceToFlag();
        assertTrue(flaFeatureToggleService.getLaunchDarklyFlags().size() >= 1);
        verify(flaFeatureToggleService).getLaunchDarklyFlags();
    }
}
