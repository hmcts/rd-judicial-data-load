package uk.gov.hmcts.reform.juddata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableJpaAuditing
//@EnableJpaRepositories
@EnableRetry
@SpringBootApplication(scanBasePackages = "uk.gov.hmcts.reform.juddata")
@EnableCircuitBreaker
@EnableFeignClients(basePackages = {"uk.gov.hmcts.reform.juddata"})
@EnableScheduling
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class
public class JudicialApplication {

    public static void main(final String[] args) {
        SpringApplication.run(JudicialApplication.class, args);
    }
}
