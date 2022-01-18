package uk.gov.hmcts.reform.juddata.exception;

public class EmailException extends RuntimeException {
    private static final long serialVersionUID = 6027645540740026982L;

    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailException(Throwable cause) {
        super(cause);
    }

    public EmailException(String message) {
        super(message);
    }
}
