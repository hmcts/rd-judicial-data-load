package uk.gov.hmcts.reform.juddata.cameltest;

import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.gov.hmcts.reform.data.ingestion.DataIngestionLibraryRunner;
import uk.gov.hmcts.reform.data.ingestion.configuration.AzureBlobConfig;
import uk.gov.hmcts.reform.data.ingestion.configuration.BlobStorageCredentials;
import uk.gov.hmcts.reform.idam.client.IdamApi;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialRegionType;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.JrdBatchIntegrationSupport;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.LeafIntegrationTestSupport;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.SpringStarter;
import uk.gov.hmcts.reform.juddata.client.IdamClient;
import uk.gov.hmcts.reform.juddata.config.LeafCamelConfig;
import uk.gov.hmcts.reform.juddata.config.ParentCamelConfig;
import uk.gov.hmcts.reform.juddata.configuration.BatchConfig;
import uk.gov.hmcts.reform.juddata.configuration.FeignConfiguration;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.DIRECT_JRD;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.START_ROUTE;
import static uk.gov.hmcts.reform.juddata.camel.util.CommonUtils.getDateTimeStamp;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.file;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithError;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithInvalidAppointmentsEntry;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithSingleRecord;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.handleNull;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.uploadBlobs;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateAdditionalInfoRolesFile;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateAppointmentFile;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateAuthorisationFile;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateDbRecordCountFor;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateDbRecordValuesFor;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateUserProfileFile;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.retrieveColumnValues;

@TestPropertySource(properties = {"spring.config.location=classpath:application-integration.yml,"
    + "classpath:application-leaf-integration.yml"})
@MockEndpoints("log:*")
@ContextConfiguration(classes = {ParentCamelConfig.class, LeafCamelConfig.class,
    JobLauncherTestUtils.class, BatchConfig.class, AzureBlobConfig.class, BlobStorageCredentials.class,
    FeignConfiguration.class},
    initializers = ConfigDataApplicationContextInitializer.class)
@CamelSpringBootTest
@EnableAutoConfiguration(exclude = JpaRepositoriesAutoConfiguration.class)
@EnableTransactionManagement
@SqlConfig(dataSource = "dataSource", transactionManager = "txManager",
    transactionMode = SqlConfig.TransactionMode.ISOLATED)
@SpringBootTest
@EnableFeignClients(clients = {IdamClient.class, IdamApi.class})
class JrdBatchApplicationTest extends JrdBatchIntegrationSupport {

    @Autowired
    DataIngestionLibraryRunner dataIngestionLibraryRunner;

