package uk.gov.hmcts.reform.juddata.camel.util;

public class MappingConstants {

    private MappingConstants() {
    }

    public static final String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}";

    public static final String DATE_PATTERN_TIMESTAMP = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}.\\d{4,9}";

    public static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    public static final String DATE_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:MI:SS.MSUS";

    public static final String ORCHESTRATED_ROUTE = "parent-route";

    public static final String SCHEDULER_START_TIME = "start-time";

    public static final String CHILD_ROUTES = "child-route";

    public static final String ROUTE = "route";

    public static final String LEAF_ROUTE = "leaf-route";

    public static final String LEAF_ROUTE_NAMES = "leaf-route-names";

    public static final String INSERT_SQL = "insert-sql";

    public static final String TRUNCATE_SQL = "truncate-sql";

    public static final String BLOBPATH = "blob-path";

    public static final String  PROCESSOR = "processor-class";

    public static final String  CSVBINDER = "csv-binder-object";

    public static final String  MAPPER = "mapper-class";

    public static final String  MAPPING_METHOD = "getMap";

    public static final String ID = "id";

    public static final String FILE_NAME = "file-name";

    public static final String TABLE_NAME = "table-name";

    public static final String HEADER_EXCEPTION = "header-exception";

    public static final String ROUTE_DETAILS = "routedetails";

    public static final String JUDICIAL_USER_PROFILE_ORCHESTRATION = "judicial-user-profile-orchestration";

    public static final String DIRECT_ROUTE = "direct:";

    public static final String IS_EXCEPTION_HANDLED = "is-exception-handled";

    public static final String SCHEDULER_STATUS = "SchedulerStatus";

    public static final String SCHEDULER_NAME = "SchedulerName";

    public static final String PARTIAL_SUCCESS = "PartialSuccess";

    public static final String FAILURE = "Failure";

    public static final String SUCCESS = "Success";

}
