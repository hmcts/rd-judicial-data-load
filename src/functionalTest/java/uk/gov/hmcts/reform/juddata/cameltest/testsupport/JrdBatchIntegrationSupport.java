package uk.gov.hmcts.reform.juddata.cameltest.testsupport;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.data.ingestion.camel.processor.ExceptionProcessor;
import uk.gov.hmcts.reform.data.ingestion.camel.route.DataLoadRoute;
import uk.gov.hmcts.reform.data.ingestion.camel.service.AuditServiceImpl;
import uk.gov.hmcts.reform.data.ingestion.camel.service.IEmailService;
import uk.gov.hmcts.reform.data.ingestion.camel.util.DataLoadUtil;

import java.util.Date;
import java.util.List;
import javax.sql.DataSource;

import static net.logstash.logback.encoder.org.apache.commons.lang3.BooleanUtils.isNotTrue;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.SCHEDULER_START_TIME;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.JUDICIAL_REF_DATA_ORCHESTRATION;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.LEAF_ROUTE;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.ORCHESTRATED_ROUTE;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.deleteBlobs;

@ExtendWith(SpringExtension.class)
public abstract class JrdBatchIntegrationSupport {

    public static final String FILE_STATUS = "status";

    protected boolean notDeletionFlag = false;

    @Autowired
    protected CamelContext camelContext;

    @Autowired
    @Qualifier("springJdbcTemplate")
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
    protected String appointmentSql;

    @Value("${child-select-child2-sql}")
    protected String authorizationSql;

    @Value("${service-code-sql}")
    protected String serviceCodeSql;

    @Value("${judicial-office-appointment-service-code-sql}")
    protected String judicialOfficeAppointmentServiceCodeSql;

    @Value("${epimms-id-sql}")
    protected String epimmsIdSql;

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

    protected TestContextManager testContextManager;

    @Autowired
    DataSource dataSource;

    @BeforeEach
    public void setUpStringContext() throws Exception {

        new TestContextManager(getClass()).prepareTestInstance(this);
        testContextManager = new TestContextManager(getClass());
        testContextManager.prepareTestInstance(this);
        SpringStarter.getInstance().init(testContextManager);

        executeScripts("testData/truncate-all.sql");
        camelContext.getGlobalOptions().put(ORCHESTRATED_ROUTE, JUDICIAL_REF_DATA_ORCHESTRATION);
        dataLoadUtil.setGlobalConstant(camelContext, JUDICIAL_REF_DATA_ORCHESTRATION);
        dataLoadUtil.setGlobalConstant(camelContext, LEAF_ROUTE);

        camelContext.getGlobalOptions()
            .put(SCHEDULER_START_TIME, String.valueOf(new Date(System.currentTimeMillis()).getTime()));
        executeScripts("testData/default-leaf-load.sql");
    }


    public void executeScripts(String path) {
        var a = new FileSystemResource(getClass().getClassLoader().getResource(path)
            .getPath());
        var resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScripts(a);
        resourceDatabasePopulator.execute(dataSource);
    }


    @BeforeAll
    public static void setupBlobProperties() throws Exception {
        if ("preview".equalsIgnoreCase(System.getenv("execution_environment"))) {
            System.setProperty("azure.storage.account-key", System.getenv("BLOB_ACCOUNT_KEY"));
            System.setProperty("azure.storage.account-name", System.getenv("BLOB_ACCOUNT_NAME"));
            //serviceBusSecretString
            System.setProperty("JRD_ASB_CONNECTION_KEY_STR", System.getenv("serviceBusSecretString"));
            System.setProperty("RD_LD_SDK_KEY", System.getenv("RD_LD_SDK_KEY"));
        } else {
            System.setProperty("azure.storage.account-key", System.getenv("ACCOUNT_KEY"));
            System.setProperty("azure.storage.account-name", System.getenv("ACCOUNT_NAME"));
        }
        System.setProperty("ENV_NAME", System.getenv("execution_environment"));
        System.setProperty("azure.storage.container-name", "jud-ref-data");
        System.setProperty("OAUTH2_CLIENT_AUTH", System.getenv("OAUTH2_CLIENT_AUTH"));
        System.setProperty("OAUTH2_AUTH", System.getenv("OAUTH2_AUTH"));
    }

    @AfterEach
    public void cleanUp() throws Exception {
        if (isNotTrue(notDeletionFlag)) {
            deleteBlobs(jrdBlobSupport, archivalFileNames);
        }
    }
}
