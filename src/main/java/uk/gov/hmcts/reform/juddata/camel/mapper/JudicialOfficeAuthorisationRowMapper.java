package uk.gov.hmcts.reform.juddata.camel.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAuthorisation;

import java.util.HashMap;
import java.util.Map;

import static uk.gov.hmcts.reform.juddata.camel.util.CommonUtils.getDateTimeStamp;

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
        judOfficeAuthorizationRow.put("mrd_created_time", getDateTimeStamp(judicialOfficeAuthorisation
            .getMrdCreatedTime()));
        judOfficeAuthorizationRow.put("mrd_updated_time", getDateTimeStamp(judicialOfficeAuthorisation
            .getMrdUpdatedTime()));
        judOfficeAuthorizationRow.put("mrd_deleted_time", getDateTimeStamp(judicialOfficeAuthorisation
            .getMrdDeletedTime()));
        return  judOfficeAuthorizationRow;
    }

    public int generateId() {
        seqNumber = seqNumber + 1;
        return seqNumber;
    }

}