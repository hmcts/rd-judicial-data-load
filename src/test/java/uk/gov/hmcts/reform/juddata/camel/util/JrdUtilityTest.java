package uk.gov.hmcts.reform.juddata.camel.util;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class JrdUtilityTest {

    @Mock
    private JdbcTemplate mockJdbcTemplate;

    @InjectMocks
    private JrdUtility jrdUtilityUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void testSchedularAuditUpdate() {
        // Setup
        String schedulerInsertJrdSql="insert into schedular_audit (scheduler_Id,scheduler_Name,scheduler_Start_Time,scheduler_End_Time,scheduler_Status)\n" +
                "  values(DEFAULT,?,?,?,?)";
        String schedulerName="judicial_main_scheduler";
        String schedulerStatus="Test";
        Timestamp schedulerStartTime=new Timestamp(System.currentTimeMillis());
        final Exchange exchange = mock(Exchange.class);
        Message message = mock(Message.class);
        Map<String, Object> schedulerHeader=JrdUtility.getSchedulerHeader( "scheduler_Name", new Timestamp(System.currentTimeMillis()));
        message.setHeaders(schedulerHeader);
        when(exchange.getIn()).thenReturn(message);
        when(message.getHeader("SchedulerName")).thenReturn(schedulerName);
        when(message.getHeader("SchedulerStartTime")).thenReturn(schedulerStartTime);
        when(message.getHeader("SchedulerStatus")).thenReturn(schedulerStatus);
        when(mockJdbcTemplate.update(schedulerInsertJrdSql, schedulerName, schedulerStartTime, new  Timestamp(System.currentTimeMillis()), schedulerStatus)).thenReturn(0);

        // Run the test
        jrdUtilityUnderTest.schedularAuditUpdate(exchange);

        // Verify the results
    }
}
