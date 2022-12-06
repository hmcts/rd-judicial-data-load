package uk.gov.hmcts.reform.juddata;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import uk.gov.hmcts.reform.idam.client.IdamApi;

@SpringBootApplication(scanBasePackages = "uk.gov.hmcts.reform")
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class
@Slf4j
@EnableFeignClients(basePackages = {
    "uk.gov.hmcts.reform.juddata"}, basePackageClasses = {IdamApi.class})
@EnableScheduling
public class JudicialApplication {


    public static void main(final String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(JudicialApplication.class);
    }

}
