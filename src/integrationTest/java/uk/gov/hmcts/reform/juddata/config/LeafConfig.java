package uk.gov.hmcts.reform.juddata.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.JsrValidatorInitializer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialBaseLocationType;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialRegionType;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialBaseLocationRowTypeMapper;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialRegionTypeRowMapper;
import uk.gov.hmcts.reform.juddata.camel.processor.JudicialBaseLocationProcessor;
import uk.gov.hmcts.reform.juddata.camel.processor.JudicialRegionTypeProcessor;


@Configuration
public class LeafConfig {

    @Bean
    public JudicialBaseLocationProcessor judicialBaseLocationProcessor() {
        return new JudicialBaseLocationProcessor();
    }

    @Bean
    public JudicialBaseLocationRowTypeMapper judicialBaseLocationRowTypeMapper() {
        return new JudicialBaseLocationRowTypeMapper();
    }

    @Bean
    public JudicialBaseLocationType judicialBaseLocationType() {
        return new JudicialBaseLocationType();
    }

    @Bean
    public JudicialRegionType judicialRegionType() {
        return new JudicialRegionType();
    }

    @Bean
    public JudicialRegionTypeProcessor judicialRegionTypeProcessor() {
        return new JudicialRegionTypeProcessor();
    }

    @Bean
    public JudicialRegionTypeRowMapper judicialRegionTypeRowMapper() {
        return new JudicialRegionTypeRowMapper();
    }

    @Bean
    JsrValidatorInitializer<JudicialBaseLocationType> judicialBaseLocationTypeJsrValidatorInitializer() {
        return new JsrValidatorInitializer<>();
    }

    @Bean
    JsrValidatorInitializer<JudicialRegionType> judicialRegionTypeJsrValidatorInitializer() {
        return new JsrValidatorInitializer<>();
    }
}
