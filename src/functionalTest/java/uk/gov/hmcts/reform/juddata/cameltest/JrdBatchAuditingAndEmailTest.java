package uk.gov.hmcts.reform.juddata.cameltest;

import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.CamelTestContextBootstrapper;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.gov.hmcts.reform.data.ingestion.configuration.AzureBlobConfig;
import uk.gov.hmcts.reform.data.ingestion.configuration.BlobStorageCredentials;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.JrdBatchIntegrationSupport;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.LeafIntegrationTestSupport;
import uk.gov.hmcts.reform.juddata.config.LeafCamelConfig;
import uk.gov.hmcts.reform.juddata.config.ParentCamelConfig;
import uk.gov.hmcts.reform.juddata.configuration.BatchConfig;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.SUCCESS;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.JUDICIAL_REF_DATA_ORCHESTRATION;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.ORCHESTRATED_ROUTE;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.file;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithError;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.uploadBlobs;

@TestPropertySource(properties = {"spring.config.location=classpath:application-integration.yml,"
    + "classpath:application-leaf-integration.yml"})
@MockEndpoints("log:*")
@ContextConfiguration(classes = {ParentCamelConfig.class, LeafCamelConfig.class, CamelTestContextBootstrapper.class,
    JobLauncherTestUtils.class, BatchConfig.class, AzureBlobConfig.class, BlobStorageCredentials.class},
    initializers = ConfigFileApplicationContextInitializer.class)
@SpringBootTest
@CamelSpringBootTest
@EnableAutoConfiguration(exclude = JpaRepositoriesAutoConfiguration.class)
@EnableTransactionManagement
@SqlConfig(dataSource = "dataSource", transactionManager = "txManager",
    transactionMode = SqlConfig.TransactionMode.ISOLATED)
class JrdBatchAuditingAndEmailTest extends JrdBatchIntegrationSupport {

    @Test
    void testParentOrchestrationSchedulerSuccess() throws Exception {

        uploadBlobs(jrdBlobSupport, archivalFileNames, true, file);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        jobLauncherTestUtils.launchJob();

        List<Map<String, Object>> dataLoadSchedulerAudit = jdbcTemplate.queryForList(schedulerInsertJrdSqlSuccess);
        assertEquals(SUCCESS, dataLoadSchedulerAudit.get(0).get(FILE_STATUS));
    }

    @Test
    void testParentOrchestrationFailureEmail() throws Exception {

        uploadBlobs(jrdBlobSupport, archivalFileNames, true, fileWithError);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        camelContext.getGlobalOptions().put(ORCHESTRATED_ROUTE, JUDICIAL_REF_DATA_ORCHESTRATION);
        setField(emailService, "mailEnabled", Boolean.FALSE);

        jobLauncherTestUtils.launchJob();
        verify(emailService, times(0)).sendEmail(any(), any());
    }

    @Test
    void testParentOrchestrationSuccessEmail() throws Exception {
        uploadBlobs(jrdBlobSupport, archivalFileNames, true, file);
        uploadBlobs(jrdBlobSupport, archivalFileNames, false, LeafIntegrationTestSupport.file);

        camelContext.getGlobalOptions().put(ORCHESTRATED_ROUTE, JUDICIAL_REF_DATA_ORCHESTRATION);

        jobLauncherTestUtils.launchJob();
        verify(emailService, times(0)).sendEmail(any(), any());
    }
}
