package uk.gov.hmcts.reform.juddata.camel.util;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.DATE_TIME_FORMAT;

public class CommonUtils {

    private  CommonUtils() {
    }

    public static Timestamp getDateTimeStamp(String dateTime) {

        if (!StringUtils.isBlank(dateTime)) {
            LocalDateTime ldt = LocalDateTime.parse(dateTime,
                    DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
            return Timestamp.valueOf(ldt);
        }
        return null;
    }
}
