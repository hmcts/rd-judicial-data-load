package uk.gov.hmcts.reform.juddata.cameltest.testsupport;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.junit.After;
import org.junit.BeforeClass;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import uk.gov.hmcts.reform.data.ingestion.camel.processor.ExceptionProcessor;
import uk.gov.hmcts.reform.data.ingestion.camel.route.DataLoadRoute;
import uk.gov.hmcts.reform.data.ingestion.camel.service.AuditServiceImpl;
import uk.gov.hmcts.reform.data.ingestion.camel.service.IEmailService;
import uk.gov.hmcts.reform.data.ingestion.camel.util.DataLoadUtil;

import java.util.List;

import static net.logstash.logback.encoder.org.apache.commons.lang3.BooleanUtils.isNotTrue;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.deleteBlobs;

public abstract class JrdBatchIntegrationSupport {


    public static final String FILE_STATUS = "status";

    protected boolean notDeletionFlag = false;

    @Autowired
    protected CamelContext camelContext;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected DataLoadRoute parentRoute;

    @Value("${start-route}")
    protected String startRoute;

    @Autowired
    protected ProducerTemplate producerTemplate;

    @Value("${parent-select-jrd-sql}")
    protected String userProfileSql;

    @Value("${child-select-child1-sql}")
    protected String sqlChild1;

    @Value("${child-select-child2-sql}")
    protected String sqlChild2;

    @Value("${archival-cred}")
    protected String archivalCred;

    @Value("${select-dataload-schedular-audit}")
    protected String selectDataLoadSchedulerAudit;

    @Value("${scheduler-insert-sql}")
    protected String schedulerInsertJrdSql;

    @Value("${select-dataload-scheduler-audit-failure}")
    protected String schedulerInsertJrdSqlFailure;

    @Value("${select-dataload-scheduler-audit-partial-success}")
    protected String schedulerInsertJrdSqlPartialSuccess;

    @Value("${select-dataload-scheduler-audit-success}")
    protected String schedulerInsertJrdSqlSuccess;

    @Value("${audit-enable}")
    protected Boolean auditEnable;

    @Autowired
    protected DataLoadUtil dataLoadUtil;

    @Autowired
    protected ExceptionProcessor exceptionProcessor;

    @Autowired
    protected IEmailService emailService;

    @Autowired
    protected AuditServiceImpl judicialAuditServiceImpl;

    @Value("${base-location-select-jrd-sql}")
    protected String baseLocationSql;

    @Value("${region-select-jrd-sql}")
    protected String regionSql;

    @Value("${contract-select-jrd-sql}")
    protected String contractSql;

    @Value("${role-select-jrd-sql}")
    protected String roleSql;


    @Value("${start-leaf-route}")
    protected String startLeafRoute;

    @Autowired
    protected JobLauncherTestUtils jobLauncherTestUtils;


    @Value("${exception-select-query}")
    protected String exceptionQuery;

    @Value("${truncate-audit}")
    protected String truncateAudit;

    @Autowired
    protected JrdBlobSupport jrdBlobSupport;

    @Value("${archival-file-names}")
    protected List<String> archivalFileNames;

    @BeforeClass
    public static void setupBlobProperties() throws Exception {
        System.setProperty("azure.storage.account-key",
                "61Sb95N8fFF5PNn/Gn5FjJFUg6wKJ/j6IWa0URdnAWnmjtluRPpJDk8uA3Zrx+u9gzUA8nF56rVJm1jl5dgTnQ==");
        System.setProperty("azure.storage.account-name", "rdpreview");
        System.setProperty("azure.storage.container-name", "jud-ref-data");
    }

    @After
    public void cleanUp() throws Exception {
        if (isNotTrue(notDeletionFlag)) {
            deleteBlobs(jrdBlobSupport, archivalFileNames);
        }
    }
}
