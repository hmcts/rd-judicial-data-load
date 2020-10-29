package uk.gov.hmcts.reform.juddata.cameltest;

import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.apache.camel.test.spring.MockEndpoints;
import org.javatuples.Pair;
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
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;
import uk.gov.hmcts.reform.data.ingestion.configuration.AzureBlobConfig;
import uk.gov.hmcts.reform.data.ingestion.configuration.BlobStorageCredentials;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.JrdBatchIntegrationSupport;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.LeafIntegrationTestSupport;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.RestartingSpringJUnit4ClassRunner;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.SpringRestarter;
import uk.gov.hmcts.reform.juddata.config.LeafCamelConfig;
import uk.gov.hmcts.reform.juddata.config.ParentCamelConfig;
import uk.gov.hmcts.reform.juddata.configuration.BatchConfig;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.SCHEDULER_START_TIME;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.file;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.uploadBlobs;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateExceptionDbRecordCount;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.validateLrdServiceFileException;


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
public class JrdFileStatusCheckTest extends JrdBatchIntegrationSupport {

    @Before
    public void init() {
        jdbcTemplate.execute(truncateAudit);
        SpringRestarter.getInstance().restart();
    }


    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql", "/testData/default-leaf-load.sql",
        "/testData/truncate-exception.sql"})
    public void testTaskletStaleFileError() throws Exception {

        camelContext.getGlobalOptions()
            .put(SCHEDULER_START_TIME, String.valueOf(
                new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000).getTime()));

        uploadBlobs(jrdBlobSupport, archivalFileNames, true, file);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();
        List<Pair<String, String>> results = ImmutableList.of(new Pair<>(
            "Roles-Test",
            "not loaded due to file stale error"
        ), new Pair<>(
            "Contracts-Test",
            "not loaded due to file stale error"
        ), new Pair<>(
            "Locations-Test",
            "not loaded due to file stale error"
        ), new Pair<>(
            "BaseLocations-Test",
            "not loaded due to file stale error"
        ),new Pair<>(
            "Personal-Test",
            "not loaded due to file stale error"
        ),new Pair<>(
            "Appointments-Test",
            "not loaded due to file stale error"
        ),new Pair<>(
            "Authorisations-Test",
            "not loaded due to file stale error"
        ));
       
        validateLrdServiceFileException(jdbcTemplate, exceptionQuery, results);
        validateExceptionDbRecordCount(jdbcTemplate, exceptionQuery, 7, false);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-parent.sql",
        "/testData/truncate-exception.sql"})
    public void testTaskletNoFileError() throws Exception {
        camelContext.getGlobalOptions()
            .put(SCHEDULER_START_TIME, String.valueOf(new Date(System.currentTimeMillis()).getTime()));

        jobLauncherTestUtils.launchJob();
        notDeletionFlag = true;
        List<Pair<String, String>> results = ImmutableList.of(new Pair<>(
            "Roles-Test",
            "Roles-Test file is not exists in container"
        ), new Pair<>(
            "Contracts-Test",
            "Contracts-Test file is not exists in container"
        ), new Pair<>(
            "Locations-Test",
            "Locations-Test file is not exists in container"
        ), new Pair<>(
            "BaseLocations-Test",
            "BaseLocations-Test file is not exists in container"
        ),new Pair<>(
            "Personal-Test",
            "Personal-Test file is not exists in container"
        ),new Pair<>(
            "Appointments-Test",
            "Appointments-Test file is not exists in container"
        ),new Pair<>(
            "Authorisations-Test",
            "Authorisations-Test file is not exists in container"
        ));

        validateLrdServiceFileException(jdbcTemplate, exceptionQuery, results);
        var result = jdbcTemplate.queryForList(selectDataLoadSchedulerAudit);
        assertEquals(0, result.size());
    }
}
