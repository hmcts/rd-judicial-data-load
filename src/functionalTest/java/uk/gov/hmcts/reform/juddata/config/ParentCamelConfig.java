package uk.gov.hmcts.reform.juddata.config;

import com.azure.core.amqp.AmqpRetryOptions;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.launchdarkly.sdk.server.LDClient;
import org.apache.camel.CamelContext;
import org.apache.camel.component.bean.validator.HibernateValidationProviderResolver;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorFactoryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.containers.PostgreSQLContainer;
import uk.gov.hmcts.reform.data.ingestion.DataIngestionLibraryRunner;
import uk.gov.hmcts.reform.data.ingestion.camel.processor.ArchiveFileProcessor;
import uk.gov.hmcts.reform.data.ingestion.camel.processor.ExceptionProcessor;
import uk.gov.hmcts.reform.data.ingestion.camel.processor.FileReadProcessor;
import uk.gov.hmcts.reform.data.ingestion.camel.processor.FileResponseProcessor;
import uk.gov.hmcts.reform.data.ingestion.camel.processor.HeaderValidationProcessor;
import uk.gov.hmcts.reform.data.ingestion.camel.route.ArchivalRoute;
import uk.gov.hmcts.reform.data.ingestion.camel.route.DataLoadRoute;
import uk.gov.hmcts.reform.data.ingestion.camel.service.ArchivalBlobServiceImpl;
import uk.gov.hmcts.reform.data.ingestion.camel.service.AuditServiceImpl;
import uk.gov.hmcts.reform.data.ingestion.camel.service.EmailServiceImpl;
import uk.gov.hmcts.reform.data.ingestion.camel.service.IEmailService;
import uk.gov.hmcts.reform.data.ingestion.camel.util.DataLoadUtil;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.JsrValidatorInitializer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAuthorisation;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.listener.JobResultListener;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialOfficeAppointmentRowMapper;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialOfficeAuthorisationRowMapper;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialUserProfileRowMapper;
import uk.gov.hmcts.reform.juddata.camel.processor.JudicialOfficeAppointmentProcessor;
import uk.gov.hmcts.reform.juddata.camel.processor.JudicialOfficeAuthorisationProcessor;
import uk.gov.hmcts.reform.juddata.camel.processor.JudicialUserProfileProcessor;
import uk.gov.hmcts.reform.juddata.camel.servicebus.TopicPublisher;
import uk.gov.hmcts.reform.juddata.camel.task.LeafRouteTask;
import uk.gov.hmcts.reform.juddata.camel.task.ParentRouteTask;
import uk.gov.hmcts.reform.juddata.camel.util.FeatureToggleService;
import uk.gov.hmcts.reform.juddata.camel.util.FeatureToggleServiceImpl;
import uk.gov.hmcts.reform.juddata.camel.util.JrdDataIngestionLibraryRunner;
import uk.gov.hmcts.reform.juddata.camel.util.JrdExecutor;
import uk.gov.hmcts.reform.juddata.cameltest.testsupport.JrdBlobSupport;

import javax.sql.DataSource;

import static java.lang.System.getenv;
import static java.util.Objects.nonNull;
import static org.mockito.Mockito.mock;


@Configuration
public class ParentCamelConfig {

    @Value("${launchdarkly.sdk.environment:''}")
    private String environment;


    @Value("${jrd.publisher.azure.service.bus.topic:''}")
    String topic;

    @Value("${jrd.publisher.azure.service.bus.connection-string:''}")
    String accessConnectionString;

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
    public JsrValidatorInitializer<JudicialUserProfile> judicialUserProfileJsrValidatorInitializer() {
        return new JsrValidatorInitializer<>();
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
    public JsrValidatorInitializer<JudicialOfficeAppointment> judicialOfficeAppointmentJsrValidatorInitializer() {
        return new JsrValidatorInitializer<>();
    }

    @Bean
    JudicialOfficeAuthorisation judicialOfficeAuthorisation() {
        return new JudicialOfficeAuthorisation();
    }

    @Bean
    JudicialOfficeAuthorisationProcessor judicialOfficeAuthorisationProcessor() {
        return new JudicialOfficeAuthorisationProcessor();
    }

    @Bean
    JudicialOfficeAuthorisationRowMapper judicialOfficeAuthorisationRowMapper() {
        return new JudicialOfficeAuthorisationRowMapper();
    }

    @Bean
    public JsrValidatorInitializer<JudicialOfficeAuthorisation> judicialOfficeAuthorisationJsrValidatorInitializer() {
        return new JsrValidatorInitializer<>();
    }

    // Route configuration ends

    // processor configuration starts
    @Bean
    FileReadProcessor fileReadProcessor() {
        return new FileReadProcessor();
    }


    @Bean
    ArchiveFileProcessor azureFileProcessor() {
        return new ArchiveFileProcessor();
    }

    @Bean
    public ExceptionProcessor exceptionProcessor() {
        return new ExceptionProcessor();
    }

    @Bean
    public AuditServiceImpl schedulerAuditProcessor() {
        return new AuditServiceImpl();
    }

    @Bean
    public HeaderValidationProcessor headerValidationProcessor() {
        return new HeaderValidationProcessor();
    }
    // processor configuration starts


    // db configuration starts
    private static final PostgreSQLContainer testPostgres = new PostgreSQLContainer("postgres")
        .withDatabaseName("dbjuddata_test");

    static {
        testPostgres.start();
    }

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = getDataSourceBuilder();
        return dataSourceBuilder.build();
    }

