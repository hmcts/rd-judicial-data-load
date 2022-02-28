package uk.gov.hmcts.reform.juddata.camel.route.beans;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.RouteProperties;
import uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport;


class RoutePropertiesTest {

    @Test
    void test_objects_RouteProperties_correctly() {

        RouteProperties routeProperties = JrdTestSupport.createRoutePropertiesMock();

        Assertions.assertEquals("Binder", routeProperties.getBinder());
        Assertions.assertEquals("Blobpath", routeProperties.getBlobPath());
        Assertions.assertEquals("childNames", routeProperties.getChildNames());
        Assertions.assertEquals("mapper", routeProperties.getMapper());
        Assertions.assertEquals("processor", routeProperties.getProcessor());
        Assertions.assertEquals("routeName", routeProperties.getRouteName());
        Assertions.assertEquals("sql", routeProperties.getSql());
        Assertions.assertEquals("truncateSql", routeProperties.getTruncateSql());
    }
}
