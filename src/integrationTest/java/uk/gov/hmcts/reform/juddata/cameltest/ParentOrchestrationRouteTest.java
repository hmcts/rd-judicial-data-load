package uk.gov.hmcts.reform.juddata.cameltest;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.JUDICIAL_USER_PROFILE_ORCHESTRATION;

import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ResourceUtils;
import uk.gov.hmcts.reform.juddata.camel.processor.ExceptionProcessor;
import uk.gov.hmcts.reform.juddata.camel.route.ParentOrchestrationRoute;
import uk.gov.hmcts.reform.juddata.camel.util.MappingConstants;
import uk.gov.hmcts.reform.juddata.config.CamelConfig;

@TestPropertySource(properties = {"spring.config.location=classpath:application-integration.yml"})
@RunWith(CamelSpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@MockEndpoints("log:*")
@ContextConfiguration(classes = {CamelConfig.class, CamelTestContextBootstrapper.class}, initializers = ConfigFileApplicationContextInitializer.class)
@SpringBootTest
@EnableAutoConfiguration
public class ParentOrchestrationRouteTest {

    private static final String[] file = {"classpath:sourceFiles/judicial_userprofile.csv", "classpath:sourceFiles/judicial_appointments.csv"};
    private static final String[] fileWithError = {"classpath:sourceFiles/judicial_userprofile.csv", "classpath:sourceFiles/judicial_appointments_error.csv"};
    private static final String[] fileWithSingleRecord = {"classpath:sourceFiles/judicial_userprofile_singlerecord.csv", "classpath:sourceFiles/judicial_appointments_singlerecord.csv"};
    @Autowired
    protected CamelContext camelContext;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    uk.gov.hmcts.reform.juddata.camel.service.EmailService emailService;

    @Autowired
    ExceptionProcessor exceptionProcessor;

    @Autowired
    ParentOrchestrationRoute parentRoute;

    @Autowired
    ProducerTemplate producerTemplate;

    @Value("${archival-cred}")
    String archivalCred;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${start-route}")
    private String startRoute;

    @Value("${spring.mail.enabled}")
    private Boolean mailEnabled;

    @Value("${parent-select-jrd-sql}")
    private String sql;

    @Value("${child-select-child1-sql}")
    private String sqlChild1;

    @Value("${truncate-jrd}")
    private String truncateAllTable;

    @BeforeClass
    public static void beforeAll() throws Exception {
        setSourcePath("classpath:archivalFiles", "archival.path");
        setSourcePath("classpath:sourceFiles", "active.path");
    }

    private static void setSourceData(String... files) throws Exception {
        setSourcePath(files[0],
            "parent.file.path");
        setSourcePath(files[1],
            "child1.file.path");
    }

    private static void setSourcePath(String path, String propertyPlaceHolder) throws Exception {

        String loadFile = ResourceUtils.getFile(path).getCanonicalPath();

        if (loadFile.endsWith(".csv")) {
            int lastSlash = loadFile.lastIndexOf("/");
            String result = loadFile.substring(0, lastSlash);
            String fileName = loadFile.substring(lastSlash + 1);

            System.setProperty(propertyPlaceHolder, "file:"
                + result + "?fileName=" + fileName + "&noop=true");
        } else {
            System.setProperty(propertyPlaceHolder, "file:" + loadFile.replaceFirst("/", ""));
        }
    }

    @Before
    public void init() {
        jdbcTemplate.execute(truncateAllTable);
    }

    @Test
    public void testParentOrchestration() throws Exception {

        setSourceData(file);
        camelContext.getGlobalOptions().put(MappingConstants.ORCHESTRATED_ROUTE, JUDICIAL_USER_PROFILE_ORCHESTRATION);
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
        camelContext.getGlobalOptions().put(MappingConstants.ORCHESTRATED_ROUTE, JUDICIAL_USER_PROFILE_ORCHESTRATION);
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

        camelContext.getGlobalOptions().put(MappingConstants.ORCHESTRATED_ROUTE, JUDICIAL_USER_PROFILE_ORCHESTRATION);
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

        camelContext.getGlobalOptions().put(MappingConstants.ORCHESTRATED_ROUTE, JUDICIAL_USER_PROFILE_ORCHESTRATION);
        parentRoute.startRoute();
        producerTemplate.sendBody(startRoute, "test JRD orchestration");
        List<Map<String, Object>> judicialUserProfileList = jdbcTemplate.queryForList(sql);
        assertEquals(judicialUserProfileList.size(), 1);

        List<Map<String, Object>> judicialAppointmentList = jdbcTemplate.queryForList(sqlChild1);
        assertEquals(judicialAppointmentList.size(), 1);
    }

    @Test
    public void testParentOrchestrationFailureEmail() throws Exception {
        setSourceData(fileWithError);
        camelContext.getGlobalOptions().put(MappingConstants.ORCHESTRATED_ROUTE, JUDICIAL_USER_PROFILE_ORCHESTRATION);
        camelContext.getGlobalOptions().put(Exchange.FAILURE_ROUTE_ID, "Rout 1");
        ReflectionTestUtils.setField(exceptionProcessor, "mailEnabled", Boolean.FALSE);
        parentRoute.startRoute();
        producerTemplate.sendBody(startRoute, "test JRD orchestration");
        Mockito.verify(emailService, Mockito.times(0)).sendEmail(Mockito.any(String.class), Mockito.any(uk.gov.hmcts.reform.juddata.camel.service.EmailData.class));
    }

    @Test
    public void testParentOrchestrationSucessEmail() throws Exception {


        setSourceData(fileWithError);
        camelContext.getGlobalOptions().put(MappingConstants.ORCHESTRATED_ROUTE, JUDICIAL_USER_PROFILE_ORCHESTRATION);
        parentRoute.startRoute();
        producerTemplate.sendBody(startRoute, "test JRD orchestration");
        Mockito.verify(emailService, Mockito.times(2)).sendEmail(Mockito.any(String.class), Mockito.any(uk.gov.hmcts.reform.juddata.camel.service.EmailData.class));

    }
}

