package uk.gov.hmcts.reform.juddata.cameltest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.JUDICIAL_USER_PROFILE_ORCHESTRATION;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SCHEDULER_START_TIME;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.IntegrationTestSupport.setSourcePath;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithInvalidHeader;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithInvalidJsr;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithInvalidJsrExceedsThreshold;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.setSourceData;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.gov.hmcts.reform.juddata.camel.route.ParentOrchestrationRoute;
import uk.gov.hmcts.reform.juddata.camel.util.MappingConstants;
import uk.gov.hmcts.reform.juddata.config.CamelConfig;

@TestPropertySource(properties = {"spring.config.location=classpath:application-integration.yml"})
@RunWith(CamelSpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@MockEndpoints("log:*")
@ContextConfiguration(classes = {CamelConfig.class, CamelTestContextBootstrapper.class}, initializers = ConfigFileApplicationContextInitializer.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = JpaRepositoriesAutoConfiguration.class)
@EnableTransactionManagement
public class ParentOrchestrationRouteValidationTest {

    @Autowired
    protected CamelContext camelContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    ParentOrchestrationRoute parentRoute;

    @Value("${start-route}")
    private String startRoute;

    @Autowired
    ProducerTemplate producerTemplate;

    @Value("${parent-select-jrd-sql}")
    private String sql;

    @Value("${child-select-child1-sql}")
    private String sqlChild1;

    @Value("${truncate-jrd}")
    private String truncateAllTable;

    @Value("${archival-cred}")
    String archivalCred;

    @Value("${exception-select-query}")
    String exceptionQuery;

    @Value("${exception-truncate-query}")
    String exceptionTruncateQuery;


    @BeforeClass
    public static void beforeAll() throws Exception {
        setSourcePath("classpath:archivalFiles", "archival.path");
        setSourcePath("classpath:sourceFiles", "active.path");
    }

    @Before
    public void init() {
        jdbcTemplate.execute(truncateAllTable);
        jdbcTemplate.execute(exceptionTruncateQuery);
        camelContext.getGlobalOptions().put(SCHEDULER_START_TIME, String.valueOf(new Date().getTime()));
    }

    @Test
    public void testParentOrchestrationInvalidHeaderRollback() throws Exception {
        setSourceData(fileWithInvalidHeader);
        camelContext.getGlobalOptions().put(MappingConstants.ORCHESTRATED_ROUTE, JUDICIAL_USER_PROFILE_ORCHESTRATION);
        parentRoute.startRoute();
        producerTemplate.sendBody(startRoute, "test JRD orchestration");

        List<Map<String, Object>> judicialUserProfileList = jdbcTemplate.queryForList(sql);
        assertEquals(judicialUserProfileList.size(), 0);

        List<Map<String, Object>> judicialAppointmentList = jdbcTemplate.queryForList(sqlChild1);
        assertEquals(judicialAppointmentList.size(), 0);

        List<Map<String, Object>> exceptionList = jdbcTemplate.queryForList(exceptionQuery);
        assertNotNull(exceptionList.get(0).get("file_name"));
        assertNotNull(exceptionList.get(0).get("scheduler_start_time"));
        assertNotNull(exceptionList.get(0).get("error_description"));
        assertNotNull(exceptionList.get(0).get("updated_timestamp"));
        assertEquals(exceptionList.size(), 1);
    }

    @Test
    public void testParentOrchestrationJsrAuditTest() throws Exception {
        setSourceData(fileWithInvalidJsr);
        camelContext.getGlobalOptions().put(MappingConstants.ORCHESTRATED_ROUTE, JUDICIAL_USER_PROFILE_ORCHESTRATION);
        parentRoute.startRoute();
        producerTemplate.sendBody(startRoute, "test JRD orchestration");

        List<Map<String, Object>> judicialUserProfileList = jdbcTemplate.queryForList(sql);
        assertEquals(judicialUserProfileList.size(), 2);

        List<Map<String, Object>> judicialAppointmentList = jdbcTemplate.queryForList(sqlChild1);
        assertEquals(judicialAppointmentList.size(), 2);

        List<Map<String, Object>> exceptionList = jdbcTemplate.queryForList(exceptionQuery);

        for (int count = 0; count < 6; count++) {
            assertNotNull(exceptionList.get(0).get("table_name"));
            assertNotNull(exceptionList.get(0).get("scheduler_start_time"));
            assertNotNull(exceptionList.get(0).get("key"));
            assertNotNull(exceptionList.get(0).get("field_in_error"));
            assertNotNull(exceptionList.get(0).get("error_description"));
            assertNotNull(exceptionList.get(0).get("updated_timestamp"));
        }
        assertEquals(exceptionList.size(), 6);
    }

    @Test(expected = Exception.class)
    public void testParentOrchestrationJsrExceedsThresholdAuditTest() throws Exception {
        setSourceData(fileWithInvalidJsrExceedsThreshold);
        camelContext.getGlobalOptions().put(MappingConstants.ORCHESTRATED_ROUTE, JUDICIAL_USER_PROFILE_ORCHESTRATION);
        parentRoute.startRoute();
        producerTemplate.sendBody(startRoute, "test JRD orchestration");
    }
}
