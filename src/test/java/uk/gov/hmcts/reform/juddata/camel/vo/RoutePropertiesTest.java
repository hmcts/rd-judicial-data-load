package uk.gov.hmcts.reform.juddata.camel.vo;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper.createRoutePropertiesMock;

import org.junit.Test;

public class RoutePropertiesTest {

    @Test
    public void  test_objects_RouteProperties_correctly() {

        RouteProperties routeProperties = createRoutePropertiesMock();

        assertEquals("Binder", routeProperties.getBinder());
        assertEquals("Blobpath", routeProperties.getBlobPath());
        assertEquals("childNames", routeProperties.getChildNames());
        assertEquals("mapper", routeProperties.getMapper());
        assertEquals("processor", routeProperties.getProcessor());
        assertEquals("routeName", routeProperties.getRouteName());
        assertEquals("sql", routeProperties.getSql());
        assertEquals("truncateSql", routeProperties.getTruncateSql());
    }
}
