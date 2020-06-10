package uk.gov.hmcts.reform.juddata.cameltest;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.JUDICIAL_USER_PROFILE_ORCHESTRATION;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.LEAF_ROUTE;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.ORCHESTRATED_ROUTE;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.PARTIAL_SUCCESS;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.IntegrationTestSupport.setSourcePath;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.LeafIntegrationTestSupport.file_error;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.LeafIntegrationTestSupport.file_jsr_error;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.file;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithElinkIdMissing;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithInvalidHeader;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithInvalidJsr;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithInvalidJsrExceedsThreshold;
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
public class JrdBatchTestValidationTest extends JrdBatchIntegrationSupport {

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
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/default-leaf-load.sql", "/testData/truncate-exception.sql"})
    public void testTaskletException() throws Exception {
        setSourceData(fileWithElinkIdMissing);
        LeafIntegrationTestSupport.setSourceData(LeafIntegrationTestSupport.file);
        leafTableRoute.startRoute();
        parentRoute.startRoute();

        jobLauncherTestUtils.launchJob();
        List<Map<String, Object>> judicialUserProfileList = jdbcTemplate.queryForList(sql);
        assertEquals(judicialUserProfileList.size(), 0);

        List<Map<String, Object>> exceptionList = jdbcTemplate.queryForList(exceptionQuery);

        for (int count = 0; count < 1; count++) {
            assertNotNull(exceptionList.get(0).get("scheduler_start_time"));
            assertNotNull(exceptionList.get(0).get("error_description"));
            assertNotNull(exceptionList.get(0).get("scheduler_name"));
            assertNotNull(exceptionList.get(0).get("updated_timestamp"));
        }
        assertEquals(exceptionList.size(), 1);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/truncate-exception.sql", "/testData/default-leaf-load.sql"})
    public void testParentOrchestrationInvalidHeaderRollback() throws Exception {
        setSourceData(fileWithInvalidHeader);
        LeafIntegrationTestSupport.setSourceData(LeafIntegrationTestSupport.file);
        leafTableRoute.startRoute();
        parentRoute.startRoute();

        jobLauncherTestUtils.launchJob();
        List<Map<String, Object>> judicialUserProfileList = jdbcTemplate.queryForList(sql);
        assertEquals(judicialUserProfileList.size(), 0);

        List<Map<String, Object>> judicialAppointmentList = jdbcTemplate.queryForList(sqlChild1);
        assertEquals(judicialAppointmentList.size(), 0);

        List<Map<String, Object>> exceptionList = jdbcTemplate.queryForList(exceptionQuery);
        assertNotNull(exceptionList.get(0).get("file_name"));
        assertNotNull(exceptionList.get(0).get("scheduler_start_time"));
        assertNotNull(exceptionList.get(0).get("error_description"));
        assertNotNull(exceptionList.get(0).get("updated_timestamp"));
        assertEquals(exceptionList.size(), 1);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-leaf.sql"})
    public void testLeafFailuresRollbackAndKeepExistingState() throws Exception {
        setSourceData(file);
        LeafIntegrationTestSupport.setSourceData(LeafIntegrationTestSupport.file);
        leafTableRoute.startRoute();
        parentRoute.startRoute();
        jobLauncherTestUtils.launchJob();

        setSourceData(file);
        LeafIntegrationTestSupport.setSourceData(file_error);
        camelContext.getGlobalOptions().put(MappingConstants.ORCHESTRATED_ROUTE, JUDICIAL_USER_PROFILE_ORCHESTRATION);
        jobLauncherTestUtils.launchJob();

        List<Map<String, Object>> judicialUserRoleType = jdbcTemplate.queryForList(roleSql);
        assertEquals(judicialUserRoleType.size(), 5);

        List<Map<String, Object>> judicialContractType = jdbcTemplate.queryForList(contractSql);
        assertEquals(judicialContractType.size(), 7);

        List<Map<String, Object>> judicialBaseLocationType = jdbcTemplate.queryForList(baseLocationSql);
        assertEquals(judicialBaseLocationType.size(), 5);

        List<Map<String, Object>> judicialRegionType = jdbcTemplate.queryForList(regionSql);
        assertEquals(judicialRegionType.size(), 5);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/truncate-exception.sql",
            "/testData/default-leaf-load.sql"})
    public void testParentOrchestrationJsrAuditTestAndPartialSuccess() throws Exception {
        setSourceData(fileWithInvalidJsr);
        LeafIntegrationTestSupport.setSourceData(LeafIntegrationTestSupport.file);
        leafTableRoute.startRoute();
        parentRoute.startRoute();
        jobLauncherTestUtils.launchJob();

        List<Map<String, Object>> judicialUserProfileList = jdbcTemplate.queryForList(sql);
        assertEquals(judicialUserProfileList.get(0).get("elinks_id"), "1");
        assertEquals(judicialUserProfileList.get(1).get("elinks_id"), "2");
        assertEquals(judicialUserProfileList.get(0).get("email_id"), "joe.bloggs@ejudiciary.net");
        assertEquals(judicialUserProfileList.get(1).get("email_id"), "jo1e.bloggs@ejudiciary.net");
        assertEquals(judicialUserProfileList.size(), 2);

        List<Map<String, Object>> judicialAppointmentList = jdbcTemplate.queryForList(sqlChild1);
        assertNotNull(judicialAppointmentList.get(0).get("judicial_office_appointment_id"));
        assertNotNull(judicialAppointmentList.get(0).get("judicial_office_appointment_id"));
        assertEquals(judicialAppointmentList.get(0).get("elinks_id"), "1");
        assertEquals(judicialAppointmentList.get(1).get("elinks_id"), "2");
        assertEquals(judicialAppointmentList.size(), 2);

        List<Map<String, Object>> exceptionList = jdbcTemplate.queryForList(exceptionQuery);

        for (int count = 0; count < 6; count++) {
            assertNotNull(exceptionList.get(0).get("table_name"));
            assertNotNull(exceptionList.get(0).get("scheduler_start_time"));
            assertNotNull(exceptionList.get(0).get("key"));
            assertNotNull(exceptionList.get(0).get("field_in_error"));
            assertNotNull(exceptionList.get(0).get("error_description"));
            assertNotNull(exceptionList.get(0).get("updated_timestamp"));
        }
        assertEquals(exceptionList.size(), 5);

        List<Map<String, Object>> dataLoadSchedulerAudit = jdbcTemplate.queryForList(schedulerInsertJrdSqlPartialSuccess);
        assertEquals(dataLoadSchedulerAudit.get(0).get(DB_SCHEDULER_STATUS), PARTIAL_SUCCESS);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/truncate-exception.sql",
            "/testData/default-leaf-load.sql"})
    public void testParentOrchestrationJsrExceedsThresholdAuditTest() throws Exception {
        setSourceData(fileWithInvalidJsrExceedsThreshold);
        LeafIntegrationTestSupport.setSourceData(LeafIntegrationTestSupport.file);
        parentRoute.startRoute();
        leafTableRoute.startRoute();
        jobLauncherTestUtils.launchJob();

        List<Map<String, Object>> exceptionList = jdbcTemplate.queryForList(exceptionQuery);
        //Jsr exception exceeds threshold limit in

        assertThat(exceptionList.get(exceptionList.size() - 1).get("error_description").toString(),
                containsString("Jsr exception exceeds threshold limit"));
    }

