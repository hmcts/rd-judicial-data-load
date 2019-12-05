package uk.gov.hmcts.reform.judicialapi.camel.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Header;

public class DynamicRouterBean1 {

    public String route(String body, @Header(Exchange.SLIP_ENDPOINT) String previousRoute) {
        if (previousRoute == null) {
            return "direct://route1";
            // check the body content and decide route
        } else if (body.toString().equals("javainuse in route 3")) {
            return "direct://route2";
            // check the body content and decide route
        } else if (body.toString().equals("javainuse in route 3 in route 2")) {
            return "direct://route1";
        } else {
            return null;
        }
    }
}
