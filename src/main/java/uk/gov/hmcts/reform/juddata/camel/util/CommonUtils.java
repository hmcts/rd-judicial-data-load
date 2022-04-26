package uk.gov.hmcts.reform.juddata.camel.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.DATE_TIME_FORMAT;

public class CommonUtils {

    private  CommonUtils() {
    }

    public static Timestamp getDateTimeStamp(String dateTime) {

        if (!dateTime.isBlank()) {
            LocalDateTime ldt = LocalDateTime.parse(dateTime,
                    DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
            return Timestamp.valueOf(ldt);
        }
        return null;
    }
}
