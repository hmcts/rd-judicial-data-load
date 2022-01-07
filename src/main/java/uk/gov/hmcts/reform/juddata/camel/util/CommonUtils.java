package uk.gov.hmcts.reform.juddata.camel.util;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;

public class CommonUtils {
    private  CommonUtils() {
    }

    public static Timestamp getDateTimeStamp(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        } else {
            return Timestamp.valueOf(date);
        }
    }
}