    @Test
    @Sql(scripts = {"/testData/truncate-leaf.sql", "/testData/truncate-exception.sql"})
    public void testLeafFailuresInvalidHeader() throws Exception {
        setSourceData(file);
        LeafIntegrationTestSupport.setSourceData(file_error);
        leafTableRoute.startRoute();
        parentRoute.startRoute();
        jobLauncherTestUtils.launchJob();

        List<Map<String, Object>> judicialUserRoleType = jdbcTemplate.queryForList(roleSql);
        assertEquals(judicialUserRoleType.size(), 0);

        List<Map<String, Object>> judicialContractType = jdbcTemplate.queryForList(contractSql);
        assertEquals(judicialContractType.size(), 0);

        List<Map<String, Object>> judicialBaseLocationType = jdbcTemplate.queryForList(baseLocationSql);
        assertEquals(judicialBaseLocationType.size(), 0);

        List<Map<String, Object>> judicialRegionType = jdbcTemplate.queryForList(regionSql);
        assertEquals(judicialRegionType.size(), 0);

        List<Map<String, Object>> exceptionList = jdbcTemplate.queryForList(exceptionQuery);
        assertNotNull(exceptionList.get(0).get("file_name"));
        assertNotNull(exceptionList.get(0).get("scheduler_start_time"));
        assertNotNull(exceptionList.get(0).get("error_description"));
        assertNotNull(exceptionList.get(0).get("updated_timestamp"));
        assertEquals(exceptionList.size(), 1);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-leaf.sql", "/testData/truncate-exception.sql"})
    public void testLeafFailuresInvalidJsr() throws Exception {
        setSourceData(file);
        LeafIntegrationTestSupport.setSourceData(file_jsr_error);
        leafTableRoute.startRoute();
        parentRoute.startRoute();
        jobLauncherTestUtils.launchJob();

        List<Map<String, Object>> judicialUserRoleType = jdbcTemplate.queryForList(roleSql);
        assertEquals(judicialUserRoleType.size(), 3);
        assertEquals(judicialUserRoleType.get(0).get("role_id"), "1");
        assertEquals(judicialUserRoleType.get(1).get("role_id"), "3");
        assertEquals(judicialUserRoleType.get(2).get("role_id"), "7");

        assertEquals(judicialUserRoleType.get(0).get("role_desc_en"), "Magistrate");
        assertEquals(judicialUserRoleType.get(1).get("role_desc_en"), "Advisory Committee Member - Non Magistrate");
        assertEquals(judicialUserRoleType.get(2).get("role_desc_en"), "MAGS - AC Admin User");

        List<Map<String, Object>> judicialContractType = jdbcTemplate.queryForList(contractSql);
        assertEquals(judicialContractType.size(), 5);
        assertEquals(judicialContractType.get(0).get("contract_type_id"), "1");
        assertEquals(judicialContractType.get(1).get("contract_type_id"), "3");
        assertEquals(judicialContractType.get(2).get("contract_type_id"), "5");
        assertEquals(judicialContractType.get(3).get("contract_type_id"), "6");
        assertEquals(judicialContractType.get(4).get("contract_type_id"), "7");

        List<Map<String, Object>> judicialBaseLocationType = jdbcTemplate.queryForList(baseLocationSql);
        assertEquals(judicialBaseLocationType.get(0).get("base_location_id"), "1");
        assertEquals(judicialBaseLocationType.get(1).get("base_location_id"), "2");
        assertEquals(judicialBaseLocationType.get(2).get("base_location_id"), "5");
        assertEquals(judicialBaseLocationType.size(), 3);

        List<Map<String, Object>> judicialRegionType = jdbcTemplate.queryForList(regionSql);
        assertEquals(judicialRegionType.get(0).get("region_id"), "1");
        assertEquals(judicialRegionType.get(1).get("region_id"), "4");
        assertEquals(judicialRegionType.get(2).get("region_id"), "5");
        assertEquals(judicialRegionType.size(), 3);

        List<Map<String, Object>> exceptionList = jdbcTemplate.queryForList(exceptionQuery);
        for (int count = 0; count < 8; count++) {
            assertNotNull(exceptionList.get(count).get("table_name"));
            assertNotNull(exceptionList.get(count).get("scheduler_start_time"));
            assertNotNull(exceptionList.get(count).get("key"));
            assertNotNull(exceptionList.get(count).get("field_in_error"));
            assertNotNull(exceptionList.get(count).get("error_description"));
            assertNotNull(exceptionList.get(count).get("updated_timestamp"));
        }
        assertEquals(exceptionList.size(), 9);
    }
}
