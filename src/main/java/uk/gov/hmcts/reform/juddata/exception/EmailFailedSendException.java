package uk.gov.hmcts.reform.juddata.exception;

public class EmailFailedSendException extends RuntimeException {
    public EmailFailedSendException(Throwable cause) {
        super(cause);
    }
}
