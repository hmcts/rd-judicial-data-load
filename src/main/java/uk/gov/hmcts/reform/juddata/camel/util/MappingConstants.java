package uk.gov.hmcts.reform.juddata.camel.util;

import java.sql.Timestamp;
import java.util.Date;

public class MappingConstants {

    private MappingConstants() {
    }

    public static final String ORCHESTRATED_ROUTE = "parent-route";

    public static final String CHILD_ROUTES = "child-route";

    public static final String ROUTE = "route";

    public static final String INSERT_SQL = "insert-sql";

    public static final String TRUNCATE_SQL = "truncate-sql";

    public static final String BLOBPATH = "blob-path";

    public static final String  PROCESSOR = "processor-class";

    public static final String  CSVBINDER = "csv-binder-object";

    public static final String  MAPPER = "mapper-class";

    public static final String  MAPPING_METHOD = "getMap";

    public static final String ID = "id";

    public static final String JUDICIAL_USER_PROFILE_ORCHESTRATION = "judicial-user-profile-orchestration";

    public static final String DIRECT_ROUTE = "direct:";

    public static Timestamp getDateTimeStamp(String date) {
        return Timestamp.valueOf(date);
    }

    public static Timestamp getCurrentTimeStamp() {

        return new Timestamp(new Date().getTime());
    }
}
