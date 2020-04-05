package uk.gov.hmcts.reform.juddata.camel.util;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Service
public class DataLoadAudit {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${Scheduler-insert-sql}")
    private String schedulerInsertSql;

    @NotNull
    public static Map<String, Object> getSchedulerHeader(final String schedulerName,final Timestamp schedulerStartTime) {
        Map<String, Object> heders = new HashMap<>();
        heders.put(MappingConstants.SCHEDULER_NAME, schedulerName);
        heders.put(MappingConstants.SCHEDULER_START_TIME, schedulerStartTime);
        return heders;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public  void schedularAuditUpdate(final Exchange exchange) {
        String schedulerName = (String) exchange.getIn().getHeader(MappingConstants.SCHEDULER_NAME);
        String schedulerStatus = (String) exchange.getIn().getHeader(MappingConstants.SCHEDULER_STATUS);
        Timestamp schedulerStartTime = (Timestamp) exchange.getIn().getHeader(MappingConstants.SCHEDULER_START_TIME);
        jdbcTemplate.update(schedulerInsertSql, schedulerName, schedulerStartTime, new  Timestamp(System.currentTimeMillis()), schedulerStatus);
    }
}
