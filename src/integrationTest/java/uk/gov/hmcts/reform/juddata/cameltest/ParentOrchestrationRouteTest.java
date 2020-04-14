package uk.gov.hmcts.reform.juddata.cameltest;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.JUDICIAL_USER_PROFILE_ORCHESTRATION;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SCHEDULER_START_TIME;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.IntegrationTestSupport.setSourcePath;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.file;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithError;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.ParentIntegrationTestSupport.fileWithSingleRecord;
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
import uk.gov.hmcts.reform.juddata.camel.util.DataLoadAudit;
import uk.gov.hmcts.reform.juddata.camel.util.MappingConstants;
import uk.gov.hmcts.reform.juddata.config.ParentCamelConfig;

@TestPropertySource(properties = {"spring.config.location=classpath:application-integration.yml"})
@RunWith(CamelSpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@MockEndpoints("log:*")
@ContextConfiguration(classes = {ParentCamelConfig.class, CamelTestContextBootstrapper.class}, initializers = ConfigFileApplicationContextInitializer.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = JpaRepositoriesAutoConfiguration.class)
@EnableTransactionManagement
public class ParentOrchestrationRouteTest {

    public static final String DB_SCHEDULER_STATUS = "scheduler_status";

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

    @Value("${insert-default-role}")
    private String insertDefaultRole;

    @Value("${insert-default-role1}")
    private String insertDefaultRole1;

    @Value("${select-dataload-schedular-audit}")
    String selectDataloadSchedularAudit;

    private static final String[] file = {"classpath:sourceFiles/judicial_userprofile.csv", "classpath:sourceFiles/judicial_appointments.csv"};

    @Value("${insert-default-region}")
    private String insertDefaultRegion;

    @Value("${insert-default-base-location}")
    private String insertDefaultContract;

    @Value("${insert-default-contract}")
    private String insertDefaultBaseLocation;

    @Value("${scheduler-insert-sql}")
    private String schedulerInsertJrdSql;

    @Value("${select-dataload-schedular-audit-failure}")
    private String schedulerInsertJrdSqlFailure;

    @Value("${select-dataload-schedular-audit-partial-sucess}")
    private String schedulerInsertJrdSqlPartialSucess;

    @Value("${select-dataload-schedular-audit-sucess}")
    private String schedulerInsertJrdSqlSucess;

    @Autowired
    DataLoadAudit dataLoadAudit;

    @Value("${audit-enable}")
    Boolean auditEnable;


    @BeforeClass
    public static void beforeAll() throws Exception {
        setSourcePath("classpath:archivalFiles", "archival.path");
        setSourcePath("classpath:sourceFiles", "active.path");
    }

    @Before
    public void init() {
        jdbcTemplate.execute(truncateAllTable);
        jdbcTemplate.execute(insertDefaultRole);
        jdbcTemplate.execute(insertDefaultRole1);
        jdbcTemplate.execute(insertDefaultRegion);
        jdbcTemplate.execute(insertDefaultContract);
        jdbcTemplate.execute(insertDefaultBaseLocation);
        camelContext.getGlobalOptions().put(SCHEDULER_START_TIME, String.valueOf(new Date().getTime()));
        camelContext.getGlobalOptions().put(MappingConstants.ORCHESTRATED_ROUTE, JUDICIAL_USER_PROFILE_ORCHESTRATION);
        camelContext.getGlobalOptions().put(MappingConstants.SCHEDULER_START_TIME, MappingConstants.getCurrentTimeStamp().toString());

    }

    @Test
    public void testParentOrchestration() throws Exception {

        setSourceData(file);
        parentRoute.startRoute();
        producerTemplate.sendBody(startRoute, "test JRD orchestration");

        List<Map<String, Object>> judicialUserProfileList = jdbcTemplate.queryForList(sql);
        assertEquals(judicialUserProfileList.size(), 2);
        assertNotNull(judicialUserProfileList.get(0));
        assertNotNull(judicialUserProfileList.get(1));
        assertEquals(judicialUserProfileList.get(0).get("elinks_id"), "1");
        assertEquals(judicialUserProfileList.get(1).get("elinks_id"), "2");
        assertEquals(judicialUserProfileList.get(0).get("email_id"), "joe.bloggs@ejudiciary.net");
        assertEquals(judicialUserProfileList.get(1).get("email_id"), "jo1e.bloggs@ejudiciary.net");

        List<Map<String, Object>> judicialAppointmentList = jdbcTemplate.queryForList(sqlChild1);
        assertEquals(judicialAppointmentList.size(), 2);
        assertNotNull(judicialAppointmentList.get(0));
        assertNotNull(judicialAppointmentList.get(1));
        assertNotNull(judicialAppointmentList.get(0).get("judicial_office_appointment_id"));
        assertNotNull(judicialAppointmentList.get(0).get("judicial_office_appointment_id"));
        assertEquals(judicialAppointmentList.get(0).get("elinks_id"), "1");
        assertEquals(judicialAppointmentList.get(1).get("elinks_id"), "2");
    }


    @Test
    public void testParentOrchestrationFailure() throws Exception {

        setSourceData(fileWithError);
        parentRoute.startRoute();
        producerTemplate.sendBody(startRoute, "test JRD orchestration");

        List<Map<String, Object>> judicialUserProfileList = jdbcTemplate.queryForList(sql);
        assertEquals(judicialUserProfileList.size(), 0);

        List<Map<String, Object>> judicialAppointmentList = jdbcTemplate.queryForList(sqlChild1);
        assertEquals(judicialAppointmentList.size(), 0);
    }

    @Test
    public void testParentOrchestrationFailureRollBackKeepExistingData() throws Exception {

        // Day 1 Success data load
        setSourceData(file);
        parentRoute.startRoute();
        producerTemplate.sendBody(startRoute, "test JRD orchestration");

        // Day 2 Success load fails
        setSourceData(fileWithError);
        producerTemplate.sendBody(startRoute, "test JRD orchestration");

        //Keeps Day1 data load state and roll back day2
        List<Map<String, Object>> judicialUserProfileList = jdbcTemplate.queryForList(sql);
        assertEquals(judicialUserProfileList.size(), 2);
        assertNotNull(judicialUserProfileList.get(0));
        assertNotNull(judicialUserProfileList.get(1));
        assertEquals(judicialUserProfileList.get(0).get("elinks_id"), "1");
        assertEquals(judicialUserProfileList.get(1).get("elinks_id"), "2");
        assertEquals(judicialUserProfileList.get(0).get("email_id"), "joe.bloggs@ejudiciary.net");
        assertEquals(judicialUserProfileList.get(1).get("email_id"), "jo1e.bloggs@ejudiciary.net");

        List<Map<String, Object>> judicialAppointmentList = jdbcTemplate.queryForList(sqlChild1);
        assertEquals(judicialAppointmentList.size(), 2);
        assertNotNull(judicialAppointmentList.get(0));
        assertNotNull(judicialAppointmentList.get(1));
        assertNotNull(judicialAppointmentList.get(0).get("judicial_office_appointment_id"));
        assertNotNull(judicialAppointmentList.get(0).get("judicial_office_appointment_id"));
        assertEquals(judicialAppointmentList.get(0).get("elinks_id"), "1");
        assertEquals(judicialAppointmentList.get(1).get("elinks_id"), "2");
    }

    @Test
    public void testParentOrchestrationSingleRecord() throws Exception {

        setSourceData(fileWithSingleRecord);
        parentRoute.startRoute();
        producerTemplate.sendBody(startRoute, "test JRD orchestration");
        List<Map<String, Object>> judicialUserProfileList = jdbcTemplate.queryForList(sql);
        assertEquals(judicialUserProfileList.size(), 1);

        List<Map<String, Object>> judicialAppointmentList = jdbcTemplate.queryForList(sqlChild1);
        assertEquals(judicialAppointmentList.size(), 1);
    }


    @Test
    public void testParentOrchestrationSchedularFailure() throws Exception {


        setSourceData(fileWithError);

        parentRoute.startRoute();
        producerTemplate.sendBody(startRoute, "test JRD orchestration");

        List<Map<String, Object>>  dataloadSchedularAudit = jdbcTemplate.queryForList(schedulerInsertJrdSqlFailure);
        assertEquals(dataloadSchedularAudit.get(0).get(DB_SCHEDULER_STATUS), MappingConstants.FAILURE);
    }

    @Test
    public void testParentOrchestrationSchedularSucess() throws Exception {

        setSourceData(file);

        parentRoute.startRoute();
        producerTemplate.sendBody(startRoute, "test JRD orchestration");

        List<Map<String, Object>>  dataloadSchedularAudit = jdbcTemplate.queryForList(schedulerInsertJrdSqlSucess);
        assertEquals(dataloadSchedularAudit.get(0).get(DB_SCHEDULER_STATUS), MappingConstants.SUCCESS);
    }

    @Test
    public void testParentOrchestrationSchedularPartialSucess() throws Exception {
        setSourceData(file);

        camelContext.getGlobalOptions().put(MappingConstants.SCHEDULER_STATUS, MappingConstants.PARTIAL_SUCCESS);
        parentRoute.startRoute();
        producerTemplate.sendBody(startRoute, "test JRD orchestration");

        List<Map<String, Object>>  dataloadSchedularAudit = jdbcTemplate.queryForList(schedulerInsertJrdSqlPartialSucess);
        assertEquals(dataloadSchedularAudit.get(0).get(DB_SCHEDULER_STATUS), MappingConstants.PARTIAL_SUCCESS);
    }


}

