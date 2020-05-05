package uk.gov.hmcts.reform.juddata.camel.initializer;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import org.apache.camel.CamelContext;
import org.junit.Test;
import uk.gov.hmcts.reform.juddata.camel.route.LeafTableRoute;
import uk.gov.hmcts.reform.juddata.camel.route.initializer.JrdLeafDataLoadStarter;

public class JrdLeafDataLoadStarterTest {

    @Test
    public void testPostConstruct() throws Exception {

        JrdLeafDataLoadStarter jrdLeafDataLoadStarter = new JrdLeafDataLoadStarter();

        CamelContext camelContext = mock(CamelContext.class);
        LeafTableRoute leafTableRoute = mock(LeafTableRoute.class);

        setField(jrdLeafDataLoadStarter,"camelContext", camelContext);
        setField(jrdLeafDataLoadStarter,"leafTableRoutes", leafTableRoute);
        doNothing().when(leafTableRoute).startRoute();
        JrdLeafDataLoadStarter jrdLeafDataLoadStarterSpy = spy(jrdLeafDataLoadStarter);
        jrdLeafDataLoadStarterSpy.postConstruct();
        verify(jrdLeafDataLoadStarterSpy, times(1)).postConstruct();
    }
}
