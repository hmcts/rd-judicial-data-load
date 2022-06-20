package uk.gov.hmcts.reform.juddata.camel.util;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CommonUtilsTest {

    @Test
    void test_empty_getDateTimeStamp() {

        Timestamp dateValue = CommonUtils.getDateTimeStamp("");
        assertNull(dateValue);
    }

    @Test
    void test_valid_getDateTimeStamp() {
        Timestamp inputValue = new Timestamp(System.currentTimeMillis());
        Timestamp dateValue = CommonUtils.getDateTimeStamp(inputValue.toString());
        assertNotNull(dateValue);
    }
}
