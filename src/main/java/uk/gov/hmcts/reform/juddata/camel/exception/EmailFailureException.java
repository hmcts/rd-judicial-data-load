package uk.gov.hmcts.reform.juddata.camel.exception;

public class EmailFailureException extends RuntimeException {
    public EmailFailureException(Throwable cause) {
        super(cause);
    }
}