package uk.gov.hmcts.reform.juddata.camel.route.beans;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.RouteProperties;
import uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport;

public class RoutePropertiesTest {

    @Test
    public void  test_objects_RouteProperties_correctly() {

        RouteProperties routeProperties = JrdTestSupport.createRoutePropertiesMock();

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
