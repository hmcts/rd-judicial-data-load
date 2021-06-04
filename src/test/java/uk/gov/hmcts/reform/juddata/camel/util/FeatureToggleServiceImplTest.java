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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FeatureToggleServiceImplTest {

    LDClient ldClient = mock(LDClient.class);
    @InjectMocks
    FeatureToggleServiceImpl flaFeatureToggleService;

    @Test
    public void testIsFlagEnabled() {
        assertFalse(flaFeatureToggleService.isFlagEnabled("test"));
        verify(ldClient).boolVariation(anyString(),any(),anyBoolean());
    }

    @Test
    public void mapServiceToFlagTest() {
        flaFeatureToggleService = spy(new FeatureToggleServiceImpl(ldClient, "rd"));
        flaFeatureToggleService.mapServiceToFlag();
        assertTrue(flaFeatureToggleService.getLaunchDarklyFlags().size() >= 1);
        verify(flaFeatureToggleService).getLaunchDarklyFlags();
    }
}