    @Test
    void testTasklet() throws Exception {
        uploadBlobs(jrdBlobSupport, parentFiles, file);
        uploadBlobs(jrdBlobSupport, leafFiles, LeafIntegrationTestSupport.file);
        JobParameters params = new JobParametersBuilder()
            .addString(jobLauncherTestUtils.getJob().getName(), String.valueOf(System.currentTimeMillis()))
            .addString(START_ROUTE, DIRECT_JRD)
            .toJobParameters();
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 2);
        validateDbRecordCountFor(jdbcTemplate, roleSql, 5);
        validateAdditionalInfoRolesFile(jdbcTemplate, roleSql);
    }

    @Test
    void testTaskletIdempotent() throws Exception {
        //clean context
        SpringStarter.getInstance().restart();
        uploadBlobs(jrdBlobSupport, parentFiles, file);
        uploadBlobs(jrdBlobSupport, leafFiles, LeafIntegrationTestSupport.file);
        JobParameters params = new JobParametersBuilder()
            .addString(jobLauncherTestUtils.getJob().getName(), String.valueOf(System.currentTimeMillis()))
            .addString(START_ROUTE, DIRECT_JRD)
             .toJobParameters();
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        List<Map<String, Object>> auditDetails = jdbcTemplate.queryForList(selectDataLoadSchedulerAudit);
        final Timestamp timestamp = (Timestamp) auditDetails.get(0).get("scheduler_end_time");
        uploadBlobs(jrdBlobSupport, parentFiles, file);
        uploadBlobs(jrdBlobSupport, leafFiles, LeafIntegrationTestSupport.file);
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 2);
        validateDbRecordCountFor(jdbcTemplate, roleSql, 5);
        validateDbRecordCountFor(jdbcTemplate, selectDataLoadSchedulerAudit, 6);
        List<Map<String, Object>> auditDetailsNextRun = jdbcTemplate.queryForList(selectDataLoadSchedulerAudit);
        final Timestamp timestampNextRun = (Timestamp) auditDetailsNextRun.get(0).get("scheduler_end_time");
        assertEquals(timestamp, timestampNextRun);
    }

    @Test
    void testParentOrchestration() throws Exception {

        uploadBlobs(jrdBlobSupport, parentFiles, file);
        uploadBlobs(jrdBlobSupport, leafFiles, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();

        validateUserProfileFile(jdbcTemplate, userProfileSql);
        validateAppointmentFile(jdbcTemplate, appointmentSql);
        validateAuthorisationFile(jdbcTemplate, authorizationSql);
    }

    @Test
    void testAppointmentFailureRollbacksAppointment() throws Exception {

        uploadBlobs(jrdBlobSupport, parentFiles, fileWithError);
        uploadBlobs(jrdBlobSupport, leafFiles, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();

        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 2);
        validateDbRecordCountFor(jdbcTemplate, appointmentSql, 0);
        validateDbRecordCountFor(jdbcTemplate, authorizationSql, 8);
    }

    @Test
    void testParentOrchestrationSingleRecord() throws Exception {

        uploadBlobs(jrdBlobSupport, parentFiles, fileWithSingleRecord);
        uploadBlobs(jrdBlobSupport, leafFiles, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();
        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 1);
        validateDbRecordCountFor(jdbcTemplate, appointmentSql, 1);
        validateDbRecordCountFor(jdbcTemplate, authorizationSql, 1);
    }

    @Test
    void testAllLeafs() throws Exception {

        uploadBlobs(jrdBlobSupport, parentFiles, file);
        uploadBlobs(jrdBlobSupport, leafFiles, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();

        validateDbRecordCountFor(jdbcTemplate, baseLocationSql, 8);
        validateDbRecordCountFor(jdbcTemplate, regionSql, 6);
        validateDbRecordCountFor(jdbcTemplate, roleSql, 5);
        validateLocationLeafFile(jdbcTemplate, regionSql);

        validateDbRecordValuesFor(jdbcTemplate, baseLocationSql,"mrd_created_time");
        validateDbRecordValuesFor(jdbcTemplate, baseLocationSql,"mrd_updated_time");
        validateDbRecordValuesFor(jdbcTemplate, baseLocationSql,"mrd_deleted_time");
    }

    @Test
    void testTicketCodeMappingInJudicialOfficeAuthorisationTable() throws Exception {
        uploadBlobs(jrdBlobSupport, parentFiles, file);
        uploadBlobs(jrdBlobSupport, leafFiles, LeafIntegrationTestSupport.file);
        final JobParameters params = new JobParametersBuilder()
                .addString(jobLauncherTestUtils.getJob().getName(), String.valueOf(System.currentTimeMillis()))
                .addString(START_ROUTE, DIRECT_JRD)
                .toJobParameters();
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        validateDbRecordCountFor(jdbcTemplate, ticketCodeSql, 7);

        var ticketCodes = retrieveColumnValues(jdbcTemplate, ticketCodeSql, "ticket_code");
        assertTrue(ticketCodes.containsAll(List.of("366","373","289")));
        assertFalse(ticketCodes.containsAll(List.of("363","376")));
    }

    @Test
    void testLocationsMappingInJudicialOfficeAppointmentTable() throws Exception {
        uploadBlobs(jrdBlobSupport, parentFiles, file);
        uploadBlobs(jrdBlobSupport, leafFiles, LeafIntegrationTestSupport.file);
        final JobParameters params = new JobParametersBuilder()
                .addString(jobLauncherTestUtils.getJob().getName(), String.valueOf(System.currentTimeMillis()))
                .addString(START_ROUTE, DIRECT_JRD)
                .toJobParameters();
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        validateDbRecordCountFor(jdbcTemplate, epimmsIdSql, 2);

        final List<Object> epimmsId = retrieveColumnValues(jdbcTemplate, epimmsIdSql, "epimms_id");
        assertTrue(epimmsId.contains("20262"));
    }

    @Test
    void testServiceCodeMappingInJudicialOfficeAppointmentTable() throws Exception {
        uploadBlobs(jrdBlobSupport, parentFiles, file);
        uploadBlobs(jrdBlobSupport, leafFiles, LeafIntegrationTestSupport.file);
        final JobParameters params = new JobParametersBuilder()
                .addString(jobLauncherTestUtils.getJob().getName(), String.valueOf(System.currentTimeMillis()))
                .addString(START_ROUTE, DIRECT_JRD)
                .toJobParameters();
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        validateDbRecordCountFor(jdbcTemplate, judicialOfficeAppointmentServiceCodeSql, 1);

        final List<Object> serviceCodes = retrieveColumnValues(jdbcTemplate, judicialOfficeAppointmentServiceCodeSql,
                "service_code");
        assertTrue(serviceCodes.contains("BFA1"));
    }

    @Test
    void testObjectIdMappingInJudicialOfficeAuthorisationTable() throws Exception {
        uploadBlobs(jrdBlobSupport, parentFiles, file);
        uploadBlobs(jrdBlobSupport, leafFiles, LeafIntegrationTestSupport.file);
        final JobParameters params = new JobParametersBuilder()
                .addString(jobLauncherTestUtils.getJob().getName(), String.valueOf(System.currentTimeMillis()))
                .addString(START_ROUTE, DIRECT_JRD)
                .toJobParameters();
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        validateDbRecordCountFor(jdbcTemplate, authorizationSql, 8);

        final List<Object> objectIds = retrieveColumnValues(jdbcTemplate, authorizationSql, "object_id");
        assertTrue(objectIds.contains("578256875287452"));
    }

    @Test
    void testMappingInJudicialOfficeAppointmentTable() throws Exception {
        uploadBlobs(jrdBlobSupport, parentFiles, file);
        uploadBlobs(jrdBlobSupport, leafFiles, LeafIntegrationTestSupport.file);
        final JobParameters params = new JobParametersBuilder()
                .addString(jobLauncherTestUtils.getJob().getName(), String.valueOf(System.currentTimeMillis()))
                .addString(START_ROUTE, DIRECT_JRD)
                .toJobParameters();
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        validateDbRecordCountFor(jdbcTemplate, appointmentSql, 2);

        final List<Object> objectIds = retrieveColumnValues(jdbcTemplate, appointmentSql, "object_id");
        assertTrue(objectIds.contains("578256875287452"));

        final List<Object> appointments = retrieveColumnValues(jdbcTemplate, appointmentSql, "appointment");
        assertTrue(appointments.contains("Magistrate"));

        final List<Object> appointmentTypes = retrieveColumnValues(jdbcTemplate, appointmentSql, "appointment_type");
        assertTrue(appointmentTypes.contains("1"));

    }

    @Test
    void testMappingInJudicialOfficeAppointmentWithErrors() throws Exception {
        uploadBlobs(jrdBlobSupport, parentFiles, fileWithInvalidAppointmentsEntry);
        uploadBlobs(jrdBlobSupport, leafFiles, LeafIntegrationTestSupport.file);
        final JobParameters params = new JobParametersBuilder()
                .addString(jobLauncherTestUtils.getJob().getName(), String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        validateDbRecordCountFor(jdbcTemplate, appointmentSql, 2);

        final List<Object> appointments = retrieveColumnValues(jdbcTemplate, appointmentSql, "appointment");
        assertFalse(appointments.contains("Initial Automated Record"));

        final List<Object> appointmentsError = retrieveColumnValues(jdbcTemplate, exceptionQuery, "field_in_error");
        assertTrue(appointmentsError.contains("appointment"));

    }

    @Test
    void testMappingInJudicialUserRoleTypesWithErrors() throws Exception {
        uploadBlobs(jrdBlobSupport, parentFiles, file);
        uploadBlobs(jrdBlobSupport, leafFiles, LeafIntegrationTestSupport.file);
        final JobParameters params = new JobParametersBuilder()
                .addString(jobLauncherTestUtils.getJob().getName(), String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        validateDbRecordCountFor(jdbcTemplate, roleSql, 5);

        final List<Object> perIds = retrieveColumnValues(jdbcTemplate, roleSql, "per_id");
        assertFalse(perIds.contains("5"));

        final List<Object> titles = retrieveColumnValues(jdbcTemplate, roleSql, "title");
        assertTrue(titles.contains("Family Course Tutor (JC)"));

        final List<Object> locations = retrieveColumnValues(jdbcTemplate, roleSql, "location");
        assertTrue(locations.contains("Nationwide"));

        final List<Object> RolesError = retrieveColumnValues(jdbcTemplate, exceptionQuery, "field_in_error");
        assertTrue(RolesError.contains("per_id"));

    }

    private void validateLocationLeafFile(JdbcTemplate jdbcTemplate, String regionSql) {
        final List<Map<String, Object>> locations = jdbcTemplate.queryForList(regionSql);
        var judicialRegionTypes = locations.stream()
                .map(l -> {
                    JudicialRegionType judicialRegionType = new JudicialRegionType();
                    judicialRegionType.setRegionId((String) l.get("region_id"));
                    judicialRegionType.setRegionDescCy((String) l.get("region_desc_cy"));
                    judicialRegionType.setRegionDescEn((String) l.get("region_desc_en"));
                    judicialRegionType
                            .setMrdCreatedTime(handleNull((Timestamp) l.get("mrd_created_time")));
                    judicialRegionType
                            .setMrdUpdatedTime(handleNull((Timestamp) l.get("mrd_updated_time")));
                    judicialRegionType
                            .setMrdDeletedTime(handleNull((Timestamp) l.get("mrd_deleted_time")));
                    return judicialRegionType;
                })
                .collect(Collectors.toList());
        assertTrue(judicialRegionTypes.get(2).getMrdCreatedTime().contains("2022-04-28"));
        assertTrue(judicialRegionTypes.get(2).getMrdUpdatedTime().contains("2022-04-28"));
        assertTrue(judicialRegionTypes.get(2).getMrdDeletedTime().contains("2022-04-28"));
    }

    @Test
    void testIfTheUserIsJudgeOrPanelMemberOrMagistrateAndMRDTime() throws Exception {
        uploadBlobs(jrdBlobSupport, parentFiles, file);
        uploadBlobs(jrdBlobSupport, leafFiles, LeafIntegrationTestSupport.file);
        final JobParameters params = new JobParametersBuilder()
                .addString(jobLauncherTestUtils.getJob().getName(), String.valueOf(System.currentTimeMillis()))
                .addString(START_ROUTE, DIRECT_JRD)
                .toJobParameters();
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);

        List<Map<String, Object>> userProfiles = jdbcTemplate.queryForList(userProfileSql);

        var userProfile1 = userProfiles.get(0);
        assertTrue((Boolean)userProfile1.get("is_judge"));
        assertTrue((Boolean)userProfile1.get("is_panel_member"));
        assertFalse((Boolean)userProfile1.get("is_magistrate"));
        assertEquals(getDateTimeStamp("28-04-2022 00:00:00"), userProfile1.get("mrd_created_time"));
        assertEquals(getDateTimeStamp("28-04-2022 00:00:00"), userProfile1.get("mrd_updated_time"));
        assertEquals(getDateTimeStamp("28-04-2022 00:00:00"), userProfile1.get("mrd_deleted_time"));

        var userProfile2 = userProfiles.get(1);
        assertTrue((Boolean)userProfile2.get("is_judge"));
        assertTrue((Boolean)userProfile2.get("is_panel_member"));
        assertFalse((Boolean)userProfile2.get("is_magistrate"));
        assertNull(userProfile2.get("mrd_created_time"));
        assertNull(userProfile2.get("mrd_updated_time"));
        assertNull(userProfile2.get("mrd_deleted_time"));
    }

    @Test
    void testMappingInJudicialOfficeAppointmentWithLocationAndMRDTime() throws Exception {
        uploadBlobs(jrdBlobSupport, parentFiles, file);
        uploadBlobs(jrdBlobSupport, leafFiles, LeafIntegrationTestSupport.file);
        final JobParameters params = new JobParametersBuilder()
                .addString(jobLauncherTestUtils.getJob().getName(), String.valueOf(System.currentTimeMillis()))
                .addString(START_ROUTE, DIRECT_JRD)
                .toJobParameters();
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        validateDbRecordCountFor(jdbcTemplate, appointmentSql, 2);

        final List<Object> primaryLocation = retrieveColumnValues(jdbcTemplate, appointmentSql, "primary_location");
        assertEquals("primary_01", primaryLocation.get(0));

        final List<Object> secondaryLocation = retrieveColumnValues(jdbcTemplate, appointmentSql, "secondary_location");
        assertEquals("secondary_01", secondaryLocation.get(0));

        final List<Object> tertiaryLocation = retrieveColumnValues(jdbcTemplate, appointmentSql, "tertiary_location");
        assertEquals("tertiary_01", tertiaryLocation.get(0));

        final List<Object> mrdCreatedTime = retrieveColumnValues(jdbcTemplate, appointmentSql, "mrd_created_time");
        assertEquals(getDateTimeStamp("28-04-2022 00:00:00"), mrdCreatedTime.get(0));
        final List<Object> mrdUpdatedTime = retrieveColumnValues(jdbcTemplate, appointmentSql, "mrd_updated_time");
        assertEquals(getDateTimeStamp("28-04-2022 00:00:00"), mrdUpdatedTime.get(0));
        final List<Object> mrdDeletedTime = retrieveColumnValues(jdbcTemplate, appointmentSql, "mrd_deleted_time");
        assertEquals(getDateTimeStamp("28-04-2022 00:00:00"), mrdDeletedTime.get(0));
    }
}
