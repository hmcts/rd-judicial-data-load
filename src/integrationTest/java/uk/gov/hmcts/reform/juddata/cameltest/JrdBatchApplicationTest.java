package uk.gov.hmcts.reform.juddata.cameltest;

import static net.logstash.logback.encoder.org.apache.commons.lang3.BooleanUtils.negate;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.JUDICIAL_REF_DATA_ORCHESTRATION;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.LEAF_ROUTE;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.ORCHESTRATED_ROUTE;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.IntegrationTestSupport.setSourcePath;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.file;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithError;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithSingleRecord;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.setSourceData;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateAppointmentFile;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateAuthorisationFile;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateDbRecordCountFor;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateUserProfileFile;

import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.JrdBatchIntegrationSupport;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.LeafIntegrationTestSupport;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.RestartingSpringJUnit4ClassRunner;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.SpringRestarter;
import uk.gov.hmcts.reform.juddata.config.LeafCamelConfig;
import uk.gov.hmcts.reform.juddata.config.ParentCamelConfig;
import uk.gov.hmcts.reform.juddata.configuration.BatchConfig;

@TestPropertySource(properties = {"spring.config.location=classpath:application-integration.yml,"
       + "classpath:application-leaf-integration.yml"})
@RunWith(RestartingSpringJUnit4ClassRunner.class)
@MockEndpoints("log:*")
@ContextConfiguration(classes = {ParentCamelConfig.class, LeafCamelConfig.class, CamelTestContextBootstrapper.class,
        JobLauncherTestUtils.class, BatchConfig.class}, initializers = ConfigFileApplicationContextInitializer.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = JpaRepositoriesAutoConfiguration.class)
@EnableTransactionManagement
@SqlConfig(dataSource = "dataSource", transactionManager = "txManager",
        transactionMode = SqlConfig.TransactionMode.ISOLATED)
public class JrdBatchApplicationTest extends JrdBatchIntegrationSupport {

    @BeforeClass
    public static void beforeAll() throws Exception {
        setSourcePath("classpath:archivalFiles", "archival.path");
        setSourcePath("classpath:sourceFiles", "active.path");
    }

    @Before
    public void init() {
        jdbcTemplate.execute(truncateAudit);
        SpringRestarter.getInstance().restart();
        camelContext.getGlobalOptions().put(ORCHESTRATED_ROUTE, JUDICIAL_REF_DATA_ORCHESTRATION);
        dataLoadUtil.setGlobalConstant(camelContext, JUDICIAL_REF_DATA_ORCHESTRATION);
        dataLoadUtil.setGlobalConstant(camelContext, LEAF_ROUTE);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/default-leaf-load.sql"})
    public void testTasklet() throws Exception {

        setSourceData(file);
        LeafIntegrationTestSupport.setSourceData(LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();

        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 2);
        validateDbRecordCountFor(jdbcTemplate, roleSql, 6);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/default-leaf-load.sql",
            "/testData/truncate-exception.sql"})
    public void testTaskletIdempotent() throws Exception {

        testTasklet();
        SpringRestarter.getInstance().restart();
        setSourceData(file);
        LeafIntegrationTestSupport.setSourceData(LeafIntegrationTestSupport.file);
        if (negate(judicialAuditServiceImpl.isAuditingCompleted())) {
            jobLauncherTestUtils.launchJob();
        }

        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 2);
        validateDbRecordCountFor(jdbcTemplate, roleSql, 6);
        validateDbRecordCountFor(jdbcTemplate, selectDataLoadSchedulerAudit, 2);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/default-leaf-load.sql"})
    public void testParentOrchestration() throws Exception {

        setSourceData(file);
        LeafIntegrationTestSupport.setSourceData(LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();

        validateUserProfileFile(jdbcTemplate, userProfileSql);
        validateAppointmentFile(jdbcTemplate, sqlChild1);
        validateAuthorisationFile(jdbcTemplate, sqlChild2);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/default-leaf-load.sql"})
    public void testParentOrchestrationFailure() throws Exception {

        setSourceData(fileWithError);
        LeafIntegrationTestSupport.setSourceData(LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();

        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 0);
        validateDbRecordCountFor(jdbcTemplate, sqlChild1, 0);
        validateDbRecordCountFor(jdbcTemplate, sqlChild2, 0);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/default-leaf-load.sql"})
    public void testParentOrchestrationSingleRecord() throws Exception {

        setSourceData(fileWithSingleRecord);
        LeafIntegrationTestSupport.setSourceData(LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();
        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 1);
        validateDbRecordCountFor(jdbcTemplate, sqlChild1, 1);
        validateDbRecordCountFor(jdbcTemplate, sqlChild2, 1);
    }


    @Test
    @Sql(scripts = {"/testData/truncate-leaf.sql"})
    public void testAllLeafs() throws Exception {
        setSourceData(file);
        LeafIntegrationTestSupport.setSourceData(LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();

        validateDbRecordCountFor(jdbcTemplate, roleSql, 5);
        validateDbRecordCountFor(jdbcTemplate, contractSql, 7);
        validateDbRecordCountFor(jdbcTemplate, baseLocationSql, 5);
        validateDbRecordCountFor(jdbcTemplate, regionSql, 5);
    }
}
