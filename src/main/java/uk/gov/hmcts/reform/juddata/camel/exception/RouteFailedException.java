package uk.gov.hmcts.reform.juddata.camel.exception;

public class RouteFailedException extends Exception {

    public RouteFailedException(String message) {
        super(message);
    }
}
