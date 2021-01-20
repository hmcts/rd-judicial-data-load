package uk.gov.hmcts.reform.juddata.camel.mapper;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAuthorisation;

@Slf4j
@Component
public class JudicialOfficeAuthorisationRowMapper {

    private int seqNumber = 0;

    public Map<String, Object> getMap(JudicialOfficeAuthorisation judicialOfficeAuthorisation) {

        Map<String, Object> judOfficeAppointmentRow = new HashMap<>();

        judOfficeAppointmentRow.put("judicial_office_auth_id", generateId());
        judOfficeAppointmentRow.put("elinks_id", judicialOfficeAuthorisation.getElinksId());
        judOfficeAppointmentRow.put("jurisdiction", judicialOfficeAuthorisation.getJurisdiction());
        judOfficeAppointmentRow.put("ticket_id", judicialOfficeAuthorisation.getTicketId());
        judOfficeAppointmentRow.put("start_date", getDateTimeStamp(judicialOfficeAuthorisation.getStartDate()));
        judOfficeAppointmentRow.put("end_date", getDateTimeStamp(judicialOfficeAuthorisation.getEndDate()));
        judOfficeAppointmentRow.put("created_date", getDateTimeStamp(judicialOfficeAuthorisation.getCreatedDate()));
        judOfficeAppointmentRow.put("last_updated", getDateTimeStamp(judicialOfficeAuthorisation.getLastUpdated()));
        judOfficeAppointmentRow.put("lower_level", judicialOfficeAuthorisation.getLowerLevel());
        return  judOfficeAppointmentRow;
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