
package uk.gov.hmcts.reform.juddata.cameltest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.PARTIAL_SUCCESS;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.JUDICIAL_REF_DATA_ORCHESTRATION;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.LEAF_ROUTE;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.ORCHESTRATED_ROUTE;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.file;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithAuthElinkIdMissing;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithAuthorisationInvalidHeader;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithElinkIdInvalidInParent;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithElinkIdMissing;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithInvalidHeader;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithInvalidJsr;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithInvalidJsrExceedsThreshold;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.uploadBlobs;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateDbRecordCountFor;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateExceptionDbRecordCount;

import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Before;
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
import uk.gov.hmcts.reform.data.ingestion.configuration.AzureBlobConfig;
import uk.gov.hmcts.reform.data.ingestion.configuration.StorageCredentials;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.JrdBatchIntegrationSupport;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.LeafIntegrationTestSupport;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.RestartingSpringJUnit4ClassRunner;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.SpringRestarter;
import uk.gov.hmcts.reform.juddata.config.LeafCamelConfig;
import uk.gov.hmcts.reform.juddata.config.ParentCamelConfig;
import uk.gov.hmcts.reform.juddata.configuration.BatchConfig;
import java.util.List;
import java.util.Map;

@TestPropertySource(properties = {"spring.config.location=classpath:application-integration.yml,"
        + "classpath:application-leaf-integration.yml"})
@RunWith(RestartingSpringJUnit4ClassRunner.class)
@MockEndpoints("log:*")
@ContextConfiguration(classes = {ParentCamelConfig.class, LeafCamelConfig.class, CamelTestContextBootstrapper.class,
        JobLauncherTestUtils.class, BatchConfig.class, AzureBlobConfig.class, StorageCredentials.class},
        initializers = ConfigFileApplicationContextInitializer.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = JpaRepositoriesAutoConfiguration.class)
@EnableTransactionManagement
@SqlConfig(dataSource = "dataSource", transactionManager = "txManager",
        transactionMode = SqlConfig.TransactionMode.ISOLATED)
public class JrdBatchTestValidationTest extends JrdBatchIntegrationSupport {

