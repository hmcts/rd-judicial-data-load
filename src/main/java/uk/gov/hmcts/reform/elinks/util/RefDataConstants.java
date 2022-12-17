package uk.gov.hmcts.reform.elinks.util;


import org.springframework.jdbc.core.RowMapper;

public class RefDataConstants {

    private RefDataConstants() {
    }

    public static final String AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "Content-Type";

    public static final String BAD_REQUEST = "Bad Request";
    public static final String FORBIDDEN_ERROR = "Forbidden Error: Access denied for invalid permissions";
    public static final String UNAUTHORIZED_ERROR =
            "Unauthorized Error : The requested resource is restricted and requires authentication";
    public static final String NO_DATA_FOUND = "The eLinks data could not be found";
    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

    public static final String TOO_MANY_REQUESTS = "Too Many Requests";

    public static final String JOB_ID = "JOB_ID";

    public static final String ASB_PUBLISHING_STATUS = "ASB_PUBLISHING_STATUS";

    public static final String CONTENT_TYPE_PLAIN = "text/plain";

    public static final RowMapper<String> ROW_MAPPER = (rs, i) -> rs.getString(1);

}
