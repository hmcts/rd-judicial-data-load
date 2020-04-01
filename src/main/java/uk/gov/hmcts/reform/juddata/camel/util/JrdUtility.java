package uk.gov.hmcts.reform.juddata.camel.util;

import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@EnableTransactionManagement
public class JrdUtility {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //make  a  generic  names  not JRD
    @Value("${Scheduler-insert-sql}")
    private String schedulerInsertJrdSql;

    @NotNull
    public static Map<String, Object> getSchedulerHeader(String scheduler_Name, Timestamp SchedulerStartTime) {
        Map<String, Object> heders = new HashMap<>();
        heders.put("SchedulerName", scheduler_Name);
        heders.put("SchedulerStartTime", SchedulerStartTime);
        return heders;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public  void schedularAuditUpdate(Exchange exchange) {
        String schedulerName = (String) exchange.getIn().getHeader("SchedulerName");
        String schedulerStatus = (String) exchange.getIn().getHeader("SchedulerStatus");
        Timestamp schedulerStartTime = (Timestamp) exchange.getIn().getHeader("SchedulerStartTime");
        jdbcTemplate.update(schedulerInsertJrdSql, schedulerName, schedulerStartTime, new  Timestamp(System.currentTimeMillis()), schedulerStatus);
    }
}
