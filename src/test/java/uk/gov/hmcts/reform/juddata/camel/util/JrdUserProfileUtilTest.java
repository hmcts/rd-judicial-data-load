package uk.gov.hmcts.reform.juddata.camel.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JrdUserProfileUtilTest {

    @InjectMocks
    JrdUserProfileUtil jrdUserProfileFilter;

    List<JudicialUserProfile> judicialUserProfiles;
    List<JudicialUserProfile> judicialUserProfilesValidRecords;
    List<JudicialUserProfile> judicialUserProfilesInvalidObjectIds;
    List<JudicialUserProfile> judicialUserProfilesInvalidPersonalCodes;

    @BeforeEach
    public void createUserProfiles() {
        judicialUserProfiles = new ArrayList<>();
        judicialUserProfilesValidRecords = new ArrayList<>(Arrays.asList(
                 JudicialUserProfile.builder().personalCode("1234567").objectId("673sg825-7c34-5456-aa54-e126394793056")
                         .build(),
                 JudicialUserProfile.builder().personalCode("1234567").objectId("673sg825-7c34-5456-aa54-e126394793056")
                         .build(),
                 JudicialUserProfile.builder().personalCode("2034512").objectId("6427c339-79bd-47a7-b000-2356cb77c13b")
                         .build()
         ));
        judicialUserProfilesInvalidObjectIds = new ArrayList<>(Arrays.asList(
                JudicialUserProfile.builder().personalCode("16023").objectId("0d3b6f60-40b5-45d6-b526-d38a212992d9")
                        .build(),
                JudicialUserProfile.builder().personalCode("16023").objectId("0d3b6f60-40b5-45d6-b526-d38a212992d9")
                        .build(),
                JudicialUserProfile.builder().personalCode("4925916").objectId("0d3b6f60-40b5-45d6-b526-d38a212992d9")
                        .build(),
                JudicialUserProfile.builder().personalCode("4925916").objectId("0d3b6f60-40b5-45d6-b526-d38a212992d9")
                        .build()
        ));
        judicialUserProfilesInvalidPersonalCodes = new ArrayList<>(Arrays.asList(
                JudicialUserProfile.builder().personalCode("4927112").objectId("a01009ed-e6d1-47a3-add6-adf4365ca397")
                        .build(),
                JudicialUserProfile.builder().personalCode("4927112").objectId("bd30e3c4-c377-4da4-8eb5-d7b1ff71a4ba")
                        .build(),
                JudicialUserProfile.builder().personalCode("4927112").objectId("933fc903-4c39-4742-bb46-d69903835904")
                        .build()
        ));
    }

    @Test
    void test_filter_and_remove() {
        judicialUserProfiles.addAll(judicialUserProfilesValidRecords);
        judicialUserProfiles.addAll(judicialUserProfilesInvalidObjectIds);
        judicialUserProfiles.addAll(judicialUserProfilesInvalidPersonalCodes);

        List<JudicialUserProfile> resultList = jrdUserProfileFilter.removeInvalidRecords(judicialUserProfiles);
        assertThat(resultList).isNotNull().hasSize(3).isEqualTo(judicialUserProfilesValidRecords);
    }

    @Test
    void test_filter_and_remove_when_all_valid_profiles() {
        judicialUserProfiles.addAll(judicialUserProfilesValidRecords);

        List<JudicialUserProfile> resultList = jrdUserProfileFilter.removeInvalidRecords(judicialUserProfiles);
        assertThat(resultList).isNotNull().hasSize(3).isEqualTo(judicialUserProfilesValidRecords);
    }

    @Test
    void test_filter_and_remove_when_all_invalid_profiles() {
        judicialUserProfiles.addAll(judicialUserProfilesInvalidObjectIds);
        judicialUserProfiles.addAll(judicialUserProfilesInvalidPersonalCodes);

        List<JudicialUserProfile> resultList = jrdUserProfileFilter.removeInvalidRecords(judicialUserProfiles);
        assertThat(resultList).isEmpty();
    }

    @Test
    void test_filter_by_object_id() {
        judicialUserProfiles.addAll(judicialUserProfilesInvalidObjectIds);
        judicialUserProfiles.addAll(judicialUserProfilesValidRecords);

        List<JudicialUserProfile> profiles = jrdUserProfileFilter.filterByObjectId(judicialUserProfiles);
        assertThat(profiles).isNotNull().hasSize(4).isEqualTo(judicialUserProfilesInvalidObjectIds);
    }

    @Test
    void test_filter_by_personal_code() {
        judicialUserProfiles.addAll(judicialUserProfilesInvalidPersonalCodes);
        judicialUserProfiles.addAll(judicialUserProfilesValidRecords);

        List<JudicialUserProfile> profiles = jrdUserProfileFilter.filterByPersonalCode(judicialUserProfiles);
        assertThat(profiles).isNotNull().hasSize(3).isEqualTo(judicialUserProfilesInvalidPersonalCodes);
    }

    @Test
    void test_remove() {
        judicialUserProfiles.addAll(judicialUserProfilesInvalidPersonalCodes);

        jrdUserProfileFilter.remove(judicialUserProfilesInvalidPersonalCodes, judicialUserProfiles);
        assertThat(judicialUserProfiles).isNotNull().isEmpty();
    }

}
