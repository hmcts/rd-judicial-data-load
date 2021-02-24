package uk.gov.hmcts.reform.juddata.cameltest;

import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.gov.hmcts.reform.data.ingestion.DataIngestionLibraryRunner;
import uk.gov.hmcts.reform.data.ingestion.configuration.AzureBlobConfig;
import uk.gov.hmcts.reform.data.ingestion.configuration.BlobStorageCredentials;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.JrdBatchIntegrationSupport;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.LeafIntegrationTestSupport;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.RestartingSpringJUnit4ClassRunner;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.SpringRestarter;
import uk.gov.hmcts.reform.juddata.config.LeafCamelConfig;
import uk.gov.hmcts.reform.juddata.config.ParentCamelConfig;
import uk.gov.hmcts.reform.juddata.configuration.BatchConfig;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.SCHEDULER_START_TIME;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.file;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithError;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithSingleRecord;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.uploadBlobs;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateAppointmentFile;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateAuthorisationFile;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateDbRecordCountFor;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateUserProfileFile;

@TestPropertySource(properties = {"spring.config.location=classpath:application-integration.yml,"
    + "classpath:application-leaf-integration.yml"})
@RunWith(RestartingSpringJUnit4ClassRunner.class)
@MockEndpoints("log:*")
@ContextConfiguration(classes = {ParentCamelConfig.class, LeafCamelConfig.class, CamelTestContextBootstrapper.class,
    JobLauncherTestUtils.class, BatchConfig.class, AzureBlobConfig.class, BlobStorageCredentials.class},
    initializers = ConfigFileApplicationContextInitializer.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = JpaRepositoriesAutoConfiguration.class)
@EnableTransactionManagement
@SqlConfig(dataSource = "dataSource", transactionManager = "txManager",
    transactionMode = SqlConfig.TransactionMode.ISOLATED)
public class JrdBatchApplicationTest extends JrdBatchIntegrationSupport {

    @Autowired
    DataIngestionLibraryRunner dataIngestionLibraryRunner;

    @Before
    public void init() {
        jdbcTemplate.execute(truncateAudit);
        SpringRestarter.getInstance().restart();
        camelContext.getGlobalOptions()
            .put(SCHEDULER_START_TIME, String.valueOf(new Date(System.currentTimeMillis()).getTime()));
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/default-leaf-load.sql"})
    public void testTasklet() throws Exception {

        uploadBlobs(jrdBlobSupport, archivalFileNames, true, file);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();

        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 2);
        validateDbRecordCountFor(jdbcTemplate, roleSql, 6);

    }


    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/default-leaf-load.sql",
        "/testData/truncate-exception.sql"})
    public void testTaskletIdempotent() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, file);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);
        JobParameters params = new JobParametersBuilder()
            .addString(jobLauncherTestUtils.getJob().getName(), String.valueOf(System.currentTimeMillis()))
            .toJobParameters();
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        List<Map<String, Object>> auditDetails = jdbcTemplate.queryForList(selectDataLoadSchedulerAudit);
        final Timestamp timestamp = (Timestamp) auditDetails.get(0).get("scheduler_end_time");
        SpringRestarter.getInstance().restart();
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, file);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 2);
        validateDbRecordCountFor(jdbcTemplate, roleSql, 6);
        validateDbRecordCountFor(jdbcTemplate, selectDataLoadSchedulerAudit, 7);
        List<Map<String, Object>> auditDetailsNextRun = jdbcTemplate.queryForList(selectDataLoadSchedulerAudit);
        final Timestamp timestampNextRun = (Timestamp) auditDetailsNextRun.get(0).get("scheduler_end_time");
        assertEquals(timestamp, timestampNextRun);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/default-leaf-load.sql"})
    public void testParentOrchestration() throws Exception {

        uploadBlobs(jrdBlobSupport, archivalFileNames, true, file);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();

        validateUserProfileFile(jdbcTemplate, userProfileSql);
        validateAppointmentFile(jdbcTemplate, appointmentSql);
        validateAuthorisationFile(jdbcTemplate, authorizationSql);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/default-leaf-load.sql"})
    public void testAppointmentFailureRollbacksAppointment() throws Exception {

        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithError);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();

        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 2);
        validateDbRecordCountFor(jdbcTemplate, appointmentSql, 0);
        validateDbRecordCountFor(jdbcTemplate, authorizationSql, 8);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/default-leaf-load.sql"})
    public void testParentOrchestrationSingleRecord() throws Exception {

        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithSingleRecord);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();
        validateDbRecordCountFor(jdbcTemplate, userProfileSql, 1);
        validateDbRecordCountFor(jdbcTemplate, appointmentSql, 1);
        validateDbRecordCountFor(jdbcTemplate, authorizationSql, 1);
    }


    @Test
    @Sql(scripts = {"/testData/truncate-leaf.sql"})
    public void testAllLeafs() throws Exception {

        uploadBlobs(jrdBlobSupport, archivalFileNames, true, file);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();

        validateDbRecordCountFor(jdbcTemplate, roleSql, 5);
        validateDbRecordCountFor(jdbcTemplate, contractSql, 7);
        validateDbRecordCountFor(jdbcTemplate, baseLocationSql, 5);
        validateDbRecordCountFor(jdbcTemplate, regionSql, 5);
    }
}