    @Bean("springJdbcDataSource")
    public DataSource springJdbcDataSource() {
        DataSourceBuilder dataSourceBuilder = getDataSourceBuilder();
        return dataSourceBuilder.build();
    }

    public DataSourceBuilder getDataSourceBuilder() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url(testPostgres.getJdbcUrl());
        dataSourceBuilder.username(testPostgres.getUsername());
        dataSourceBuilder.password(testPostgres.getPassword());
        return dataSourceBuilder;
    }


    @Bean("springJdbcTemplate")
    public JdbcTemplate springJdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(springJdbcDataSource());
        return jdbcTemplate;
    }
    // db configuration ends

    // transaction configuration starts
    @Bean(name = "txManager")
    public PlatformTransactionManager txManager() {
        DataSourceTransactionManager platformTransactionManager = new DataSourceTransactionManager(dataSource());
        platformTransactionManager.setDataSource(dataSource());
        return platformTransactionManager;
    }

    @Bean(name = "springJdbcTransactionManager")
    public PlatformTransactionManager springJdbcTransactionManager() {
        DataSourceTransactionManager platformTransactionManager
            = new DataSourceTransactionManager(springJdbcDataSource());
        platformTransactionManager.setDataSource(springJdbcDataSource());
        return platformTransactionManager;
    }

    @Bean(name = "PROPAGATION_REQUIRED")
    public SpringTransactionPolicy getSpringTransaction() {
        SpringTransactionPolicy springTransactionPolicy = new SpringTransactionPolicy();
        springTransactionPolicy.setTransactionManager(txManager());
        springTransactionPolicy.setPropagationBehaviorName("PROPAGATION_REQUIRED");
        return springTransactionPolicy;
    }

    @Bean(name = "PROPAGATION_REQUIRES_NEW")
    public SpringTransactionPolicy propagationRequiresNew() {
        SpringTransactionPolicy springTransactionPolicy = new SpringTransactionPolicy();
        springTransactionPolicy.setTransactionManager(txManager());
        springTransactionPolicy.setPropagationBehaviorName("PROPAGATION_REQUIRES_NEW");
        return springTransactionPolicy;
    }
    // transaction configuration ends

    // tasks configuration starts
    @Bean
    ParentRouteTask parentRouteTask() {
        return new ParentRouteTask();
    }

    @Bean
    LeafRouteTask leafRouteTask() {
        return new LeafRouteTask();
    }

    @Bean
    JrdExecutor jrdTask() {
        return new JrdExecutor();
    }
    // tasks configuration ends

    // camel related configuration starts
    @Bean
    DataLoadRoute parentRoute() {
        return new DataLoadRoute();
    }

    @Bean
    ArchivalRoute archivalRoute() {
        return new ArchivalRoute();
    }

    @Bean
    public CamelContext camelContext(ApplicationContext applicationContext) {
        return new SpringCamelContext(applicationContext);
    }

    @Bean
    JrdBlobSupport integrationTestSupport() {
        return new JrdBlobSupport();
    }
    // camel related configuration ends

    // miscellaneous configuration starts
    @Bean("myValidationProviderResolver")
    HibernateValidationProviderResolver hibernateValidationProviderResolver() {
        return new HibernateValidationProviderResolver();
    }

    @Bean("myConstraintValidatorFactory")
    public ConstraintValidatorFactoryImpl constraintValidatorFactory() {
        return new ConstraintValidatorFactoryImpl();
    }

    @Bean
    public DataLoadUtil dataLoadUtil() {
        return new DataLoadUtil();
    }

    @Bean
    IEmailService emailService() {
        return mock(EmailServiceImpl.class);
    }

    @Bean
    JobResultListener jobResultListener() {
        return new JobResultListener();
    }

    @Bean
    FileResponseProcessor fileResponseProcessor() {
        return new FileResponseProcessor();
    }

    @Bean
    ArchivalBlobServiceImpl archivalBlobService() {
        return new ArchivalBlobServiceImpl();
    }

    @Bean
    DataIngestionLibraryRunner jrdDataIngestionLibraryRunner() {
        return new JrdDataIngestionLibraryRunner();
    }

    @Bean
    TopicPublisher topicPublisher() {
        if (nonNull(environment) && environment.startsWith("preview")) {
            return new TopicPublisher();
        }
        return mock(TopicPublisher.class);
    }

    @Bean
    public ServiceBusSenderClient getServiceBusSenderClient() {
        if (nonNull(environment) && environment.startsWith("preview")) {
            return new ServiceBusClientBuilder()
                .connectionString(accessConnectionString)
                .retryOptions(new AmqpRetryOptions())
                .sender()
                .topicName(topic)
                .buildClient();
        }

        return mock(ServiceBusSenderClient.class);
    }

    @Bean
    LDClient ldClient() {
        return new LDClient((getenv("RD_LD_SDK_KEY")));
    }

    @Bean
    FeatureToggleService featureToggleService() {
        return new FeatureToggleServiceImpl(ldClient(), "rd");
    }

    // miscellaneous configuration ends
    // miscellaneous configuration ends
}
