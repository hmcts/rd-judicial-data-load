package uk.gov.hmcts.reform.juddata.camel.util;

public interface JrdConstants {
    public static final String DATE_FORMAT_ERROR_MESSAGE = "date pattern should be yyyy-MM-dd hh:mm:ss.SSS";
    public static final String DATE_FORMAT_WITH_MILLIS =  "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}.\\d{3,9}";
    public static final String IS_PARENT = "isParent";
}
