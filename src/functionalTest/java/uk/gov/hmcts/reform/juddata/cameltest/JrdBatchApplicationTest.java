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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.gov.hmcts.reform.data.ingestion.DataIngestionLibraryRunner;
import uk.gov.hmcts.reform.data.ingestion.configuration.AzureBlobConfig;
import uk.gov.hmcts.reform.data.ingestion.configuration.BlobStorageCredentials;
import uk.gov.hmcts.reform.idam.client.IdamApi;
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

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.file;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithError;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithSingleRecord;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.uploadBlobs;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateAppointmentFile;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateAuthorisationFile;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateDbRecordCountFor;
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
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, file);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);
        JobParameters params = new JobParametersBuilder()
            .addString(jobLauncherTestUtils.getJob().getName(), String.valueOf(System.currentTimeMillis()))
            .toJobParameters();
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 2);
    }

    @Test
    void testTaskletIdempotent() throws Exception {
        //clean context
        SpringStarter.getInstance().restart();
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, file);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);
        JobParameters params = new JobParametersBuilder()
            .addString(jobLauncherTestUtils.getJob().getName(), String.valueOf(System.currentTimeMillis()))
            .toJobParameters();
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        List<Map<String, Object>> auditDetails = jdbcTemplate.queryForList(selectDataLoadSchedulerAudit);
        final Timestamp timestamp = (Timestamp) auditDetails.get(0).get("scheduler_end_time");
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, file);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 2);
        validateDbRecordCountFor(jdbcTemplate, selectDataLoadSchedulerAudit, 5);
        List<Map<String, Object>> auditDetailsNextRun = jdbcTemplate.queryForList(selectDataLoadSchedulerAudit);
        final Timestamp timestampNextRun = (Timestamp) auditDetailsNextRun.get(0).get("scheduler_end_time");
        assertEquals(timestamp, timestampNextRun);
    }

    @Test
    void testParentOrchestration() throws Exception {

        uploadBlobs(jrdBlobSupport, archivalFileNames, true, file);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();

        validateUserProfileFile(jdbcTemplate, userProfileSql);
        validateAppointmentFile(jdbcTemplate, appointmentSql);
        validateAuthorisationFile(jdbcTemplate, authorizationSql);
    }

    @Test
    void testAppointmentFailureRollbacksAppointment() throws Exception {

        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithError);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();

        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 2);
        validateDbRecordCountFor(jdbcTemplate, appointmentSql, 0);
        validateDbRecordCountFor(jdbcTemplate, authorizationSql, 8);
    }

    @Test
    void testParentOrchestrationSingleRecord() throws Exception {

        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithSingleRecord);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();
        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 1);
        validateDbRecordCountFor(jdbcTemplate, appointmentSql, 1);
        validateDbRecordCountFor(jdbcTemplate, authorizationSql, 1);
    }


    @Test
    void testAllLeafs() throws Exception {

        uploadBlobs(jrdBlobSupport, archivalFileNames, true, file);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();

        validateDbRecordCountFor(jdbcTemplate, baseLocationSql, 6);
        validateDbRecordCountFor(jdbcTemplate, regionSql, 6);
    }

    @Test
    void testServiceCodeMappingInJudicialOfficeAuthorisationTable() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, file);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);
        final JobParameters params = new JobParametersBuilder()
                .addString(jobLauncherTestUtils.getJob().getName(), String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        validateDbRecordCountFor(jdbcTemplate, serviceCodeSql, 2);

        final List<Object> serviceCodes = retrieveColumnValues(jdbcTemplate, serviceCodeSql, "service_code");
        assertTrue(serviceCodes.contains("BFA1"));
        assertTrue(serviceCodes.contains("BBA3"));
    }
}
