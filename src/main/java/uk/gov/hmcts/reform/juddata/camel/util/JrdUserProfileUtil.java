package uk.gov.hmcts.reform.juddata.camel.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
public class JrdUserProfileUtil {

    @Autowired
    @Qualifier("springJdbcTemplate")
    protected JdbcTemplate jdbcTemplate;

    public void filterRemoveAndAudit(List<JudicialUserProfile> judicialUserProfiles) {

        //get list of user profiles with wrong object id
        List<JudicialUserProfile> userProfilesfiltered1 = filterByObjectId(judicialUserProfiles);

        //get list of user profiles with wrong personal code
        List<JudicialUserProfile> userProfilesfiltered2 = filterByPersonalCode(judicialUserProfiles);

        //merge erroneous lists together
        List<JudicialUserProfile> combinedFilteredList = new ArrayList<>();
        Stream.of(userProfilesfiltered1, userProfilesfiltered2).forEach(combinedFilteredList::addAll);

        //audit
        audit();

        //remove the erroneous entries from the original user profile list
        remove(combinedFilteredList, judicialUserProfiles);

    }

    public List<JudicialUserProfile> filterByObjectId(List<JudicialUserProfile> userProfiles) {
        //filter by object ids
        List<JudicialUserProfile> filteredList = new ArrayList<>();
        Map<String, List<JudicialUserProfile>> filteredMap = filterAndGroupByObjectId(userProfiles);
        filteredMap.forEach((k, v) -> filteredList.addAll(v));

        return filteredList;
    }

    public List<JudicialUserProfile> filterByPersonalCode(List<JudicialUserProfile> userProfiles) {
        //filter by personal codes
        List<JudicialUserProfile> filteredList = new ArrayList<>();
        Map<String, List<JudicialUserProfile>> filteredMap = filterAndGroupByPersonalCode(userProfiles);
        filteredMap.forEach((k, v) -> filteredList.addAll(v));

        return filteredList;
    }

    public Map<String, List<JudicialUserProfile>> filterAndGroupByObjectId(
            List<JudicialUserProfile> userProfiles) {

        return userProfiles.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(JudicialUserProfile::getObjectId), map -> {
                            map.values().removeIf(
                                    l -> l.stream().map(JudicialUserProfile::getPersonalCode).distinct().count() < 2);
                            return map; }));
    }

    public Map<String, List<JudicialUserProfile>> filterAndGroupByPersonalCode(
            List<JudicialUserProfile> userProfiles) {

        return userProfiles.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(JudicialUserProfile::getPersonalCode), map -> {
                            map.values().removeIf(
                                    l -> l.stream().map(JudicialUserProfile::getObjectId).distinct().count() < 2);
                        return map; }));
    }

    public void remove(List<JudicialUserProfile> userProfilesToBeDeleted, List<JudicialUserProfile> userProfiles) {
        Set<JudicialUserProfile> erroneousUserProfileSet = new HashSet<>(userProfilesToBeDeleted);
        userProfiles.removeIf(erroneousUserProfileSet::contains);
    }

    public void audit() {
        // TODO: jdbcTemplate.update()
    }

}
