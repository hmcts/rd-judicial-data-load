
package uk.gov.hmcts.reform.juddata.cameltest;

import org.apache.camel.CamelContext;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.CamelTestContextBootstrapper;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.gov.hmcts.reform.data.ingestion.DataIngestionLibraryRunner;
import uk.gov.hmcts.reform.data.ingestion.configuration.AzureBlobConfig;
import uk.gov.hmcts.reform.data.ingestion.configuration.BlobStorageCredentials;
import uk.gov.hmcts.reform.juddata.camel.util.JrdExecutor;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.JrdBatchIntegrationSupport;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.LeafIntegrationTestSupport;
import uk.gov.hmcts.reform.juddata.config.LeafCamelConfig;
import uk.gov.hmcts.reform.juddata.config.ParentCamelConfig;
import uk.gov.hmcts.reform.juddata.configuration.BatchConfig;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.PARTIAL_SUCCESS;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.MISSING_BASE_LOCATION;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.MISSING_PER;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.MISSING_LOCATION;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.JUDICIAL_REF_DATA_ORCHESTRATION;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.LEAF_ROUTE;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.ORCHESTRATED_ROUTE;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.file;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithAuthPerIdMissing;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithAuthorisationInvalidHeader;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithPerIdInvalidInParent;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithPerIdMissing;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithForeignKeyViolations;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithInvalidHeader;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithInvalidJsr;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithInvalidJsrExceedsThreshold;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.uploadBlobs;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateDbRecordCountFor;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateExceptionDbRecordCount;

@TestPropertySource(properties = {"spring.config.location=classpath:application-integration.yml,"
    + "classpath:application-leaf-integration.yml"})
@MockEndpoints("log:*")
@ContextConfiguration(classes = {ParentCamelConfig.class, LeafCamelConfig.class, CamelTestContextBootstrapper.class,
    JobLauncherTestUtils.class, BatchConfig.class, AzureBlobConfig.class, BlobStorageCredentials.class},
    initializers = ConfigFileApplicationContextInitializer.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = JpaRepositoriesAutoConfiguration.class)
@EnableTransactionManagement
@SqlConfig(dataSource = "dataSource", transactionManager = "txManager",
    transactionMode = SqlConfig.TransactionMode.ISOLATED)
@CamelSpringBootTest
class JrdBatchTestValidationTest extends JrdBatchIntegrationSupport {

    @Autowired
    JrdExecutor jrdExecutor;

    @Autowired
    DataIngestionLibraryRunner dataIngestionLibraryRunner;

    @Autowired
    CamelContext camelContext;

