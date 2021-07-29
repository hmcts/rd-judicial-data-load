package uk.gov.hmcts.reform.juddata.cameltest;

import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.javatuples.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;
import uk.gov.hmcts.reform.data.ingestion.DataIngestionLibraryRunner;
import uk.gov.hmcts.reform.data.ingestion.camel.route.DataLoadRoute;
import uk.gov.hmcts.reform.data.ingestion.configuration.AzureBlobConfig;
import uk.gov.hmcts.reform.data.ingestion.configuration.BlobStorageCredentials;
import uk.gov.hmcts.reform.idam.client.IdamApi;
import uk.gov.hmcts.reform.juddata.camel.util.JrdExecutor;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.JrdBatchIntegrationSupport;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.LeafIntegrationTestSupport;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.SpringStarter;
import uk.gov.hmcts.reform.juddata.client.IdamClient;
import uk.gov.hmcts.reform.juddata.config.LeafCamelConfig;
import uk.gov.hmcts.reform.juddata.config.ParentCamelConfig;
import uk.gov.hmcts.reform.juddata.configuration.BatchConfig;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.sql.DataSource;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.SCHEDULER_START_TIME;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.deleteBlobs;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.file;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.uploadBlobs;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateExceptionDbRecordCount;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateLrdServiceFileException;

@TestPropertySource(properties = {"spring.config.location=classpath:application-integration.yml,"
    + "classpath:application-leaf-integration.yml"})
@MockEndpoints("log:*")
@ContextConfiguration(classes = {ParentCamelConfig.class, LeafCamelConfig.class,
    JobLauncherTestUtils.class, BatchConfig.class, AzureBlobConfig.class, BlobStorageCredentials.class},
    initializers = ConfigDataApplicationContextInitializer.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = JpaRepositoriesAutoConfiguration.class)
@EnableTransactionManagement
@SqlConfig(dataSource = "dataSource", transactionManager = "txManager",
    transactionMode = SqlConfig.TransactionMode.ISOLATED)
@CamelSpringBootTest
@EnableFeignClients(clients = {IdamClient.class, IdamApi.class})
class JrdFileStatusCheckTest extends JrdBatchIntegrationSupport {

    @Autowired
    JrdExecutor jrdExecutor;

    @Autowired
    DataIngestionLibraryRunner dataIngestionLibraryRunner;

    @Value("${truncate-exception}")
    protected String truncateException;

    @Autowired
    @Qualifier("springJdbcDataSource")
    DataSource dataSource;

    @Autowired
    DataLoadRoute dataLoadRoute;

    @Value("${routes-to-execute-leaf}")
    List<String> routesToExecuteLeaf;

    @Value("${routes-to-execute-orchestration}")
    List<String> routesToExecute;


    @Test
    void testTaskletStaleFileErrorDay2WithKeepingDay1Data() throws Exception {

        //Day 1 happy path
        uploadFiles(String.valueOf(new Date(System.currentTimeMillis()).getTime()));

        JobParameters params = new JobParametersBuilder()
            .addString(jobLauncherTestUtils.getJob().getName(), UUID.randomUUID().toString())
            .toJobParameters();
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        deleteBlobs(jrdBlobSupport, archivalFileNames);
        deleteAuditAndExceptionDataOfDay1();

        //Day 2 stale files
        uploadFiles(String.valueOf(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000).getTime()));

        //not ran with dataIngestionLibraryRunner to set stale file via
        // camelContext.getGlobalOptions().remove(SCHEDULER_START_TIME);
        params = new JobParametersBuilder()
            .addString(jobLauncherTestUtils.getJob().getName(), UUID.randomUUID().toString())
            .toJobParameters();
        jobLauncherTestUtils.launchJob(params);
        List<Pair<String, String>> results = ImmutableList.of(new Pair<>(
            "Locations-Test",
            "not loaded due to file stale error"
        ), new Pair<>(
            "BaseLocations-Test",
            "not loaded due to file stale error"
        ), new Pair<>(
            "Personal-Test",
            "not loaded due to file stale error"
        ));


        validateLrdServiceFileException(jdbcTemplate, exceptionQuery, results);
        validateExceptionDbRecordCount(jdbcTemplate, exceptionQuery, 3, false);
        assertEquals(3, jdbcTemplate.queryForList(schedulerInsertJrdSqlFailure).size());

        //validate old day 1 data not gets truncated after day 2 stale file ran
        List<Map<String, Object>> userProfileList = jdbcTemplate.queryForList(userProfileSql);
        assertTrue(userProfileList.size() > 0);
    }


    @Test
    void testTaskletNoFileErrorDay2WithKeepingDay1Data() throws Exception {

        //Day 1 happy path
        uploadFiles(String.valueOf(new Date(System.currentTimeMillis()).getTime()));

        JobParameters params = new JobParametersBuilder()
            .addString(jobLauncherTestUtils.getJob().getName(), UUID.randomUUID().toString())
            .toJobParameters();
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        deleteBlobs(jrdBlobSupport, archivalFileNames);
        deleteAuditAndExceptionDataOfDay1();

        //Day 2 no upload file
        camelContext.getGlobalOptions().put(SCHEDULER_START_TIME,
            String.valueOf(new Date(System.currentTimeMillis()).getTime()));
        params = new JobParametersBuilder()
            .addString(jobLauncherTestUtils.getJob().getName(), UUID.randomUUID().toString())
            .toJobParameters();
        jobLauncherTestUtils.launchJob(params);
        notDeletionFlag = true;
        List<Pair<String, String>> results = ImmutableList.of(new Pair<>(
            "Locations-Test",
            "Locations-Test file does not exist in azure storage account"
        ), new Pair<>(
            "BaseLocations-Test",
            "BaseLocations-Test file does not exist in azure storage account"
        ), new Pair<>(
            "Personal-Test",
            "Personal-Test file does not exist in azure storage account"
        ));

        validateLrdServiceFileException(jdbcTemplate, exceptionQuery, results);
        assertEquals(3, jdbcTemplate.queryForList(schedulerInsertJrdSqlFailure).size());

        //validate old day 1 data not gets truncated after day 2  file not exist ran
        List<Map<String, Object>> appointmentList = jdbcTemplate.queryForList(appointmentSql);
        assertTrue(appointmentList.size() > 0);
        List<Map<String, Object>> profileList = jdbcTemplate.queryForList(userProfileSql);
        assertTrue(profileList.size() > 0);
    }

    private void deleteAuditAndExceptionDataOfDay1() throws Exception {
        jdbcTemplate.execute(truncateAudit);
        jdbcTemplate.execute(truncateException);
        SpringStarter.getInstance().restart();
    }

    private void uploadFiles(String time) throws Exception {
        camelContext.getGlobalOptions().put(SCHEDULER_START_TIME, time);
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, file);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);
    }
}
