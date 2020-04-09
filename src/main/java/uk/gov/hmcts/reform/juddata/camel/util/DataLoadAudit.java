package uk.gov.hmcts.reform.juddata.camel.util;

import com.google.common.base.*;
import java.sql.Timestamp;
import java.util.*;
import javax.transaction.Transactional;

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

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void schedularAuditUpdate(final Exchange exchange) {

        Map<String, String> globalOptions = exchange.getContext().getGlobalOptions();
        Timestamp schedulerStartTime = Timestamp.valueOf(globalOptions.get(MappingConstants.SCHEDULER_START_TIME));
        String schedulerName = globalOptions.get(MappingConstants.SCHEDULER_NAME);
        String schedulerStatus = globalOptions.get(MappingConstants.SCHEDULER_STATUS);
        if (Strings.isNullOrEmpty(schedulerStatus) || schedulerStatus.equalsIgnoreCase(MappingConstants.PARTIAL_SUCCESS)) {
            if (Strings.isNullOrEmpty(schedulerStatus)) {
                schedulerStatus = MappingConstants.SUCCESS;
            }
            schedulerStatus = MappingConstants.PARTIAL_SUCCESS;
        }
        jdbcTemplate.update(schedulerInsertSql, schedulerName, schedulerStartTime, new Timestamp(System.currentTimeMillis()), schedulerStatus);
    }
}
