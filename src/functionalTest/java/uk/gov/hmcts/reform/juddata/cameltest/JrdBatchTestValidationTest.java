
package uk.gov.hmcts.reform.juddata.cameltest;

import org.apache.camel.CamelContext;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.CamelTestContextBootstrapper;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.gov.hmcts.reform.data.ingestion.DataIngestionLibraryRunner;
import uk.gov.hmcts.reform.data.ingestion.camel.service.EmailServiceImpl;
import uk.gov.hmcts.reform.data.ingestion.camel.service.dto.Email;
import uk.gov.hmcts.reform.data.ingestion.configuration.AzureBlobConfig;
import uk.gov.hmcts.reform.data.ingestion.configuration.BlobStorageCredentials;
import uk.gov.hmcts.reform.idam.client.IdamApi;
import uk.gov.hmcts.reform.juddata.camel.util.JrdExecutor;
import uk.gov.hmcts.reform.juddata.camel.util.JrdUserProfileUtil;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.JrdBatchIntegrationSupport;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.LeafIntegrationTestSupport;
import uk.gov.hmcts.reform.juddata.client.IdamClient;
import uk.gov.hmcts.reform.juddata.config.LeafCamelConfig;
import uk.gov.hmcts.reform.juddata.config.ParentCamelConfig;
import uk.gov.hmcts.reform.juddata.configuration.BatchConfig;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.PARTIAL_SUCCESS;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.START_ROUTE;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.DIRECT_JRD;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.INVALID_JSR_PARENT_ROW;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.MISSING_BASE_LOCATION;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.MISSING_PER_ID;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.MISSING_LOCATION;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.JUDICIAL_REF_DATA_ORCHESTRATION;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.LEAF_ROUTE;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.ORCHESTRATED_ROUTE;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.file;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithAuthPerIdMissing;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithAuthorisationInvalidHeader;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithEmptyPerIdInAuth;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithInvalidPerCodeObjectIds;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithPerIdInvalidInParent;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithPerIdMissing;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithForeignKeyViolations;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithInvalidHeader;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithInvalidJsr;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithInvalidJsrExceedsThreshold;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithInvalidAppointments;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.missingUserProfile;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.uploadBlobs;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateDbRecordCountFor;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateExceptionDbRecordCount;

@TestPropertySource(properties = {"spring.config.location=classpath:application-integration.yml,"
    + "classpath:application-leaf-integration.yml"})
@MockEndpoints("log:*")
@ContextConfiguration(classes = {ParentCamelConfig.class, LeafCamelConfig.class, CamelTestContextBootstrapper.class,
    JobLauncherTestUtils.class, BatchConfig.class, AzureBlobConfig.class, BlobStorageCredentials.class},
    initializers = ConfigDataApplicationContextInitializer.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = JpaRepositoriesAutoConfiguration.class)
@EnableTransactionManagement
@SqlConfig(dataSource = "dataSource", transactionManager = "txManager",
    transactionMode = SqlConfig.TransactionMode.ISOLATED)
@CamelSpringBootTest
@EnableFeignClients(clients = {IdamClient.class, IdamApi.class})
class JrdBatchTestValidationTest extends JrdBatchIntegrationSupport {

    @Autowired
    JrdExecutor jrdExecutor;

    @Autowired
    JrdUserProfileUtil jrdUserProfileUtil;

    @Autowired
    DataIngestionLibraryRunner dataIngestionLibraryRunner;

    @Autowired
    CamelContext camelContext;

    @Mock
    EmailServiceImpl emailService;

