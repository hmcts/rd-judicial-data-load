package uk.gov.hmcts.reform.juddata.camel.processor;

import static java.lang.Boolean.TRUE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.ORCHESTRATED_ROUTE;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SCHEDULER_NAME;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SCHEDULER_START_TIME;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import uk.gov.hmcts.reform.juddata.camel.service.AuditProcessingService;
import uk.gov.hmcts.reform.juddata.camel.util.MappingConstants;

public class AuditProcessingServiceTest {


    private JdbcTemplate mockJdbcTemplate = mock(JdbcTemplate.class);

    private AuditProcessingService dataLoadAuditUnderTest = mock(AuditProcessingService.class);

    PlatformTransactionManager platformTransactionManager = mock(PlatformTransactionManager.class);

    TransactionStatus transactionStatus = mock(TransactionStatus.class);

    @Value("${Scheduler-insert-sql}")
    private String schedulerInsertSql;

    public static Map<String, String> getGlobalOptions(String schedulerName) {
        Map<String, String> globalOptions = new HashMap<>();
        globalOptions.put(ORCHESTRATED_ROUTE, MappingConstants.JUDICIAL_USER_PROFILE_ORCHESTRATION);
        globalOptions.put(SCHEDULER_START_TIME, String.valueOf(new Date().getTime()));
        globalOptions.put(SCHEDULER_NAME, schedulerName);
        return globalOptions;
    }

    @Before
    public void setUp() {
        setField(dataLoadAuditUnderTest, "auditEnabled", TRUE);
        setField(dataLoadAuditUnderTest, "jdbcTemplate", mockJdbcTemplate);
        setField(dataLoadAuditUnderTest, "platformTransactionManager", platformTransactionManager);
        //jdbcTemplate

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSchedulerAuditUpdate() throws Exception {

        final String schedulerName = "judicial_main_scheduler";

        final Exchange exchange = Mockito.mock(Exchange.class);
        CamelContext camelContext = Mockito.mock(CamelContext.class);

        when(exchange.getContext()).thenReturn(camelContext);
        Map<String, String> globalOptions = getGlobalOptions(schedulerName);
        when(exchange.getContext().getGlobalOptions()).thenReturn(globalOptions);
        when(camelContext.getGlobalOptions()).thenReturn(globalOptions);
        //when(mockJdbcTemplate.update(schedulerInsertSql, schedulerName, schedulerStartTime, schedulerEndTime, schedulerStatus)).thenReturn(0);
        when(mockJdbcTemplate.update(anyString(), anyString(), any(), any(), any())).thenReturn(1);
        when(platformTransactionManager.getTransaction(any())).thenReturn(transactionStatus);
        doNothing().when(platformTransactionManager).commit(transactionStatus);

        dataLoadAuditUnderTest.auditSchedulerStatus(camelContext);

        verify(dataLoadAuditUnderTest, times(1)).auditSchedulerStatus(any(CamelContext.class));
    }
}