package uk.gov.hmcts.reform.juddata.camel.mapper;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.mapper.IMapper;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;

@Slf4j
@Component
public class JudicialUserProfileRowMapper implements IMapper {

    @Value("${logging-component-name}")
    private String logComponentName;

    public Map<String, Object> getMap(Object userProfile) {
        JudicialUserProfile judicialUserProfile = (JudicialUserProfile) userProfile;
        Map<String, Object> judUserProfileRow = new HashMap<>();
        judUserProfileRow.put("per_id", judicialUserProfile.getPerId());
        judUserProfileRow.put("personal_code", judicialUserProfile.getPersonalCode());
        judUserProfileRow.put("known_as", getKnownAsValue(judicialUserProfile));
        judUserProfileRow.put("surname", judicialUserProfile.getSurName());
        judUserProfileRow.put("full_name", judicialUserProfile.getFullName());
        judUserProfileRow.put("post_nominals", judicialUserProfile.getPostNominals());
        judUserProfileRow.put("work_pattern", judicialUserProfile.getWorkPattern());
        judUserProfileRow.put("ejudiciary_email", judicialUserProfile.getEjudiciaryEmail());
        judUserProfileRow.put("joining_date", judicialUserProfile.getJoiningDate());
        judUserProfileRow.put("last_working_date", judicialUserProfile.getLastWorkingDate());
        judUserProfileRow.put("active_flag", judicialUserProfile.isActiveFlag());
        judUserProfileRow.put("extracted_date", judicialUserProfile.getExtractedDate());
        judUserProfileRow.put("object_id", judicialUserProfile.getObjectId());
        judUserProfileRow.put("is_judge", judicialUserProfile.isJudge());
        judUserProfileRow.put("is_panel_member", judicialUserProfile.isPanelMember());
        judUserProfileRow.put("is_magistrate", judicialUserProfile.isMagistrate());

        Optional<String> mrdCreatedTimeOptional =
                Optional.ofNullable(judicialUserProfile.getMrdCreatedTime()).filter(Predicate.not(String::isEmpty));
        judUserProfileRow.put("mrd_created_time", mrdCreatedTimeOptional.map(Timestamp::valueOf).orElse(null));

        Optional<String> mrdUpdatedTimeOptional =
                Optional.ofNullable(judicialUserProfile.getMrdUpdatedTime()).filter(Predicate.not(String::isEmpty));
        judUserProfileRow.put("mrd_updated_time", mrdUpdatedTimeOptional.map(Timestamp::valueOf).orElse(null));

        Optional<String> mrdDeletedTimeOptional =
                Optional.ofNullable(judicialUserProfile.getMrdDeletedTime()).filter(Predicate.not(String::isEmpty));
        judUserProfileRow.put("mrd_deleted_time", mrdDeletedTimeOptional.map(Timestamp::valueOf).orElse(null));

        return  judUserProfileRow;
    }

    public String getKnownAsValue(JudicialUserProfile judicialUserProfile) {
        if (isBlank(judicialUserProfile.getKnownAs())) {
            log.warn("{} :: known_as field value is missing for per_id :: {}",
                    logComponentName, judicialUserProfile.getPerId());
            return judicialUserProfile.getFullName();
        }
        return judicialUserProfile.getKnownAs();
    }
}
