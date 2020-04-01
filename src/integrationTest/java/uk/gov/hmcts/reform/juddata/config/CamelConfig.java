package uk.gov.hmcts.reform.juddata.config;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.containers.PostgreSQLContainer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialOfficeAppointmentRowMapper;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialUserProfileRowMapper;
import uk.gov.hmcts.reform.juddata.camel.processor.*;
import uk.gov.hmcts.reform.juddata.camel.route.ParentOrchestrationRoute;
import uk.gov.hmcts.reform.juddata.camel.util.JrdUtility;

import javax.sql.DataSource;

import static org.mockito.Mockito.mock;

@Configuration
public class CamelConfig {

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    ParentOrchestrationRoute parentRoute() {
        return new ParentOrchestrationRoute();
    }

    @Bean
    JudicialUserProfileProcessor judicialUserProfileProcessor() {
        return new JudicialUserProfileProcessor();
    }

    @Bean
    JudicialUserProfileRowMapper judicialUserProfileRowMapper() {
        return new JudicialUserProfileRowMapper();
    }

    @Bean
    JudicialUserProfile judicialUserProfile() {
        return new JudicialUserProfile();
    }

    @Bean
    FileReadProcessor fileReadProcessor() {
        return new FileReadProcessor();
    }

    @Bean
    ArchiveAzureFileProcessor azureFileProcessor() {
        return mock(ArchiveAzureFileProcessor.class);
    }


    @Bean
    JrdUtility jrdUtility() { return new JrdUtility(); }

    @Bean
    JdbcTemplate jdbcTemplate() { return new JdbcTemplate(this.dataSource()); }

    @Bean
    SchedulerAuditProcessor schedulerAuditProcessor() { return new SchedulerAuditProcessor(); }



    private static final PostgreSQLContainer testPostgres = new PostgreSQLContainer("postgres")
            .withDatabaseName("dbjuddata_test");

    static {
        testPostgres.start();
    }

    @Bean
    public DataSource dataSource() {
        final PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setUrl(testPostgres.getJdbcUrl());
        ds.setUser(testPostgres.getUsername());
        ds.setPassword(testPostgres.getPassword());
        return ds;
    }

    @Bean(name = "txManager")
    public PlatformTransactionManager txManager() {
        PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(dataSource());
        return platformTransactionManager;
    }

    @Bean
    public SpringTransactionPolicy getSpringTransaction() {
        SpringTransactionPolicy springTransactionPolicy = new SpringTransactionPolicy();
        springTransactionPolicy.setTransactionManager(txManager());
        springTransactionPolicy.setPropagationBehaviorName("PROPAGATION_REQUIRED");
        return springTransactionPolicy;
    }

    @Bean
    public JudicialOfficeAppointmentProcessor judicialOfficeAppointmentProcessor() {
        return new JudicialOfficeAppointmentProcessor();
    }

    @Bean
    public JudicialOfficeAppointmentRowMapper judicialOfficeAppointmentRowMapper() {
        return new JudicialOfficeAppointmentRowMapper();
    }

    @Bean
    public JudicialOfficeAppointment judicialOfficeAppointment() {
        return new JudicialOfficeAppointment();
    }

    @Bean
    public ExceptionProcessor exceptionProcessor() {
        return new ExceptionProcessor();
    }

    @Bean
    public CamelContext camelContext() {
        CamelContext camelContext = new SpringCamelContext(applicationContext);
        return camelContext;
    }

}
