package uk.gov.hmcts.reform.juddata.camel.util;

public enum JobStatus {

    IN_PROGRESS("IN_PROGRESS"),
    FAILED("FAILED"),
    SUCCESS("SUCCESS"),
    FILE_LOAD_FAILED("FILE_LOAD_FAILED");

    String status;

    JobStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
