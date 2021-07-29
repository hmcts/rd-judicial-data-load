package uk.gov.hmcts.reform.juddata.exception;

public class JudicialDataLoadException extends RuntimeException {

    final String message;

    public JudicialDataLoadException(String message) {
        this.message = message;
    }
}
