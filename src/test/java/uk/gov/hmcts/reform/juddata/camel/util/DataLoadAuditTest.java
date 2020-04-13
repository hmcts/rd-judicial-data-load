package uk.gov.hmcts.reform.juddata.camel.util;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

public class DataLoadAuditTest extends CamelTestSupport {

    @Mock
    private JdbcTemplate mockJdbcTemplate;

    @InjectMocks
    private DataLoadAudit dataLoadAuditUnderTest;

    @Value("${Scheduler-insert-sql}")
    private String schedulerInsertSql;

    public static Map<String, String> getGlobalOptions(String schedulerName) {
        Map<String, String> globalOptions = new HashMap<>();
        globalOptions.put(MappingConstants.ORCHESTRATED_ROUTE, MappingConstants.JUDICIAL_USER_PROFILE_ORCHESTRATION);
        globalOptions.put(MappingConstants.SCHEDULER_START_TIME, MappingConstants.getCurrentTimeStamp().toString());
        globalOptions.put(MappingConstants.SCHEDULER_NAME, schedulerName);
        return globalOptions;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSchedularAuditUpdate() throws Exception {

        final String schedulerName = "judicial_main_scheduler";
        final String schedulerStatus = "Success";
        Message message = Mockito.mock(Message.class);
        final Exchange exchange = Mockito.mock(Exchange.class);
        CamelContext camelContext = Mockito.mock(CamelContext.class);

        Mockito.when(exchange.getContext()).thenReturn(camelContext);
        Map<String, String> globalOptions = getGlobalOptions(schedulerName);
        Mockito.when(exchange.getContext().getGlobalOptions()).thenReturn(globalOptions);
        Message messageMock = Mockito.mock(Message.class);

        Mockito.when(camelContext.getGlobalOptions()).thenReturn(globalOptions);
        Timestamp schedulerStartTime = MappingConstants.getCurrentTimeStamp();
        Timestamp schedulerEndTime = MappingConstants.getCurrentTimeStamp();
        Mockito.when(mockJdbcTemplate.update(schedulerInsertSql, schedulerName, schedulerStartTime, schedulerEndTime, schedulerStatus)).thenReturn(0);

        dataLoadAuditUnderTest.schedularAuditUpdate(exchange);
    }
}