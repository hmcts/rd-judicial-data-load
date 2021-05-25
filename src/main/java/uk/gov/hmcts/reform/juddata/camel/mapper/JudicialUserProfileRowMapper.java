package uk.gov.hmcts.reform.juddata.camel.mapper;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.HashMap;
import java.util.Map;

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
        judUserProfileRow.put("title", judicialUserProfile.getTitle());
        judUserProfileRow.put("known_as", getKnownAsValue(judicialUserProfile));
        judUserProfileRow.put("surname", judicialUserProfile.getSurName());
        judUserProfileRow.put("full_name", judicialUserProfile.getFullName());
        judUserProfileRow.put("post_nominals", judicialUserProfile.getPostNominals());
        judUserProfileRow.put("contract_type", judicialUserProfile.getContractTypeId());
        judUserProfileRow.put("work_pattern", judicialUserProfile.getWorkPattern());
        judUserProfileRow.put("email_id", judicialUserProfile.getEmailId());
        judUserProfileRow.put("joining_date", judicialUserProfile.getJoiningDate());
        judUserProfileRow.put("last_working_date", judicialUserProfile.getLastWorkingDate());
        judUserProfileRow.put("active_flag", judicialUserProfile.isActiveFlag());
        judUserProfileRow.put("extracted_date", judicialUserProfile.getExtractedDate());
        judUserProfileRow.put("object_id", judicialUserProfile.getObjectId());
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
