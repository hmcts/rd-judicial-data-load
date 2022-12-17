package uk.gov.hmcts.reform.elinks.util;

public class SqlContants {

    private SqlContants(){

    }

    public static final String INSERT_AUDIT_JOB = "INSERT INTO dbjudicialdata.dataload_schedular_job(job_start_time, "
            + "publishing_status) VALUES(?, ?)";

    public static final String SELECT_JOB_STATUS_SQL = "SELECT id, publishing_status FROM "
            + "dbjudicialdata.dataload_schedular_job WHERE DATE(job_start_time) = current_date";

    public static final String GET_DISTINCT_SIDAM_ID = "SELECT DISTINCT sidam_id FROM "
            + "dbjudicialdata.judicial_user_profile WHERE sidam_id IS NOT NULL";

    public static final String UPDATE_JOB_SQL = "UPDATE dbjudicialdata.dataload_schedular_job "
            + "SET job_end_time = NOW() AT TIME ZONE 'utc', publishing_status = ? WHERE id =?";
}
