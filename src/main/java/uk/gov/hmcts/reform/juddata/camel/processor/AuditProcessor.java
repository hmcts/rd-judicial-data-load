package uk.gov.hmcts.reform.juddata.camel.processor;

import static java.util.Objects.isNull;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.PARTIAL_SUCCESS;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SCHEDULER_NAME;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SCHEDULER_START_TIME;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SCHEDULER_STATUS;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SUCCESS;

import com.google.common.base.Strings;

import java.sql.Timestamp;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Slf4j
@Component
public class AuditProcessor implements Processor {


    @Value("${audit-enable}")
    Boolean auditEnabled;

    @Autowired
    @Qualifier("springJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @Value("${scheduler-insert-sql}")
    String schedulerInsertSql;

    @Autowired
    @Qualifier("springJdbcTransactionManager")
    PlatformTransactionManager platformTransactionManager;


    /**
     * Update the Audit table for Scheduler.
     *
     * @param exchange the message exchange
     * @throws Exception if an internal processing error has occurred.
     */
    @Override
    public void process(Exchange exchange) {
        if (auditEnabled.booleanValue()) {
            schedulerAuditUpdate(exchange);
        }
    }

    private void schedulerAuditUpdate(final Exchange exchange) {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("Auditing scheduler details");

        Map<String, String> globalOptions = exchange.getContext().getGlobalOptions();

        Timestamp schedulerStartTime =  new Timestamp(Long.valueOf((globalOptions.get(SCHEDULER_START_TIME))));
        String schedulerName = globalOptions.get(SCHEDULER_NAME);
        String schedulerStatus = isNull(globalOptions.get(SCHEDULER_STATUS))
                ? (String) exchange.getIn().getHeader(SCHEDULER_STATUS)
                : globalOptions.get(SCHEDULER_STATUS);
        if (Strings.isNullOrEmpty(schedulerStatus) || schedulerStatus.equalsIgnoreCase(PARTIAL_SUCCESS)) {
            if (Strings.isNullOrEmpty(schedulerStatus)) {
                schedulerStatus = SUCCESS;
            } else {
                schedulerStatus = PARTIAL_SUCCESS;
            }
        }
        jdbcTemplate.update(schedulerInsertSql, schedulerName, schedulerStartTime, new Timestamp(System.currentTimeMillis()), schedulerStatus);
        TransactionStatus status = platformTransactionManager.getTransaction(def);
        platformTransactionManager.commit(status);
    }
}
