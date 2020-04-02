package uk.gov.hmcts.reform.juddata.camel.util;

import java.sql.Timestamp;
import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

public class JrdUtilityTest {

    @Mock
    private JdbcTemplate mockJdbcTemplate;

    @InjectMocks
    private JrdUtility jrdUtilityUnderTest;

    @Value("${Scheduler-insert-sql}")
    private String schedulerInsertJrdSql;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSchedularAuditUpdate() {
        final String schedulerName = "judicial_main_scheduler";
        final String schedulerStatus = "Test";
        Message message = Mockito.mock(Message.class);
        Timestamp schedulerStartTime = MappingConstants.getCurrentTimeStamp();
        final Exchange exchange = Mockito.mock(Exchange.class);
        Map<String, Object> schedulerHeader = JrdUtility.getSchedulerHeader(MappingConstants.SCHEDULER_NAME, schedulerStartTime);
        message.setHeaders(schedulerHeader);
        Mockito.when(exchange.getIn()).thenReturn(message);
        Mockito.when(message.getHeader(MappingConstants.SCHEDULER_NAME)).thenReturn(schedulerName);
        Mockito.when(message.getHeader(MappingConstants.SCHEDULER_START_TIME)).thenReturn(schedulerStartTime);
        Mockito.when(message.getHeader(MappingConstants.SCHEDULER_STATUS)).thenReturn(schedulerStatus);
        Mockito.when(mockJdbcTemplate.update(schedulerInsertJrdSql, schedulerName, schedulerStartTime, new  Timestamp(System.currentTimeMillis()), schedulerStatus)).thenReturn(0);

        // Run the test
        jrdUtilityUnderTest.schedularAuditUpdate(exchange);
    }
}
