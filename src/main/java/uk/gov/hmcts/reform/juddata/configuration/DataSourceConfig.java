package uk.gov.hmcts.reform.juddata.configuration;

import javax.sql.DataSource;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    @Autowired
    DataSource dataSource;

    @Bean(name = "txManager")
    public PlatformTransactionManager txManager() {
        PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(dataSource);
        return platformTransactionManager;
    }

    @Bean
    public SpringTransactionPolicy getSpringTransaction() {
        SpringTransactionPolicy springTransactionPolicy = new SpringTransactionPolicy();
        springTransactionPolicy.setTransactionManager(txManager());
        springTransactionPolicy.setPropagationBehaviorName("PROPAGATION_REQUIRED");
        return springTransactionPolicy;
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
