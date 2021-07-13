package uk.gov.hmcts.reform.juddata.cameltest;

import com.launchdarkly.sdk.server.LDClient;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.testcontainers.shaded.com.google.common.collect.ImmutableSet;
import uk.gov.hmcts.reform.data.ingestion.DataIngestionLibraryRunner;
import uk.gov.hmcts.reform.data.ingestion.camel.util.DataLoadUtil;
import uk.gov.hmcts.reform.data.ingestion.configuration.AzureBlobConfig;
import uk.gov.hmcts.reform.data.ingestion.configuration.BlobStorageCredentials;
import uk.gov.hmcts.reform.idam.client.IdamApi;
import uk.gov.hmcts.reform.juddata.camel.util.JrdSidamTokenService;
import uk.gov.hmcts.reform.juddata.client.IdamClient;
import uk.gov.hmcts.reform.juddata.config.LeafConfig;
import uk.gov.hmcts.reform.juddata.config.ParentConfig;
import uk.gov.hmcts.reform.juddata.configuration.BatchConfig;
import uk.gov.hmcts.reform.juddata.configuration.FeignConfiguration;
import uk.gov.hmcts.reform.juddata.support.JrdBatchIntegrationSupport;
import uk.gov.hmcts.reform.juddata.support.LeafIntegrationSupport;
import uk.gov.hmcts.reform.juddata.support.ParentIntegrationTestSupport;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.PARTIAL_SUCCESS;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.INVALID_JSR_PARENT_ROW;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.MISSING_PER_ID;
import static uk.gov.hmcts.reform.juddata.support.IntegrationTestSupport.setSourcePath;
import static uk.gov.hmcts.reform.juddata.support.ParentIntegrationTestSupport.setSourceData;


@TestPropertySource(properties = {"spring.config.location=classpath:application-integration-test.yml,"
    + "classpath:application-leaf-integration-test.yml"})
@MockEndpoints("log:*")
@ContextConfiguration(classes = {ParentConfig.class, LeafConfig.class,
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
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
@SuppressWarnings("unchecked")
class JrdBatchApplicationIntegrationTest extends JrdBatchIntegrationSupport {

    @Autowired
    DataIngestionLibraryRunner dataIngestionLibraryRunner;

    @Autowired
    JrdSidamTokenService jrdSidamTokenService;

    @Autowired
    LDClient ldClient;

    @Value("${failed-records-exception}")
    protected String failedRecords;

    @BeforeAll
    public static void beforeAll() throws Exception {
        setSourcePath("classpath:archivalFiles", "archival.path");
        setSourcePath("classpath:sourceFiles", "active.path");
        System.setProperty("archival.file.names", "classpath:sourceFiles/judicial_userprofile_jsr.csv,"
            + "classpath:sourceFiles/judicial_appointments_jsr.csv,"
            + "classpath:sourceFiles/judicial_office_authorisation_jsr_partial_success.csv");
        setSourceData(ParentIntegrationTestSupport.fileWithInvalidJsr);
        LeafIntegrationSupport.setSourceData(LeafIntegrationSupport.file_jsr_error);
    }

    @BeforeEach
    public void before() {
        IdamClient.User user = new IdamClient.User();
        user.setSsoId(UUID.randomUUID().toString());
        user.setId(UUID.randomUUID().toString());
        Set<IdamClient.User> sidamUsers = ImmutableSet.of(user);
        when(jrdSidamTokenService.getSyncFeed()).thenReturn(sidamUsers);
        when(ldClient.boolVariation(anyString(), any(), anyBoolean())).thenReturn(true);
    }


    @Test
    @RefreshScope
    void testTasklet() throws Exception {
        setSourceData(ParentIntegrationTestSupport.file);
        LeafIntegrationSupport.setSourceData(LeafIntegrationSupport.file);
        JobParameters params = new JobParametersBuilder()
            .addString(jobLauncherTestUtils.getJob().getName(), String.valueOf(System.currentTimeMillis()))
            .toJobParameters();
        dataIngestionLibraryRunner.run(jobLauncherTestUtils.getJob(), params);
        assertEquals(6, jdbcTemplate.queryForList(userProfileSql).size());
        List<Map<String, Object>> dataLoadSchedulerAudit = jdbcTemplate
            .queryForList(selectDataLoadSchedulerAudit);
        assertEquals(PARTIAL_SUCCESS, dataLoadSchedulerAudit.get(0).get(FILE_STATUS));
        assertEquals(PARTIAL_SUCCESS,
            DataLoadUtil.getFileDetails(camelContext,
                "classpath:sourceFiles/judicial_userprofile_jsr.csv").getAuditStatus());
        validateExceptionDbRecordCount(jdbcTemplate, exceptionQuery, 20, true);
        validateForeignKeyRecordsAndMissingParentRecords();
    }

    private void validateForeignKeyRecordsAndMissingParentRecords() {
        String[] parameters = new String[]{INVALID_JSR_PARENT_ROW, "judicial-office-appointment"};
        validateExceptionDbRecordCount(jdbcTemplate, failedRecords, 1,
            true, parameters);
        parameters = new String[]{INVALID_JSR_PARENT_ROW, "judicial_office_authorisation"};
        validateExceptionDbRecordCount(jdbcTemplate, failedRecords, 1,
            true, parameters);
        parameters = new String[]{MISSING_PER_ID, "judicial-office-appointment"};
        validateExceptionDbRecordCount(jdbcTemplate, failedRecords, 1,
            true, parameters);
        parameters = new String[]{MISSING_PER_ID, "judicial_office_authorisation"};
        validateExceptionDbRecordCount(jdbcTemplate, failedRecords, 1,
            true, parameters);
    }

    static void validateExceptionDbRecordCount(JdbcTemplate jdbcTemplate,
                                               String queryName, int expectedCount,
                                               boolean isPartialSuccessValidation, String... params) {
        List<Map<String, Object>> exceptionList;
        if (isNotEmpty(params)) {
            exceptionList = jdbcTemplate.queryForList(queryName, params);
        } else {
            exceptionList = jdbcTemplate.queryForList(queryName);
        }

        exceptionList.forEach(exception -> {
            assertTrue(isNotEmpty(exception.get("scheduler_name")));
            assertTrue(isNotEmpty(exception.get("scheduler_start_time")));
            assertTrue(isNotEmpty(exception.get("error_description")));
            assertTrue(isNotEmpty(exception.get("updated_timestamp")));
            if (isPartialSuccessValidation) {
                assertTrue(isNotEmpty(exception.get("table_name")));
                if (isNotEmpty(params)) {
                    assertTrue(isNotEmpty(exception.get("key")));
                } else {
                    assertNotNull((exception.get("key")));
                }
                assertTrue(isNotEmpty(exception.get("field_in_error")));
            }
        });
        assertEquals(expectedCount, exceptionList.size());
    }
}
