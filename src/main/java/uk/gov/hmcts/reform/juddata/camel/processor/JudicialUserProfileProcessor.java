package uk.gov.hmcts.reform.juddata.camel.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.processor.JsrValidationBaseProcessor;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.JsrValidatorInitializer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@Slf4j
@Component
public class JudicialUserProfileProcessor extends JsrValidationBaseProcessor<JudicialUserProfile> {

    @Autowired
    JsrValidatorInitializer<JudicialUserProfile> judicialUserProfileJsrValidatorInitializer;

    @Value("${logging-component-name}")
    private String logComponentName;

    private Set<String> validPerIdInUserProfile;

    @Autowired
    @Qualifier("springJdbcTemplate")
    protected JdbcTemplate jdbcTemplate;

    @Value("${fetch-personal-per-id}")
    String loadPerId;

    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) {

        List<JudicialUserProfile> judicialUserProfiles;

        judicialUserProfiles = (exchange.getIn().getBody() instanceof List)
            ? (List<JudicialUserProfile>) exchange.getIn().getBody()
            : singletonList((JudicialUserProfile) exchange.getIn().getBody());

        log.info("{}:: Judicial User Profile Records count before Validation {}::", logComponentName,
            judicialUserProfiles.size());

        List<JudicialUserProfile> filteredJudicialUserProfiles = validate(judicialUserProfileJsrValidatorInitializer,
            judicialUserProfiles);

        log.info("{}:: Judicial User Profile Records count after Validation {}::", logComponentName,
            filteredJudicialUserProfiles.size());

        audit(judicialUserProfileJsrValidatorInitializer, exchange);

        //Get Per Ids from current load
        validPerIdInUserProfile = filteredJudicialUserProfiles.stream()
            .map(JudicialUserProfile::getPerId).collect(toSet());

        //Get Per Id from previous loads
        validPerIdInUserProfile.addAll(loadPerId());

        exchange.getMessage().setBody(filteredJudicialUserProfiles);
    }

    public Set<String> getValidPerIdInUserProfile() {
        return isNotEmpty(validPerIdInUserProfile) ? validPerIdInUserProfile : emptySet();
    }

    @SuppressWarnings("unchecked")
    private List<String> loadPerId() {
        List<String> perIdList = jdbcTemplate.queryForList(loadPerId, String.class);
        return isNotEmpty(perIdList) ? perIdList : emptyList();
    }
}
