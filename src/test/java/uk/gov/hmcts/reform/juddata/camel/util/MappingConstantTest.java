package uk.gov.hmcts.reform.juddata.camel.util;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper.createCurrentLocalDate;

import java.sql.Timestamp;
import org.junit.Test;

public class MappingConstantTest {

    @Test
    public void test_getDateTimeStamp() {
        Timestamp ts = MappingConstants.getDateTimeStamp(createCurrentLocalDate());
        assertThat(ts).isExactlyInstanceOf(Timestamp.class);
        assertThat(ts).isNotNull();
    }

    @Test
    public void test_getCurrentTimeStamp() {
        Timestamp ts = MappingConstants.getCurrentTimeStamp();
        assertThat(ts).isExactlyInstanceOf(Timestamp.class);
        assertThat(ts).isNotNull();
    }
}