package uk.gov.hmcts.reform.juddata.camel.initializer;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import org.apache.camel.CamelContext;
import org.junit.Test;
import uk.gov.hmcts.reform.juddata.camel.route.ParentOrchestrationRoute;
import uk.gov.hmcts.reform.juddata.camel.route.initializer.JrdUserProfileDataLoadStarter;

public class JrdUserProfileDataLoadStarterTest {

    @Test
    public void testPostConstruct() throws Exception {

        JrdUserProfileDataLoadStarter jrdUserProfileDataLoadStarter = new JrdUserProfileDataLoadStarter();

        CamelContext camelContext = mock(CamelContext.class);
        ParentOrchestrationRoute parentOrchestrationRoute = mock(ParentOrchestrationRoute.class);

        setField(jrdUserProfileDataLoadStarter, "camelContext", camelContext);
        setField(jrdUserProfileDataLoadStarter, "parentOrchestrationRoute", parentOrchestrationRoute);
        doNothing().when(parentOrchestrationRoute).startRoute();

        JrdUserProfileDataLoadStarter jrdUserProfileDataLoadStarterSpy = spy(jrdUserProfileDataLoadStarter);
        jrdUserProfileDataLoadStarterSpy.postConstruct();
        verify(jrdUserProfileDataLoadStarterSpy, times(1)).postConstruct();
    }
}
