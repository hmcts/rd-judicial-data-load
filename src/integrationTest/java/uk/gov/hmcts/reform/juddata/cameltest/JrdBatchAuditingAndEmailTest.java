package uk.gov.hmcts.reform.juddata.cameltest;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.JUDICIAL_USER_PROFILE_ORCHESTRATION;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.LEAF_ROUTE;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.ORCHESTRATED_ROUTE;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.IntegrationTestSupport.setSourcePath;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.file;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithError;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.setSourceData;

import java.util.List;
import java.util.Map;

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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.gov.hmcts.reform.juddata.camel.util.MappingConstants;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.JrdBatchIntegrationSupport;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.LeafIntegrationTestSupport;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.RestartingSpringJUnit4ClassRunner;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.SpringRestarter;
import uk.gov.hmcts.reform.juddata.config.LeafCamelConfig;
import uk.gov.hmcts.reform.juddata.config.ParentCamelConfig;
import uk.gov.hmcts.reform.juddata.configuration.BatchConfig;

@TestPropertySource(properties = {"spring.config.location=classpath:application-integration.yml,classpath:application-leaf-integration.yml"})
@RunWith(RestartingSpringJUnit4ClassRunner.class)
@MockEndpoints("log:*")
@ContextConfiguration(classes = {ParentCamelConfig.class, LeafCamelConfig.class, CamelTestContextBootstrapper.class, JobLauncherTestUtils.class, BatchConfig.class}, initializers = ConfigFileApplicationContextInitializer.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = JpaRepositoriesAutoConfiguration.class)
@EnableTransactionManagement
@SqlConfig(dataSource = "dataSource", transactionManager = "txManager", transactionMode = SqlConfig.TransactionMode.ISOLATED)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JrdBatchAuditingAndEmailTest extends JrdBatchIntegrationSupport {

    @BeforeClass
    public static void beforeAll() throws Exception {
        setSourcePath("classpath:archivalFiles", "archival.path");
        setSourcePath("classpath:sourceFiles", "active.path");

    }

    @Before
    public void init() {
        jdbcTemplate.execute(truncateAudit);
        SpringRestarter.getInstance().restart();
        camelContext.getGlobalOptions().put(ORCHESTRATED_ROUTE, JUDICIAL_USER_PROFILE_ORCHESTRATION);
        dataLoadUtil.setGlobalConstant(camelContext, JUDICIAL_USER_PROFILE_ORCHESTRATION);
        dataLoadUtil.setGlobalConstant(camelContext, LEAF_ROUTE);
    }

    @Test
    public void testParentOrchestrationSchedulerFailure() throws Exception {
        setSourceData(fileWithError);
        LeafIntegrationTestSupport.setSourceData(LeafIntegrationTestSupport.file);
        leafTableRoute.startRoute();
        parentRoute.startRoute();

        jobLauncherTestUtils.launchJob();

        auditProcessingService.auditSchedulerStatus(camelContext);
        List<Map<String, Object>> dataLoadSchedulerAudit = jdbcTemplate.queryForList(schedulerInsertJrdSqlFailure);
        assertEquals(dataLoadSchedulerAudit.get(0).get(DB_SCHEDULER_STATUS), MappingConstants.FAILURE);
    }

    @Test
    public void testParentOrchestrationSchedulerSuccess() throws Exception {

        setSourceData(file);

        LeafIntegrationTestSupport.setSourceData(LeafIntegrationTestSupport.file);
        leafTableRoute.startRoute();
        parentRoute.startRoute();

        jobLauncherTestUtils.launchJob();

        auditProcessingService.auditSchedulerStatus(camelContext);

        List<Map<String, Object>> dataLoadSchedulerAudit = jdbcTemplate.queryForList(schedulerInsertJrdSqlSuccess);
        assertEquals(dataLoadSchedulerAudit.get(0).get(DB_SCHEDULER_STATUS), MappingConstants.SUCCESS);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/truncate-exception.sql",
            "/testData/default-leaf-load.sql"})
    public void testParentOrchestrationFailureEmail() throws Exception {
        setSourceData(fileWithError);
        LeafIntegrationTestSupport.setSourceData(LeafIntegrationTestSupport.file);
        camelContext.getGlobalOptions().put(MappingConstants.ORCHESTRATED_ROUTE, JUDICIAL_USER_PROFILE_ORCHESTRATION);
        setField(emailService, "mailEnabled", Boolean.FALSE);
        leafTableRoute.startRoute();
        parentRoute.startRoute();
        jobLauncherTestUtils.launchJob();
        verify(emailService, times(1)).sendEmail(any(String.class));
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/truncate-exception.sql",
            "/testData/default-leaf-load.sql"})
    public void testParentOrchestrationSuccessEmail() throws Exception {
        setSourceData(file);
        LeafIntegrationTestSupport.setSourceData(LeafIntegrationTestSupport.file);
        camelContext.getGlobalOptions().put(MappingConstants.ORCHESTRATED_ROUTE, JUDICIAL_USER_PROFILE_ORCHESTRATION);
        leafTableRoute.startRoute();
        parentRoute.startRoute();
        jobLauncherTestUtils.launchJob();
        verify(emailService, times(0)).sendEmail(any(String.class));
    }
}
