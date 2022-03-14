package uk.gov.hmcts.reform.juddata.camel.mapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAuthorisation;
import uk.gov.hmcts.reform.juddata.camel.util.CommonUtils;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JudicialOfficeAuthorisationRowMapper {

    private int seqNumber = 0;

    public Map<String, Object> getMap(JudicialOfficeAuthorisation judicialOfficeAuthorisation) {

        Map<String, Object> judOfficeAuthorizationRow = new HashMap<>();

        judOfficeAuthorizationRow.put("judicial_office_auth_id", generateId());
        judOfficeAuthorizationRow.put("per_id", judicialOfficeAuthorisation.getPerId());
        judOfficeAuthorizationRow.put("jurisdiction", judicialOfficeAuthorisation.getJurisdiction());
        judOfficeAuthorizationRow.put("ticket_id", judicialOfficeAuthorisation.getTicketId());
        judOfficeAuthorizationRow.put("start_date", getDateTimeStamp(judicialOfficeAuthorisation.getStartDate()));
        judOfficeAuthorizationRow.put("end_date", getDateTimeStamp(judicialOfficeAuthorisation.getEndDate()));
        judOfficeAuthorizationRow.put("lower_level", judicialOfficeAuthorisation.getLowerLevel());
        judOfficeAuthorizationRow.put("personal_code", judicialOfficeAuthorisation.getPersonalCode());
        judOfficeAuthorizationRow.put("object_id", judicialOfficeAuthorisation.getObjectId());
        judOfficeAuthorizationRow.put("mrd_created_time", CommonUtils.getDateTimeStamp(judicialOfficeAuthorisation
            .getMrdCreatedTime()));
        judOfficeAuthorizationRow.put("mrd_updated_time", CommonUtils.getDateTimeStamp(judicialOfficeAuthorisation
            .getMrdUpdatedTime()));
        judOfficeAuthorizationRow.put("mrd_deleted_time", CommonUtils.getDateTimeStamp(judicialOfficeAuthorisation
            .getMrdDeletedTime()));
        return  judOfficeAuthorizationRow;
    }

    private Timestamp getDateTimeStamp(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        } else {
            return Timestamp.valueOf(date);
        }
    }

    public int generateId() {
        seqNumber = seqNumber + 1;
        return seqNumber;
    }

}