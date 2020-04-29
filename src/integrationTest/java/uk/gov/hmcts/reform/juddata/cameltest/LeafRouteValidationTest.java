package uk.gov.hmcts.reform.juddata.cameltest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.LEAF_ROUTE;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.IntegrationTestSupport.setSourcePath;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.LeafIntegrationTestSupport.file_error;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.LeafIntegrationTestSupport.file_jsr_error;
import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.LeafIntegrationTestSupport.setSourceData;

import java.util.List;
import java.util.Map;

import org.apache.camel.test.spring.CamelSpringRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.gov.hmcts.reform.juddata.config.LeafCamelConfig;
import uk.gov.hmcts.reform.juddata.config.ParentCamelConfig;

@TestPropertySource(properties = {"spring.config.location=classpath:application-integration.yml,classpath:application-leaf-integration.yml"})
@RunWith(CamelSpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@MockEndpoints("log:*")
@ContextConfiguration(classes = {ParentCamelConfig.class, LeafCamelConfig.class, CamelTestContextBootstrapper.class},
        initializers = ConfigFileApplicationContextInitializer.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = JpaRepositoriesAutoConfiguration.class)
@EnableTransactionManagement
@SqlConfig(dataSource = "dataSource", transactionManager = "txManager", transactionMode = SqlConfig.TransactionMode.ISOLATED)
public class LeafRouteValidationTest extends LeafRouteAbstractTest {

    @Value("${exception-select-query}")
    String exceptionQuery;

    @BeforeClass
    public static void beforeAll() throws Exception {
        setSourcePath("classpath:archivalFiles", "archival.path");
        setSourcePath("classpath:sourceFiles", "active.path");
    }

    @Before
    public void init() {
        dataLoadUtil.setGlobalConstant(camelContext, LEAF_ROUTE);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-leaf.sql", "/testData/truncate-exception.sql"})
    public void testLeafFailuresInvalidHeader() throws Exception {
        setSourceData(file_error);
        leafTableRoute.startRoute();
        producerTemplate.sendBody(startLeafRoute, "test JRD leaf");

        List<Map<String, Object>> judicialUserRoleType = jdbcTemplate.queryForList(roleSql);
        assertEquals(judicialUserRoleType.size(), 0);

        List<Map<String, Object>> judicialContractType = jdbcTemplate.queryForList(contractSql);
        assertEquals(judicialContractType.size(), 0);

        List<Map<String, Object>> judicialBaseLocationType = jdbcTemplate.queryForList(baseLocationSql);
        assertEquals(judicialBaseLocationType.size(), 0);

        List<Map<String, Object>> judicialRegionType = jdbcTemplate.queryForList(regionSql);
        assertEquals(judicialRegionType.size(), 0);

        List<Map<String, Object>> exceptionList = jdbcTemplate.queryForList(exceptionQuery);
        assertNotNull(exceptionList.get(0).get("file_name"));
        assertNotNull(exceptionList.get(0).get("scheduler_start_time"));
        assertNotNull(exceptionList.get(0).get("error_description"));
        assertNotNull(exceptionList.get(0).get("updated_timestamp"));
        assertEquals(exceptionList.size(), 1);
    }

    @Test
    @Sql(scripts = {"/testData/truncate-leaf.sql", "/testData/truncate-exception.sql"})
    public void testLeafFailuresInvalidJsr() throws Exception {

        setSourceData(file_jsr_error);
        leafTableRoute.startRoute();
        producerTemplate.sendBody(startLeafRoute, "test JRD leaf");

        List<Map<String, Object>> judicialUserRoleType = jdbcTemplate.queryForList(roleSql);
        assertEquals(judicialUserRoleType.size(), 3);
        assertEquals(judicialUserRoleType.get(0).get("role_id"), "1");
        assertEquals(judicialUserRoleType.get(1).get("role_id"), "3");
        assertEquals(judicialUserRoleType.get(2).get("role_id"), "7");

        assertEquals(judicialUserRoleType.get(0).get("role_desc_en"), "Magistrate");
        assertEquals(judicialUserRoleType.get(1).get("role_desc_en"), "Advisory Committee Member - Non Magistrate");
        assertEquals(judicialUserRoleType.get(2).get("role_desc_en"), "MAGS - AC Admin User");

        List<Map<String, Object>> judicialContractType = jdbcTemplate.queryForList(contractSql);
        assertEquals(judicialContractType.size(), 5);
        assertEquals(judicialContractType.get(0).get("contract_type_id"), "1");
        assertEquals(judicialContractType.get(1).get("contract_type_id"), "3");
        assertEquals(judicialContractType.get(2).get("contract_type_id"), "5");
        assertEquals(judicialContractType.get(3).get("contract_type_id"), "6");
        assertEquals(judicialContractType.get(4).get("contract_type_id"), "7");

        List<Map<String, Object>> judicialBaseLocationType = jdbcTemplate.queryForList(baseLocationSql);
        assertEquals(judicialBaseLocationType.get(0).get("base_location_id"), "1");
        assertEquals(judicialBaseLocationType.get(1).get("base_location_id"), "2");
        assertEquals(judicialBaseLocationType.get(2).get("base_location_id"), "5");
        assertEquals(judicialBaseLocationType.size(), 3);

        List<Map<String, Object>> judicialRegionType = jdbcTemplate.queryForList(regionSql);
        assertEquals(judicialRegionType.get(0).get("region_id"), "1");
        assertEquals(judicialRegionType.get(1).get("region_id"), "4");
        assertEquals(judicialRegionType.get(2).get("region_id"), "5");
        assertEquals(judicialRegionType.size(), 3);

        List<Map<String, Object>> exceptionList = jdbcTemplate.queryForList(exceptionQuery);
        for (int count = 0; count < 8; count++) {
            assertNotNull(exceptionList.get(count).get("table_name"));
            assertNotNull(exceptionList.get(count).get("scheduler_start_time"));
            assertNotNull(exceptionList.get(count).get("key"));
            assertNotNull(exceptionList.get(count).get("field_in_error"));
            assertNotNull(exceptionList.get(count).get("error_description"));
            assertNotNull(exceptionList.get(count).get("updated_timestamp"));
        }
        assertEquals(exceptionList.size(), 8);
    }
}
