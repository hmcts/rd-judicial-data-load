package uk.gov.hmcts.reform.juddata.camel.service;

public class EmailFailedSendException extends RuntimeException {
    public EmailFailedSendException(Throwable cause) {
        super(cause);
    }
}
