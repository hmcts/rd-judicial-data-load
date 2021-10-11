package uk.gov.hmcts.reform.juddata.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.JsrValidatorInitializer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialBaseLocationType;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialRegionType;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserRoleType;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialBaseLocationRowTypeMapper;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialRegionTypeRowMapper;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialRoleTypeRowMapper;
import uk.gov.hmcts.reform.juddata.camel.processor.JudicialBaseLocationProcessor;
import uk.gov.hmcts.reform.juddata.camel.processor.JudicialRegionTypeProcessor;
import uk.gov.hmcts.reform.juddata.camel.processor.JudicialUserRoleTypeProcessor;


@Configuration
public class LeafCamelConfig {

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

    @Bean
    public JudicialUserRoleType judicialUserRoleType() {
        return new JudicialUserRoleType();
    }

    @Bean
    public JudicialUserRoleTypeProcessor judicialUserRoleTypeProcessor() {
        return new JudicialUserRoleTypeProcessor();
    }

    @Bean
    public JudicialRoleTypeRowMapper judicialRoleTypeRowMapper() {
        return new JudicialRoleTypeRowMapper();
    }

    @Bean
    JsrValidatorInitializer<JudicialUserRoleType> judicialUserRoleTypeJsrValidatorInitializer() {
        return new JsrValidatorInitializer<>();
    }

}
