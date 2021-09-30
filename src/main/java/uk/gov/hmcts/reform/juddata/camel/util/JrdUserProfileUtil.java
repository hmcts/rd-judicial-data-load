package uk.gov.hmcts.reform.juddata.camel.util;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;


@Component
public class JrdUserProfileUtil {


    private List<JudicialUserProfile> userProfilesWithInvalidObjectIds;
    private List<JudicialUserProfile> userProfilesWithInvalidPersonalCodes;

    public List<JudicialUserProfile> removeInvalidRecords(List<JudicialUserProfile> judicialUserProfiles) {

        List<JudicialUserProfile> userProfiles = new ArrayList<>(judicialUserProfiles);

        //get list of user profiles with misconfigured object id
        userProfilesWithInvalidObjectIds = filterByObjectId(userProfiles);

        //get list of user profiles with misconfigured personal code
        userProfilesWithInvalidPersonalCodes = filterByPersonalCode(userProfiles);

        //remove the misconfigured entries from the original user profile list
        remove(getInvalidRecords(), userProfiles);

        return List.copyOf(userProfiles);

    }

    public List<JudicialUserProfile> filterByObjectId(List<JudicialUserProfile> userProfiles) {
        List<JudicialUserProfile> filteredList = new ArrayList<>();
        //filter by object ids
        Map<String, List<JudicialUserProfile>> filteredMap = filterAndGroupByObjectId(userProfiles);
        filteredMap.forEach((k, v) -> filteredList.addAll(v));

        return List.copyOf(filteredList);
    }

    public List<JudicialUserProfile> filterByPersonalCode(List<JudicialUserProfile> userProfiles) {
        List<JudicialUserProfile> filteredList = new ArrayList<>();
        //filter by personal codes
        Map<String, List<JudicialUserProfile>> filteredMap = filterAndGroupByPersonalCode(userProfiles);
        filteredMap.forEach((k, v) -> filteredList.addAll(v));

        return List.copyOf(filteredList);
    }

    /**
     * Iterate through the list of user profiles and group them by object id,
     * filter out the entries where for each object id, the number of distinct personal codes are less than 2.
     * @param userProfiles original list of user profiles
     * @return map containing only the object ids where the associated personal codes are misconfigured
     */
    public Map<String, List<JudicialUserProfile>> filterAndGroupByObjectId(
            List<JudicialUserProfile> userProfiles) {

        return userProfiles.stream()
                .collect(collectingAndThen(groupingBy(JudicialUserProfile::getObjectId), map -> {
                    map.values().removeIf(l -> l.stream()
                            .map(JudicialUserProfile::getPersonalCode)
                            .distinct().count() < 2);
                    return map; }));
    }


    /**
     * Iterate through the list of user profiles and group them by personal code,
     * filter out the entries where for each personal code, the number of distinct object ids are less than 2.
     * @param userProfiles original list of user profiles
     * @return map containing only the personal codes where the associated object ids are misconfigured
     */
    public Map<String, List<JudicialUserProfile>> filterAndGroupByPersonalCode(
            List<JudicialUserProfile> userProfiles) {

        return userProfiles.stream()
                .collect(collectingAndThen(groupingBy(JudicialUserProfile::getPersonalCode), map -> {
                    map.values().removeIf(l -> l.stream()
                            .map(JudicialUserProfile::getObjectId)
                            .distinct().count() < 2);
                    return map; }));
    }

    public List<JudicialUserProfile> getInvalidRecords() {
        List<JudicialUserProfile> invalidRecordsList = new ArrayList<>();
        Stream.of(userProfilesWithInvalidObjectIds, userProfilesWithInvalidPersonalCodes)
                .forEach(invalidRecordsList::addAll);

        return List.copyOf(invalidRecordsList);
    }

    public void remove(List<JudicialUserProfile> userProfilesToBeDeleted, List<JudicialUserProfile> userProfiles) {
        Set<JudicialUserProfile> erroneousUserProfileSet = new HashSet<>(userProfilesToBeDeleted);
        userProfiles.removeIf(erroneousUserProfileSet::contains);
    }

}
