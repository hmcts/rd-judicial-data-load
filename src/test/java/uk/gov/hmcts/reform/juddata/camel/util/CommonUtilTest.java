package uk.gov.hmcts.reform.juddata.camel.util;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


class CommonUtilTest {


    @Test
    void testEmptyDate() {

        var date = CommonUtils.getDateTimeStamp(" ");
        assertNull(date);
    }

    @Test
    void testNullDate() {

        var date = CommonUtils.getDateTimeStamp(null);
        assertNull(date);
    }

    @Test
    void testValidDate() {

        var date = CommonUtils.getDateTimeStamp("2022-06-09 12:00:40");
        assertNotNull(date);
    }
}
