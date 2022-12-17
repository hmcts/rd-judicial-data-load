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

    @Bean(name = "eLinksLdClient")
    public LDClient ldClient(@Value("${launchdarkly.sdk.key}") String sdkKey) {
        return new LDClient(sdkKey);
    }

    @Autowired
    private FeatureConditionEvaluation featureConditionEvaluation;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(featureConditionEvaluation)
                .addPathPatterns("/refdata/jinternal/elink/reference_data/base_location")
                .addPathPatterns("/refdata/internal/elink/reference_data/location")
                .addPathPatterns("/refdata/jinternal/elink/people")
                .addPathPatterns("/refdata/jinternal/elink/leavers")
                .addPathPatterns("/refdata/jinternal/elink/idam/elastic/search")
                .addPathPatterns("/refdata/jinternal/elink/sidam/asb/publish");

    }
}