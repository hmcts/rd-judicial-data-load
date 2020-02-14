package uk.gov.hmcts.reform.juddata.camel.mapper;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialOfficeAuthorisation;

@Slf4j
@Component
public class JudicialOfficeAuthorisationRowMapper {

    private static int seqNumber = 0;

    public Map<String, Object> getMap(JudicialOfficeAuthorisation judicialOfficeAuthorisation) {

        Map<String, Object> judOfficeAppointmentRow = new HashMap<>();

        judOfficeAppointmentRow.put("judicial_office_auth_id", generateId());
        judOfficeAppointmentRow.put("elinks_id", judicialOfficeAuthorisation.getElinksId());
        judOfficeAppointmentRow.put("authorisation_id", judicialOfficeAuthorisation.getAuthorisationId());
        judOfficeAppointmentRow.put("jurisdiction_id", judicialOfficeAuthorisation.getJurisdictionId());
        judOfficeAppointmentRow.put("authorisation_date", judicialOfficeAuthorisation.getAuthorisationDate());
        judOfficeAppointmentRow.put("extracted_date", getDateTimeStamp(judicialOfficeAuthorisation.getExtractedDate()));
        judOfficeAppointmentRow.put("created_date", getCurrentTimeStamp());
        judOfficeAppointmentRow.put("last_loaded_date", getCurrentTimeStamp());
        return  judOfficeAppointmentRow;
    }

    private Timestamp getDateTimeStamp(String date) {
        return Timestamp.valueOf(date);
    }

    private Timestamp getCurrentTimeStamp() {

        return new Timestamp(new Date().getTime());
    }

    private int generateId() {
        seqNumber = seqNumber + 1;
        return seqNumber;
    }

}
