package uk.gov.hmcts.reform.juddata.configuration;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    String url;

    @Value("${spring.datasource.username}")
    String userName;

    @Value("${spring.datasource.password}")
    String password;

    @Value("${spring.datasource.min-idle}")
    int idleConnections;

    @Value("${spring.datasource.max-timout}")
    int maxTimeOut;

    @Value("${spring.datasource.idle-timeout}")
    int idleTimeOut;

    @Value("${spring.datasource.maximum-pool-size}")
    int maxPoolSize;

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url(url);
        dataSourceBuilder.username(userName);
        dataSourceBuilder.password(password);
        HikariDataSource dataSource = (HikariDataSource) dataSourceBuilder.build();
        dataSource.setMinimumIdle(idleConnections);
        dataSource.setIdleTimeout(idleTimeOut);
        dataSource.setMaxLifetime(maxTimeOut);
        dataSource.setMaximumPoolSize(maxPoolSize);
        return dataSource;
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
    public DefaultTransactionDefinition defaultTransactionDefinition() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(Propagation.REQUIRED.ordinal());
        return def;
    }

    //Aggregation configuration
    //    @Bean
    //    public JdbcAggregationRepository getJdbcAggregationRepository() {
    //        JdbcAggregationRepository jdbcAggregationRepository = new PostgresAggregationRepository();
    //        jdbcAggregationRepository.setRepositoryName("aggregationRepo");
    //        jdbcAggregationRepository.setTransactionManager(txManager());
    //        jdbcAggregationRepository.setDataSource(dataSource);
    //        jdbcAggregationRepository.setStoreBodyAsText(true);
    //        jdbcAggregationRepository.setPropagationBehavior(PROPAGATION_NESTED);
    //        return  jdbcAggregationRepository;
    //    }

}
