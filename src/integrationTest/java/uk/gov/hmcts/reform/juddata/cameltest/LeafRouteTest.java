package uk.gov.hmcts.reform.juddata.cameltest;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.JUDICIAL_USER_PROFILE_ORCHESTRATION;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SCHEDULER_START_TIME;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.IntegrationTestSupport.setSourcePath;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.LeafIntegrationTestSupport.file;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.LeafIntegrationTestSupport.file_error;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.LeafIntegrationTestSupport.setSourceData;

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
import uk.gov.hmcts.reform.juddata.camel.route.LeafTableRoute;
import uk.gov.hmcts.reform.juddata.camel.util.MappingConstants;
import uk.gov.hmcts.reform.juddata.config.CamelConfig;
import uk.gov.hmcts.reform.juddata.config.CamelLeafConfig;

@TestPropertySource(properties = {"spring.config.location=classpath:application-integration.yml,classpath:application-leaf-integration.yml"})
@RunWith(CamelSpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@MockEndpoints("log:*")
@ContextConfiguration(classes = {CamelConfig.class, CamelLeafConfig.class, CamelTestContextBootstrapper.class},
        initializers = ConfigFileApplicationContextInitializer.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = JpaRepositoriesAutoConfiguration.class)
@EnableTransactionManagement
public class LeafRouteTest {

    @Autowired
    protected CamelContext camelContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    LeafTableRoute leafTableRoute;

    @Value("${base-location-select-jrd-sql}")
    private String baseLocationSql;

    @Value("${region-select-jrd-sql}")
    private String regionSql;

    @Value("${contract-select-jrd-sql}")
    private String contractSql;

    @Value("${role-select-jrd-sql}")
    private String roleSql;

    @Value("${truncate-jrd-base-location}")
    private String locationTruncate;

    @Value("${truncate-jrd-region}")
    private String regionTruncate;

    @Value("${truncate-jrd-contract}")
    private String contractTruncate;

    @Value("${truncate-jrd-role}")
    private String roleTruncate;

    @Autowired
    ProducerTemplate producerTemplate;

    @Value("${start-leaf-route}")
    private String startLeafRoute;


    @BeforeClass
    public static void beforeAll() throws Exception {
        setSourcePath("classpath:archivalFiles", "archival.path");
        setSourcePath("classpath:sourceFiles", "active.path");
    }

    @Before
    public void init() {
        jdbcTemplate.execute(regionTruncate);
        jdbcTemplate.execute(locationTruncate);
        jdbcTemplate.execute(contractTruncate);
        jdbcTemplate.execute(roleTruncate);
        camelContext.getGlobalOptions().put(SCHEDULER_START_TIME, String.valueOf(new Date().getTime()));
    }

    @Test
    public void testAllLeafs() throws Exception {
        setSourceData(file);
        leafTableRoute.startRoute();
        producerTemplate.sendBody(startLeafRoute, "test JRD leaf");

        List<Map<String, Object>> judicialUserRoleType = jdbcTemplate.queryForList(roleSql);
        assertEquals(judicialUserRoleType.size(), 5);

        List<Map<String, Object>> judicialContractType = jdbcTemplate.queryForList(contractSql);
        assertEquals(judicialContractType.size(), 7);

        List<Map<String, Object>> judicialBaseLocationType = jdbcTemplate.queryForList(baseLocationSql);
        assertEquals(judicialBaseLocationType.size(), 5);

        List<Map<String, Object>> judicialRegionType = jdbcTemplate.queryForList(regionSql);
        assertEquals(judicialRegionType.size(), 5);
    }

    @Test
    public void testLeafFailuresRollbackAndKeepExistingState() throws Exception {

        setSourceData(file);
        leafTableRoute.startRoute();
        producerTemplate.sendBody(startLeafRoute, "test JRD leaf");

        setSourceData(file_error);
        camelContext.getGlobalOptions().put(MappingConstants.ORCHESTRATED_ROUTE, JUDICIAL_USER_PROFILE_ORCHESTRATION);
        producerTemplate.sendBody(startLeafRoute, "test JRD leaf");

        List<Map<String, Object>> judicialUserRoleType = jdbcTemplate.queryForList(roleSql);
        assertEquals(judicialUserRoleType.size(), 5);

        List<Map<String, Object>> judicialContractType = jdbcTemplate.queryForList(contractSql);
        assertEquals(judicialContractType.size(), 7);

        List<Map<String, Object>> judicialBaseLocationType = jdbcTemplate.queryForList(baseLocationSql);
        assertEquals(judicialBaseLocationType.size(), 5);

        List<Map<String, Object>> judicialRegionType = jdbcTemplate.queryForList(regionSql);
        assertEquals(judicialRegionType.size(), 5);
    }
}
