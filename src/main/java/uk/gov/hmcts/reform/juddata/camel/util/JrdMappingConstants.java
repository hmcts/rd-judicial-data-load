package uk.gov.hmcts.reform.juddata.camel.util;

public class JrdMappingConstants {

    private JrdMappingConstants() {

    }

    public static final String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}";

    public static final String DATE_PATTERN_TIMESTAMP = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}.\\d{4,9}";

    public static final String DATE_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:MI:SS.MSUS";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";

    public static final String ORCHESTRATED_ROUTE = "parent-route";

    public static final String LEAF_ROUTE = "leaf-route";

    public static final String JUDICIAL_REF_DATA_ORCHESTRATION = "judicial-ref-data-orchestration";

    public static final String PER_ID = "per_id";

    public static final String LOCATION_ID = "region_id";

    public static final String LOWER_LEVEL = "lowerlevel";

    public static final String BASE_LOCATION_ID = "base_location_id";
}
