package uk.gov.hmcts.reform.juddata.camel.util;

import org.springframework.jdbc.core.RowMapper;

public class JrdConstants {

    private JrdConstants() {
        throw new AssertionError();
    }

    public static final String DATE_FORMAT_ERROR_MESSAGE = "date pattern should be yyyy-MM-dd hh:mm:ss.SSS";
    public static final String DATE_FORMAT_WITH_MILLIS = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}.\\d{3,9}";
    public static final String IS_PARENT = "isParent";
    public static final String INVALID_JSR_PARENT_ROW = "Record skipped due to jsr violation in the record"
            .concat(" in the parent load");
    public static final String MISSING_PER_ID = "per id is missing from parent Personal file";
    public static final String MISSING_LOCATION = "region_id id is missing from Locations file";
    public static final String MISSING_BASE_LOCATION = "base location id is missing from BaseLocations file";
    public static final String MISSING_CONTRACT = "contract id is missing from Contract file";
    public static final String JOB_ID = "JOB_ID";
    public static final String ASB_PUBLISHING_STATUS = "ASB_PUBLISHING_STATUS";
    public static final RowMapper<String> ROW_MAPPER = (rs, i) -> rs.getString(1);
    public static final String PER_CODE_OBJECT_ID_FIELD = "personal code/object id";
    public static final String PER_CODE_OBJECT_ID_ERROR_MESSAGE = "Invalid personal code or object id";
    public static final String USERPROFILE = "userprofile";
    public static final String NEW_LINE = "\n";
    public static final String DATE_PATTERN = "dd/MM/yyyy";
}
