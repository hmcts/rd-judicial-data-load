package uk.gov.hmcts.reform.elinks.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import uk.gov.hmcts.reform.data.ingestion.DataIngestionLibraryRunner;
import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.FileStatus;
import uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static java.lang.Long.parseLong;
import static java.lang.System.currentTimeMillis;
import static java.util.stream.Collectors.toList;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.DataLoadUtil.getFileDetails;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.DataLoadUtil.isFileExecuted;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.SCHEDULER_NAME;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.SCHEDULER_START_TIME;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.TABLE_NAME;

@Component
@Slf4j
public class JrdElinkDataIngestionLibraryRunner extends DataIngestionLibraryRunner {


    @Value("${archival-file-names}")
    List<String> archivalFileNames;

    @Autowired
    @Qualifier("springJdbcTemplate")
    protected JdbcTemplate jdbcTemplate;

    @Value("${scheduler-insert-sql}")
    protected String schedulerInsertSql;

    @Autowired
    @Qualifier("springJdbcTransactionManager")
    protected PlatformTransactionManager platformTransactionManager;

    @Value("${invalid-exception-sql}")
    String invalidExceptionSql;


    /**
     * Capture and log scheduler details with file status.
     *
     * @param camelContext CamelContext
     */
    public void auditSchedulerStatus(final CamelContext camelContext) {

        List<FileStatus> fileStatuses = archivalFileNames.stream()
            .filter(file -> isFileExecuted(camelContext, file))
            .map(s -> getFileDetails(camelContext, s)).collect(toList());

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("Auditing scheduler details");

        Map<String, String> globalOptions = camelContext.getGlobalOptions();
        String schedulerName = globalOptions.get(SCHEDULER_NAME);

        Timestamp schedulerStartTime = new Timestamp(parseLong((globalOptions.get(SCHEDULER_START_TIME))));


        for (FileStatus fileStatus : fileStatuses) {
            String fileAuditStatus = StringUtils.isNotEmpty(fileStatus.getAuditStatus())
                ? fileStatus.getAuditStatus() : MappingConstants.SUCCESS;
            jdbcTemplate.update(schedulerInsertSql, schedulerName, fileStatus.getFileName(),
                schedulerStartTime, new Timestamp(currentTimeMillis()), fileAuditStatus);
        }

        TransactionStatus status = platformTransactionManager.getTransaction(def);
        platformTransactionManager.commit(status);
    }

    /**
     * Capture & log scheduler exceptions.
     *
     * @param camelContext CamelContext
     */
    public void auditException(final CamelContext camelContext, String exceptionMessage) {
        Map<String, String> globalOptions = camelContext.getGlobalOptions();
        Timestamp schedulerStartTime = new Timestamp(parseLong((globalOptions.get(SCHEDULER_START_TIME))));
        String schedulerName = globalOptions.get(SCHEDULER_NAME);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        Object[] params = new Object[]{camelContext.getGlobalOptions().get(TABLE_NAME),
            schedulerStartTime, schedulerName, exceptionMessage, new Timestamp(currentTimeMillis())};

        //separate transaction manager required for auditing as it is independent form route
        //Transaction
        jdbcTemplate.update(invalidExceptionSql, params);
        TransactionStatus status = platformTransactionManager.getTransaction(def);
        platformTransactionManager.commit(status);
    }

}
