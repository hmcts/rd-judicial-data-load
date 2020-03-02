package uk.gov.hmcts.reform.juddata.camel.vo;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper.createRoutePropertiesMock;

import org.junit.Test;

public class RoutePropertiesTest {

    @Test
    public void  test_objects_RouteProperties_correctly() {

        RouteProperties routeProperties = createRoutePropertiesMock();

        assertEquals(routeProperties.getBinder(), "Binder");
        assertEquals(routeProperties.getBlobPath(), "Blobpath");
        assertEquals(routeProperties.getChildNames(), "childNames");
        assertEquals(routeProperties.getMapper(), "mapper");
        assertEquals(routeProperties.getProcessor(), "processor");
        assertEquals(routeProperties.getRouteName(), "routeName");
        assertEquals(routeProperties.getSql(), "sql");
        assertEquals(routeProperties.getTruncateSql(), "truncateSql");
    }
}
