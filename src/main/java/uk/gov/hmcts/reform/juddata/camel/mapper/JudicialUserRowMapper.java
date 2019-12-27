package uk.gov.hmcts.reform.juddata.camel.mapper;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialUserProfile;

@Slf4j
@Component
public class JudicialUserRowMapper {


    public Map<String, Object> getMap(JudicialUserProfile userProfile) {

        Map<String, Object> judUserProfileRow = new HashMap<>();
        judUserProfileRow.put("elinks_id", userProfile.getElinks_Id());
        judUserProfileRow.put("personal_code", userProfile.getPersonal_Code());
        judUserProfileRow.put("title", userProfile.getTitle());
        judUserProfileRow.put("known_as", userProfile.getKnown_As());
        judUserProfileRow.put("surname", userProfile.getSurName());
        judUserProfileRow.put("full_name", userProfile.getFullName());
        judUserProfileRow.put("post_nominals", userProfile.getPost_Nominals());
        judUserProfileRow.put("contract_type", userProfile.getContract_Type_Id());
        judUserProfileRow.put("work_pattern", userProfile.getWork_Pattern());
        judUserProfileRow.put("email_id", userProfile.getEmail_Id());
        judUserProfileRow.put("joining_date", userProfile.getJoining_Date());
        judUserProfileRow.put("last_working_date", userProfile.getLastWorking_Date());
        judUserProfileRow.put("active_flag", userProfile.isActive_Flag());
        judUserProfileRow.put("extracted_date", getDateTimeStamp(userProfile.getExtractedDate()));

        log.info("timestamp Date:: " + judUserProfileRow.get("extracted_date"));
        return  judUserProfileRow;
    }

    private Date getDateTime(String date) {

        SimpleDateFormat newPattern = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date datetime = null;
        try {
            datetime = newPattern.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  datetime;
    }

    private Timestamp getDateTimeStamp(String date) {

        return Timestamp.valueOf(date);
    }


}
