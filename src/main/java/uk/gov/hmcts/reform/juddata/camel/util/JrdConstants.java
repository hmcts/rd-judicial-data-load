package uk.gov.hmcts.reform.juddata.camel.util;

import org.springframework.jdbc.core.RowMapper;

public interface JrdConstants {
    String DATE_FORMAT_ERROR_MESSAGE = "date pattern should be yyyy-MM-dd hh:mm:ss.SSS";
    String DATE_FORMAT_WITH_MILLIS = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}.\\d{3,9}";
    String IS_PARENT = "isParent";
    String INVALID_JSR_PARENT_ROW = "Record skipped due to jsr violation in the record"
        .concat(" in the parent load");
    String MISSING_ELINKS = "elinks id is missing from parent Personal file";
    String MISSING_ROLES = "role id is missing from Roles file";
    String MISSING_LOCATION = "region_id id is missing from Locations file";
    String MISSING_BASE_LOCATION = "base location id is missing from BaseLocations file";
    String MISSING_CONTRACT = "contract id is missing from Contract file";
    String JOB_ID = "JOB_ID";
    String ASB_PUBLISHING_STATUS = "ASB_PUBLISHING_STATUS";
    RowMapper<String> ROW_MAPPER = (rs, i) -> rs.getString(1);
}