    @Before
    public void init() {
        jdbcTemplate.execute(truncateAudit);
        SpringRestarter.getInstance().restart();
        camelContext.getGlobalOptions().put(ORCHESTRATED_ROUTE, JUDICIAL_REF_DATA_ORCHESTRATION);
        dataLoadUtil.setGlobalConstant(camelContext, JUDICIAL_REF_DATA_ORCHESTRATION);
        dataLoadUtil.setGlobalConstant(camelContext, LEAF_ROUTE);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/default-leaf-load.sql",
            "/testData/truncate-exception.sql"})
    public void testTaskletException() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithElinkIdMissing);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();
        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 0);
        validateExceptionDbRecordCount(jdbcTemplate, exceptionQuery, 1, false);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/default-leaf-load.sql",
            "/testData/truncate-exception.sql"})
    public void testAuthorisationElinksMissing() throws Exception {

        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithAuthElinkIdMissing);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();
        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 0);
        validateExceptionDbRecordCount(jdbcTemplate, exceptionQuery, 1, false);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/truncate-exception.sql",
            "/testData/default-leaf-load.sql"})
    public void testParentOrchestrationInvalidHeaderRollback() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithInvalidHeader);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();
        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 0);
        validateDbRecordCountFor(jdbcTemplate, sqlChild1, 0);
        validateExceptionDbRecordCount(jdbcTemplate, exceptionQuery, 1, false);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/truncate-exception.sql",
            "/testData/default-leaf-load.sql"})
    public void testAuthorisationInvalidHeaderRollback() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithAuthorisationInvalidHeader);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();
        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 0);
        validateDbRecordCountFor(jdbcTemplate, sqlChild1, 0);
        validateDbRecordCountFor(jdbcTemplate, sqlChild2, 0);
        validateExceptionDbRecordCount(jdbcTemplate, exceptionQuery, 1, false);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-leaf.sql"})
    public void testLeafFailuresRollbackAndKeepExistingState() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, file);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();

        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithAuthorisationInvalidHeader);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file_error);
        camelContext.getGlobalOptions().put(ORCHESTRATED_ROUTE, JUDICIAL_REF_DATA_ORCHESTRATION);
        jobLauncherTestUtils.launchJob();

        validateDbRecordCountFor(jdbcTemplate, roleSql, 5);
        validateDbRecordCountFor(jdbcTemplate, contractSql, 7);
        validateDbRecordCountFor(jdbcTemplate, baseLocationSql, 5);
        validateDbRecordCountFor(jdbcTemplate, regionSql, 5);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/truncate-exception.sql",
            "/testData/default-leaf-load.sql"})
    public void testParentOrchestrationJsrAuditTestAndPartialSuccess() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithInvalidJsr);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);


        jobLauncherTestUtils.launchJob();

        List<Map<String, Object>> judicialUserProfileList = jdbcTemplate.queryForList(userProfileSql);
        assertEquals(judicialUserProfileList.get(0).get("elinks_id"), "1");
        assertEquals(judicialUserProfileList.get(1).get("elinks_id"), "2");
        assertEquals(judicialUserProfileList.get(0).get("email_id"), "joe.bloggs@ejudiciary.net");
        assertEquals(judicialUserProfileList.get(1).get("email_id"), "jo1e.bloggs@ejudiciary.net");
        assertEquals(judicialUserProfileList.size(), 2);

        List<Map<String, Object>> judicialAppointmentList = jdbcTemplate.queryForList(sqlChild1);
        assertNotNull(judicialAppointmentList.get(0).get("judicial_office_appointment_id"));
        assertNotNull(judicialAppointmentList.get(1).get("judicial_office_appointment_id"));
        assertEquals(judicialAppointmentList.get(0).get("elinks_id"), "1");
        assertEquals(judicialAppointmentList.get(1).get("elinks_id"), "2");
        assertEquals(judicialAppointmentList.size(), 2);

        validateDbRecordCountFor(jdbcTemplate, sqlChild2, 2);
        validateExceptionDbRecordCount(jdbcTemplate, exceptionQuery, 6, true);

        List<Map<String, Object>> dataLoadSchedulerAudit = jdbcTemplate
                .queryForList(schedulerInsertJrdSqlPartialSuccess);
        assertEquals(dataLoadSchedulerAudit.get(0).get(DB_SCHEDULER_STATUS), PARTIAL_SUCCESS);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/truncate-exception.sql",
            "/testData/default-leaf-load.sql"})
    public void testParentOrchestrationJsrExceedsThresholdAuditTest() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithInvalidJsrExceedsThreshold);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);


        jobLauncherTestUtils.launchJob();

        List<Map<String, Object>> exceptionList = jdbcTemplate.queryForList(exceptionQuery);
        //Jsr exception exceeds threshold limit in

        assertThat(exceptionList.get(exceptionList.size() - 1).get("error_description").toString(),
                containsString("Jsr exception exceeds threshold limit"));
    }

    @Test
    @Sql(scripts = {"/testData/truncate-leaf.sql", "/testData/truncate-exception.sql",
            "/testData/default-leaf-load.sql"})
    public void testLeafFailuresInvalidHeader() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, file);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file_error);


        jobLauncherTestUtils.launchJob();

        List<Map<String, Object>> judicialUserRoleType = jdbcTemplate.queryForList(roleSql);
        assertEquals(judicialUserRoleType.size(), 1); //default leaf

        List<Map<String, Object>> judicialContractType = jdbcTemplate.queryForList(contractSql);
        assertEquals(judicialContractType.size(), 1);

        List<Map<String, Object>> judicialBaseLocationType = jdbcTemplate.queryForList(baseLocationSql);
        assertEquals(judicialBaseLocationType.size(), 1);

        List<Map<String, Object>> judicialRegionType = jdbcTemplate.queryForList(regionSql);
        assertEquals(judicialRegionType.size(), 1);

        List<Map<String, Object>> exceptionList = jdbcTemplate.queryForList(exceptionQuery);
        assertNotNull(exceptionList.get(0).get("file_name"));
        assertNotNull(exceptionList.get(0).get("scheduler_start_time"));
        assertNotNull(exceptionList.get(0).get("error_description"));
        assertNotNull(exceptionList.get(0).get("updated_timestamp"));
        assertEquals(1, exceptionList.size());
    }

    @Test
    @Sql(scripts = {"/testData/truncate-leaf.sql", "/testData/truncate-exception.sql"})
    public void testLeafFailuresInvalidJsr() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, file);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file_jsr_error);
        jobLauncherTestUtils.launchJob();
        List<Map<String, Object>> judicialUserRoleType = jdbcTemplate.queryForList(roleSql);
        assertEquals(judicialUserRoleType.size(), 3);
        assertEquals(judicialUserRoleType.get(0).get("role_id"), "1");
        assertEquals(judicialUserRoleType.get(1).get("role_id"), "3");
        assertEquals(judicialUserRoleType.get(2).get("role_id"), "7");

        assertEquals(judicialUserRoleType.get(0).get("role_desc_en"), "Magistrate");
        assertEquals(judicialUserRoleType.get(1).get("role_desc_en"),
                "Advisory Committee Member - Non Magistrate");
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
        assertEquals(9, exceptionList.size());
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/truncate-exception.sql",
            "/testData/default-leaf-load.sql"})
    public void testParentOrchestrationJsrSkipChildForeignKeyRecords() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithElinkIdInvalidInParent);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);
        jobLauncherTestUtils.launchJob();

        List<Map<String, Object>> judicialUserProfileList = jdbcTemplate.queryForList(userProfileSql);
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
    }
}