    @Test
    void testTaskletException() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithPerIdMissing);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();
        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 2);
        validateExceptionDbRecordCount(jdbcTemplate, exceptionQuery, 2, false);
    }

    @Test
    void testAuthorisationPerMissing() throws Exception {

        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithAuthPerIdMissing);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();
        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 2);
        validateExceptionDbRecordCount(jdbcTemplate, exceptionQuery, 2, false);
    }

    @Test
    void testParentOrchestrationInvalidHeaderAppointmentsRollbackAppointments() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithInvalidHeader);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();
        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 2);
        validateDbRecordCountFor(jdbcTemplate, appointmentSql, 0);
        validateExceptionDbRecordCount(jdbcTemplate, exceptionQuery, 2, false);
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
        validateExceptionDbRecordCount(jdbcTemplate, exceptionQuery, 2, false);
    }

    @Test
    void testLeafFailuresRollbackAndKeepExistingState() throws Exception {
        setField(jrdExecutor, "emailService", emailService);
        Mockito.when(emailService.sendEmail(ArgumentMatchers.any(Email.class))).thenReturn(200);
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, file);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();

        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithAuthorisationInvalidHeader);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file_error);

        camelContext.getGlobalOptions().put(ORCHESTRATED_ROUTE, JUDICIAL_REF_DATA_ORCHESTRATION);

        jrdExecutor.execute(camelContext, LEAF_ROUTE, startLeafRoute);
        jrdExecutor.execute(camelContext, JUDICIAL_REF_DATA_ORCHESTRATION, startRoute);
        //jobLauncherTestUtils.launchJob();

        validateDbRecordCountFor(jdbcTemplate, baseLocationSql, 8);
        validateDbRecordCountFor(jdbcTemplate, regionSql, 6);
        validateDbRecordCountFor(jdbcTemplate, roleSql, 10);
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
        assertEquals(3, judicialUserProfileList.size());

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

        String errorDescription = exceptionList.get(exceptionList.size() - 3).get("error_description").toString();

        assertTrue(errorDescription.contains("Jsr exception exceeds threshold limit"));
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
        assertEquals(6, judicialBaseLocationType.size());

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
        assertEquals(5, exceptionList.size());

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
        assertEquals(3, judicialUserProfileList.size());

        List<Map<String, Object>> judicialAppointmentList = jdbcTemplate.queryForList(appointmentSql);
        assertNotNull(judicialAppointmentList.get(0).get("judicial_office_appointment_id"));
        assertNotNull(judicialAppointmentList.get(0).get("judicial_office_appointment_id"));
        assertEquals("1", judicialAppointmentList.get(0).get("per_id"));
        assertEquals("2", judicialAppointmentList.get(1).get("per_id"));
        assertEquals(3, judicialAppointmentList.size());

        List<Map<String, Object>> exceptionList = jdbcTemplate.queryForList(exceptionQuery);
        assertEquals(4, exceptionList.size());
        assertEquals("judicial-office-appointment", exceptionList.get(1).get("table_name"));
        assertEquals("judicial-office-appointment", exceptionList.get(2).get("table_name"));
        assertEquals(INVALID_JSR_PARENT_ROW, exceptionList.get(1).get("error_description"));
        assertEquals(INVALID_JSR_PARENT_ROW, exceptionList.get(2).get("error_description"));

        assertEquals(5L, exceptionList.get(1).get("row_id"));
        assertEquals(6L, exceptionList.get(2).get("row_id"));
    }

    @Test
    void testParentOrchestrationJsrSkipChildRecordsForeignKeyViolations() throws Exception {

        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithForeignKeyViolations);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        JobParameters params = new JobParametersBuilder()
            .addString(jobLauncherTestUtils.getJob().getName(), String.valueOf(System.currentTimeMillis()))
            .addString(START_ROUTE, DIRECT_JRD)
            .toJobParameters();
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        List<Map<String, Object>> judicialUserProfileList = jdbcTemplate.queryForList(userProfileSql);
        assertEquals(7, judicialUserProfileList.size());

        List<Map<String, Object>> judicialAppointmentList = jdbcTemplate.queryForList(appointmentSql);
        assertEquals(5, judicialAppointmentList.size());

        List<Map<String, Object>> exceptionList = jdbcTemplate.queryForList(exceptionQuery);
        assertEquals(4, exceptionList.size());
        assertEquals(MISSING_PER_ID, exceptionList.get(0).get("error_description"));
        assertEquals("judicial-office-appointment", exceptionList.get(0).get("table_name"));
        assertEquals(MISSING_LOCATION, exceptionList.get(1).get("error_description"));
        assertEquals(MISSING_BASE_LOCATION, exceptionList.get(2).get("error_description"));
        assertEquals(MISSING_PER_ID, exceptionList.get(3).get("error_description"));
        assertEquals("judicial_office_authorisation", exceptionList.get(3).get("table_name"));
    }

    @Test
    void testUserProfileWithInvalidAppointmentValue() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithInvalidAppointments);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();
        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 6);
        validateExceptionDbRecordCount(jdbcTemplate, exceptionQuery, 0, false);
    }

    @Test
    void testChildSkipsOnParentFailure() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, missingUserProfile);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        JobParameters params = new JobParametersBuilder()
            .addString(jobLauncherTestUtils.getJob().getName(), String.valueOf(System.currentTimeMillis()))
            .addString(START_ROUTE, DIRECT_JRD)
            .toJobParameters();
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        List<Map<String, Object>> auditList = jdbcTemplate.queryForList(selectDataLoadSchedulerAudit);
        assertEquals(3, auditList.size()); //Personal, Locations, base-locations only
    }

    @Test
    void testRowIdInExceptionTable() throws Exception {

        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithEmptyPerIdInAuth);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();
        validateExceptionDbRecordCount(jdbcTemplate, exceptionQuery, 3, false);
        List<Map<String, Object>> exceptionList = jdbcTemplate.queryForList(exceptionQuery);

        List<Long> rowId = exceptionList.stream()
                .map(i -> (Long) i.get("row_id"))
                .collect(Collectors.toList());

        assertThat(rowId)
                .hasSize(3)
                .hasSameElementsAs(List.of(3L, 5L, 7L));

    }

    private void validateLeafRoleJsr(List<Map<String, Object>> judicialUserRoleType) {
        assertEquals(5, judicialUserRoleType.size());
        assertEquals("1", judicialUserRoleType.get(1).get("role_id"));
        assertEquals("3", judicialUserRoleType.get(2).get("role_id"));
        assertEquals("7", judicialUserRoleType.get(3).get("role_id"));

        assertEquals("Magistrate", judicialUserRoleType.get(1).get("role_desc_en"));
        assertEquals("Advisory Committee Member - Non Magistrate",
                judicialUserRoleType.get(2).get("role_desc_en"));
        assertEquals("MAGS - AC Admin User", judicialUserRoleType.get(3).get("role_desc_en"));
    }


    @Test
    void testUserProfileWithInvalidPersonalCodeObjectId() throws Exception {
        setField(jrdUserProfileUtil, "emailService", emailService);
        Mockito.when(emailService.sendEmail(ArgumentMatchers.any(Email.class))).thenReturn(200);

        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithInvalidPerCodeObjectIds);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();
        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 4);
    }

}

