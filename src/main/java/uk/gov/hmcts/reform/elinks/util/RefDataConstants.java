package uk.gov.hmcts.reform.elinks.util;


public class RefDataConstants {

    private RefDataConstants() {
    }

    public static final String AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "Content-Type";

    public static final String BAD_REQUEST = "Syntax error or Bad Request";
    public static final String FORBIDDEN_ERROR = "Your source IP address is not whitelisted";
    public static final String UNAUTHORIZED_ERROR =
            "A valid access token hasn't been provided in the right form";
    public static final String NO_DATA_FOUND = "The given attribute name does not exist in the reference data";
    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

    public static final String TOO_MANY_REQUESTS = "You have exceeded the request limit (20 requests in 20 seconds)";

    public static final String LOCATION_DATA_LOAD_SUCCESS = "Location(RegionType) Loaded successfully";

    public static final String BASE_LOCATION_DATA_LOAD_SUCCESS = "Base Location Loaded successfully";

    public static final String ELINKS_ACCESS_ERROR = "An error occurred while retrieving data from Elinks";
    public static final String ELINKS_DATA_STORE_ERROR = "An error occurred while storing data from Elinks";
    public static final String ERROR_IN_PARSING_THE_FEIGN_RESPONSE = "Error in parsing %s Feign Response";

    public static final String ELINKS_ERROR_RESPONSE_BAD_REQUEST = "Syntax error or Bad request";
    public static final String ELINKS_ERROR_RESPONSE_UNAUTHORIZED =
            "A valid access token hasn't been provided in the right form";
    public static final String ELINKS_ERROR_RESPONSE_FORBIDDEN = "Your source IP address is not whitelisted";
    public static final String ELINKS_ERROR_RESPONSE_NOT_FOUND
            = "The given attribute name does not exist in the reference data";

    public static final String ELINKS_ERROR_RESPONSE_TOO_MANY_REQUESTS
            = "You have exceeded the request limit (20 requests in 20 seconds)";





}
