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
public class CamelTransaction {

    @Autowired
    DataSource  dataSource;

    PlatformTransactionManager platformTransactionManager;

    @Bean(name = "txManager")
    public PlatformTransactionManager txManager() {
        platformTransactionManager = new  DataSourceTransactionManager(dataSource);
        return platformTransactionManager;
    }

    @Bean(name = "PROPAGATION_MANDATORY")
    public SpringTransactionPolicy getSpringTransactionPolicy() {

        SpringTransactionPolicy springTransactionPolicy=  new SpringTransactionPolicy();
        springTransactionPolicy.setTransactionManager(platformTransactionManager == null ? txManager() : platformTransactionManager);
        springTransactionPolicy.setPropagationBehaviorName("PROPAGATION_MANDATORY");
        return springTransactionPolicy;

    }

    @Bean(name = "PROPAGATION_REQUIRED")
    public SpringTransactionPolicy getSpringTransactionRequiredPolicy() {

        SpringTransactionPolicy springTransactionPolicy=  new SpringTransactionPolicy();
        springTransactionPolicy.setTransactionManager(platformTransactionManager == null ? txManager() : platformTransactionManager);
        springTransactionPolicy.setPropagationBehaviorName("PROPAGATION_REQUIRED");
        return springTransactionPolicy;

    }

}