    @Test
    void testTaskletException() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithPerIdMissing);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();
        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 2);
        validateExceptionDbRecordCount(jdbcTemplate, exceptionQuery, 1, false);
    }

    @Test
    void testAuthorisationPerMissing() throws Exception {


        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithAuthPerIdMissing);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();
        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 2);
        validateExceptionDbRecordCount(jdbcTemplate, exceptionQuery, 1, false);
    }

    @Test
    void testParentOrchestrationInvalidHeaderAppointmentsRollbackAppointments() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithInvalidHeader);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();
        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 2);
        validateDbRecordCountFor(jdbcTemplate, appointmentSql, 0);
        validateExceptionDbRecordCount(jdbcTemplate, exceptionQuery, 1, false);
    }

    @Test
    void testAuthorisationInvalidHeaderAuthorizationRollback() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithAuthorisationInvalidHeader);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();
        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 2);
        //testAuthorisationInvalidHeaderRollback only Authorization
        validateDbRecordCountFor(jdbcTemplate, appointmentSql, 2);
        validateDbRecordCountFor(jdbcTemplate, authorizationSql, 0);
        validateExceptionDbRecordCount(jdbcTemplate, exceptionQuery, 1, false);
    }

    @Test
    void testLeafFailuresRollbackAndKeepExistingState() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, file);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();

        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithAuthorisationInvalidHeader);
        camelContext.getGlobalOptions().put(ORCHESTRATED_ROUTE, JUDICIAL_REF_DATA_ORCHESTRATION);

        jrdExecutor.execute(camelContext, LEAF_ROUTE, startLeafRoute);
        jrdExecutor.execute(camelContext, JUDICIAL_REF_DATA_ORCHESTRATION, startRoute);
        //jobLauncherTestUtils.launchJob();

        validateDbRecordCountFor(jdbcTemplate, baseLocationSql, 6);
        validateDbRecordCountFor(jdbcTemplate, regionSql, 6);
    }

    @Test
    void testParentOrchestrationJsrAuditTestAndPartialSuccess() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithInvalidJsr);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);
        jobLauncherTestUtils.launchJob();

        List<Map<String, Object>> judicialUserProfileList = jdbcTemplate.queryForList(userProfileSql);
        assertEquals("1", judicialUserProfileList.get(0).get("per_id"));
        assertEquals("2", judicialUserProfileList.get(1).get("per_id"));
        assertEquals("joe.bloggs@ejudiciary.net", judicialUserProfileList.get(0).get("ejudiciary_email"));
        assertEquals("jo1e.bloggs@ejudiciary.net", judicialUserProfileList.get(1).get("ejudiciary_email"));
        assertEquals(2, judicialUserProfileList.size());

        List<Map<String, Object>> judicialAppointmentList = jdbcTemplate.queryForList(appointmentSql);
        assertNotNull(judicialAppointmentList.get(0).get("judicial_office_appointment_id"));
        assertNotNull(judicialAppointmentList.get(1).get("judicial_office_appointment_id"));
        assertEquals("1", judicialAppointmentList.get(0).get("per_id"));
        assertEquals("2", judicialAppointmentList.get(1).get("per_id"));
        assertEquals(2, judicialAppointmentList.size());

        validateDbRecordCountFor(jdbcTemplate, authorizationSql, 2);
        validateExceptionDbRecordCount(jdbcTemplate, exceptionQuery, 5, true);

        List<Map<String, Object>> dataLoadSchedulerAudit = jdbcTemplate
            .queryForList(schedulerInsertJrdSqlPartialSuccess);
        assertEquals(PARTIAL_SUCCESS, dataLoadSchedulerAudit.get(0).get(FILE_STATUS));
    }

    @Test
    void testParentOrchestrationJsrExceedsThresholdAuditTest() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithInvalidJsrExceedsThreshold);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);


        jobLauncherTestUtils.launchJob();

        List<Map<String, Object>> exceptionList = jdbcTemplate.queryForList(exceptionQuery);
        //Jsr exception exceeds threshold limit in

        assertThat(exceptionList.get(exceptionList.size() - 2).get("error_description").toString(),
            containsString("Jsr exception exceeds threshold limit"));
    }

    @Test
    void testLeafFailuresInvalidJsr() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, file);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file_jsr_error);
        jobLauncherTestUtils.launchJob();

        List<Map<String, Object>> judicialBaseLocationType = jdbcTemplate.queryForList(baseLocationSql);
        assertEquals("1", judicialBaseLocationType.get(1).get("base_location_id"));
        assertEquals("2", judicialBaseLocationType.get(2).get("base_location_id"));
        assertEquals("5", judicialBaseLocationType.get(3).get("base_location_id"));
        assertEquals(4, judicialBaseLocationType.size());

        List<Map<String, Object>> judicialRegionType = jdbcTemplate.queryForList(regionSql);
        assertEquals("1", judicialRegionType.get(1).get("region_id"));
        assertEquals("4", judicialRegionType.get(2).get("region_id"));
        assertEquals("5", judicialRegionType.get(3).get("region_id"));
        assertEquals(4, judicialRegionType.size());

        List<Map<String, Object>> exceptionList = jdbcTemplate.queryForList(exceptionQuery);
        for (int count = 0; count < 4; count++) {
            assertNotNull(exceptionList.get(count).get("table_name"));
            assertNotNull(exceptionList.get(count).get("scheduler_start_time"));
            assertNotNull(exceptionList.get(count).get("key"));
            assertNotNull(exceptionList.get(count).get("field_in_error"));
            assertNotNull(exceptionList.get(count).get("error_description"));
            assertNotNull(exceptionList.get(count).get("updated_timestamp"));
        }
        assertEquals(4, exceptionList.size());
    }

    @Test
    void testParentOrchestrationJsrSkipChildAppointmentRecordsForInvalidUserProfile() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithPerIdInvalidInParent);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);
        jobLauncherTestUtils.launchJob();

        List<Map<String, Object>> judicialUserProfileList = jdbcTemplate.queryForList(userProfileSql);
        assertEquals("1", judicialUserProfileList.get(0).get("per_id"));
        assertEquals("2", judicialUserProfileList.get(1).get("per_id"));
        assertEquals("joe.bloggs@ejudiciary.net", judicialUserProfileList.get(0).get("ejudiciary_email"));
        assertEquals("jo1e.bloggs@ejudiciary.net", judicialUserProfileList.get(1).get("ejudiciary_email"));
        assertEquals(2, judicialUserProfileList.size());

        List<Map<String, Object>> judicialAppointmentList = jdbcTemplate.queryForList(appointmentSql);
        assertNotNull(judicialAppointmentList.get(0).get("judicial_office_appointment_id"));
        assertNotNull(judicialAppointmentList.get(0).get("judicial_office_appointment_id"));
        assertEquals("1", judicialAppointmentList.get(0).get("per_id"));
        assertEquals("2", judicialAppointmentList.get(1).get("per_id"));
        assertEquals(2, judicialAppointmentList.size());
    }

    @Test
    void testParentOrchestrationJsrSkipChildRecordsForeignKeyViolations() throws Exception {

        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithForeignKeyViolations);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        JobParameters params = new JobParametersBuilder()
            .addString(jobLauncherTestUtils.getJob().getName(), String.valueOf(System.currentTimeMillis()))
            .toJobParameters();
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        List<Map<String, Object>> judicialUserProfileList = jdbcTemplate.queryForList(userProfileSql);
        assertEquals(7, judicialUserProfileList.size());

        List<Map<String, Object>> judicialAppointmentList = jdbcTemplate.queryForList(appointmentSql);
        assertEquals(5, judicialAppointmentList.size());

        List<Map<String, Object>> exceptionList = jdbcTemplate.queryForList(exceptionQuery);
        assertEquals(4, exceptionList.size());
        assertEquals(MISSING_PER, exceptionList.get(0).get("error_description"));
        assertEquals("judicial-office-appointment", exceptionList.get(0).get("table_name"));
        assertEquals(MISSING_LOCATION, exceptionList.get(1).get("error_description"));
        assertEquals(MISSING_BASE_LOCATION, exceptionList.get(2).get("error_description"));
        assertEquals(MISSING_PER, exceptionList.get(3).get("error_description"));
        assertEquals("judicial_office_authorisation", exceptionList.get(3).get("table_name"));
    }
}

