package uk.gov.hmcts.reform.elinks;

import lombok.extern.slf4j.Slf4j;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.elinks.util.FeatureToggleConditionExtension;
import uk.gov.hmcts.reform.elinks.util.ToggleEnable;
import uk.gov.hmcts.reform.lib.util.serenity5.SerenityTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SerenityTest
@SpringBootTest
@WithTags({@WithTag("testType:Functional")})
@Slf4j
class JudicialIdamElasticFunctionalTest extends AuthorizationFunctionalTest {

    public static final String IDAM_ELASTIC_SEARCH = "ELinksController.idamElasticSearch";

    @Test
    @ExtendWith(FeatureToggleConditionExtension.class)
    @ToggleEnable(mapKey = IDAM_ELASTIC_SEARCH, withFeature = false)
    void shouldGet403WhenApiToggledOff() {

        var errorResponse = judicialDataLoadApiClient.getIdamElasticSearch();
        assertNotNull(errorResponse);
        assertThat(errorResponse.getStatusCode()).isEqualTo(403);
    }
}
