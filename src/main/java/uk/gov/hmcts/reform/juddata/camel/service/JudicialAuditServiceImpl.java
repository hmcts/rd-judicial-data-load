package uk.gov.hmcts.reform.juddata.camel.service;

import static java.lang.System.currentTimeMillis;

import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.FILE_NAME;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.SCHEDULER_NAME;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.SCHEDULER_START_TIME;

import java.sql.Timestamp;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import uk.gov.hmcts.reform.data.ingestion.camel.service.AuditServiceImpl;

@Slf4j
@Component
public class JudicialAuditServiceImpl extends AuditServiceImpl {

    @Value("${invalid-exception-sql}")
    String invalidExceptionSql;

    /**
     * Updates scheduler exceptions.
     *
     * @param camelContext CamelContext
     * @return void
     */
    public void auditException(final CamelContext camelContext, String exceptionMessage) {
        Map<String, String> globalOptions = camelContext.getGlobalOptions();
        Timestamp schedulerStartTime = new Timestamp(Long.valueOf((globalOptions.get(SCHEDULER_START_TIME))));
        String schedulerName = globalOptions.get(SCHEDULER_NAME);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();

        Object[] params = new Object[]{camelContext.getGlobalOptions().get(FILE_NAME),
            schedulerStartTime, schedulerName, exceptionMessage, new Timestamp(currentTimeMillis())};
        //separate transaction manager required for auditing as it is independent form route
        //Transaction
        jdbcTemplate.update(invalidExceptionSql, params);
        TransactionStatus status = platformTransactionManager.getTransaction(def);
        platformTransactionManager.commit(status);
    }
}
