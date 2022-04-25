package uk.gov.hmcts.reform.juddata.camel.util;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommonUtils {

    private static final String DATETIME_PATTERN = "dd-MM-yyyy HH:mm:ss";

    private  CommonUtils() {
    }

    public static Timestamp getDateTimeStamp(String dateTime) {
        if (StringUtils.isBlank(dateTime)) {
            return null;
        } else {
            LocalDateTime ldt = LocalDateTime.parse(dateTime,
                    DateTimeFormatter.ofPattern(DATETIME_PATTERN));
            return Timestamp.valueOf(ldt);
        }
    }
}
