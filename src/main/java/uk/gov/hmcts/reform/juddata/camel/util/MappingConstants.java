package uk.gov.hmcts.reform.juddata.camel.util;

import java.sql.Timestamp;
import java.util.Date;

public interface MappingConstants {

    String PARENT_ROUTE_NAME = "parent-route";

    String CHILD_ROUTE_NAME = "child-route";

    String ROUTE = "route";

    String TIMER = "timer";

    String SQL = "sql";

    String TRUNCATE_SQL = "truncate-sql";

    String BLOBPATH = "blob-path";

    String  PROCESSOR = "processor-class";

    String  CSVBINDER = "csv-binder-object";

    String  MAPPER = "mapper-class";

    String  MAPPING_METHOD = "getMap";

    String ID = "id";

    String JUDICIAL_USER_PROFILE_ORCHESTRATION = "judicial-user-profile-orchestration";

    String DIRECT_ROUTE = "direct:";

    static Timestamp getDateTimeStamp(String date) {
        return Timestamp.valueOf(date);
    }

    static Timestamp getCurrentTimeStamp() {

        return new Timestamp(new Date().getTime());
    }
}
