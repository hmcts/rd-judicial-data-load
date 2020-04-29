package uk.gov.hmcts.reform.juddata.camel.exception;

public class RouteFailedException extends RuntimeException {

    public RouteFailedException(String message) {
        super(message);
    }
}
