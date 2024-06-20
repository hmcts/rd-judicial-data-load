package uk.gov.hmcts.reform.elinks.controller.advice;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
