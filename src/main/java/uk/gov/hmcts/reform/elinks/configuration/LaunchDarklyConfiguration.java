package uk.gov.hmcts.reform.elinks.configuration;

import com.launchdarkly.sdk.server.LDClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.gov.hmcts.reform.elinks.util.FeatureConditionEvaluation;

@Configuration
public class LaunchDarklyConfiguration implements WebMvcConfigurer {

    @Bean
    public LDClient ldClient(@Value("${launchdarkly.sdk.key}") String sdkKey) {
        return new LDClient(sdkKey);
    }

    @Autowired
    private FeatureConditionEvaluation featureConditionEvaluation;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(featureConditionEvaluation);
              //  .addPathPatterns("/refdata/judicial/users/fetch") we need to edit here
              //  .addPathPatterns("/refdata/judicial/users/search")
              //   .addPathPatterns("/refdata/judicial/users/testing-support/sidam/actions/create-users")
              //   .addPathPatterns("/refdata/judicial/users");
    }
}