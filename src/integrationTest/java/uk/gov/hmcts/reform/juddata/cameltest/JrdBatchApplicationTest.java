package uk.gov.hmcts.reform.juddata.cameltest;

import static net.logstash.logback.encoder.org.apache.commons.lang3.BooleanUtils.negate;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.JUDICIAL_REF_DATA_ORCHESTRATION;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.LEAF_ROUTE;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.ORCHESTRATED_ROUTE;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.IntegrationTestSupport.setSourcePath;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.file;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithError;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithSingleRecord;
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
        List<Map<String, Object>> judicialUserProfileList = jdbcTemplate.queryForList(sql);
        assertEquals(judicialUserProfileList.size(), 2);
        List<Map<String, Object>> judicialUserRoleType = jdbcTemplate.queryForList(roleSql);
        assertNotEquals(0, judicialUserRoleType.size());
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

        List<Map<String, Object>> judicialUserProfileList = jdbcTemplate.queryForList(sql);
        assertEquals(judicialUserProfileList.size(), 2);
        List<Map<String, Object>> judicialUserRoleType = jdbcTemplate.queryForList(roleSql);
        assertNotEquals(judicialUserRoleType.size(), 0);
        List<Map<String, Object>> auditList = jdbcTemplate.queryForList(selectDataLoadSchedulerAudit);
        assertEquals(2, auditList.size());
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/default-leaf-load.sql"})
    public void testParentOrchestration() throws Exception {

        setSourceData(file);
        LeafIntegrationTestSupport.setSourceData(LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();

        List<Map<String, Object>> judicialUserProfileList = jdbcTemplate.queryForList(sql);
        assertEquals(judicialUserProfileList.size(), 2);
        assertNotNull(judicialUserProfileList.get(0));
        assertNotNull(judicialUserProfileList.get(1));
        assertEquals(judicialUserProfileList.get(0).get("elinks_id"), "1");
        assertEquals(judicialUserProfileList.get(1).get("elinks_id"), "2");
        assertEquals(judicialUserProfileList.get(0).get("email_id"), "joe.bloggs@ejudiciary.net");
        assertEquals(judicialUserProfileList.get(1).get("email_id"), "jo1e.bloggs@ejudiciary.net");

        List<Map<String, Object>> judicialAppointmentList = jdbcTemplate.queryForList(sqlChild1);
        assertEquals(judicialAppointmentList.size(), 2);
        assertNotNull(judicialAppointmentList.get(0));
        assertNotNull(judicialAppointmentList.get(1));
        assertNotNull(judicialAppointmentList.get(0).get("judicial_office_appointment_id"));
        assertNotNull(judicialAppointmentList.get(0).get("judicial_office_appointment_id"));
        assertEquals("1", judicialAppointmentList.get(0).get("elinks_id"));
        assertEquals("2", judicialAppointmentList.get(1).get("elinks_id"));
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/default-leaf-load.sql"})
    public void testParentOrchestrationFailure() throws Exception {

        setSourceData(fileWithError);
        LeafIntegrationTestSupport.setSourceData(LeafIntegrationTestSupport.file);


        jobLauncherTestUtils.launchJob();
        List<Map<String, Object>> judicialUserProfileList = jdbcTemplate.queryForList(sql);
        assertEquals(0, judicialUserProfileList.size());

        List<Map<String, Object>> judicialAppointmentList = jdbcTemplate.queryForList(sqlChild1);
        assertEquals(0, judicialAppointmentList.size());
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/default-leaf-load.sql"})
    public void testParentOrchestrationSingleRecord() throws Exception {

        setSourceData(fileWithSingleRecord);
        LeafIntegrationTestSupport.setSourceData(LeafIntegrationTestSupport.file);


        jobLauncherTestUtils.launchJob();
        List<Map<String, Object>> judicialUserProfileList = jdbcTemplate.queryForList(sql);
        assertEquals(1, judicialUserProfileList.size());

        List<Map<String, Object>> judicialAppointmentList = jdbcTemplate.queryForList(sqlChild1);
        assertEquals(1, judicialAppointmentList.size());
    }


    @Test
    @Sql(scripts = {"/testData/truncate-leaf.sql"})
    public void testAllLeafs() throws Exception {
        setSourceData(file);
        LeafIntegrationTestSupport.setSourceData(LeafIntegrationTestSupport.file);


        jobLauncherTestUtils.launchJob();

        List<Map<String, Object>> judicialUserRoleType = jdbcTemplate.queryForList(roleSql);
        assertEquals(5, judicialUserRoleType.size());

        List<Map<String, Object>> judicialContractType = jdbcTemplate.queryForList(contractSql);
        assertEquals(7, judicialContractType.size());

        List<Map<String, Object>> judicialBaseLocationType = jdbcTemplate.queryForList(baseLocationSql);
        assertEquals(5, judicialBaseLocationType.size());

        List<Map<String, Object>> judicialRegionType = jdbcTemplate.queryForList(regionSql);
        assertEquals(5, judicialRegionType.size());
    }
}
