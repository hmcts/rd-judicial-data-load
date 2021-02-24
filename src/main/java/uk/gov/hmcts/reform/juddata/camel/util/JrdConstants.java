package uk.gov.hmcts.reform.juddata.camel.util;

public interface JrdConstants {
    public static final String DATE_FORMAT_ERROR_MESSAGE = "date pattern should be yyyy-MM-dd hh:mm:ss.SSS";
    public static final String DATE_FORMAT_WITH_MILLIS = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}.\\d{3,9}";
    public static final String IS_PARENT = "isParent";
    public static final String INVALID_JSR_PARENT_ROW = "Record skipped due to jsr violation in the record"
        .concat(" in the parent load");
    public static final String MISSING_ELINKS = "Record skipped due to user profile has not elinks id";
    public static final String MISSING_ROLES = "Record skipped due to no role id is present in role type";
    public static final String MISSING_LOCATION = "Record skipped due to no region id is present in region type";
    public static final String MISSING_BASE_LOCATION = "Record skipped due to no base location id is present in "
        .concat("base location type");
    public static final String MISSING_CONTRACT = "Record skipped due to no contract id is present in contract type";

}
