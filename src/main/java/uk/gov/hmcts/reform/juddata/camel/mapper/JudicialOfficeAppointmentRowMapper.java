package uk.gov.hmcts.reform.juddata.camel.mapper;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialOfficeAppointment;

@Slf4j
@Component
public class JudicialOfficeAppointmentRowMapper {

    private static int seqNumber = 0;

    public Map<String, Object> getMap(JudicialOfficeAppointment officeAppoinemnt) {

        Map<String, Object> judOfficeAppointmentRow = new HashMap<>();

        judOfficeAppointmentRow.put("judicial_office_appointment_id", generateId());
        judOfficeAppointmentRow.put("elinks_id", officeAppoinemnt.getElinksId());
        judOfficeAppointmentRow.put("role_id", officeAppoinemnt.getRoleId());
        judOfficeAppointmentRow.put("contract_type_id", officeAppoinemnt.getContractType());
        judOfficeAppointmentRow.put("base_location_id", officeAppoinemnt.getBaseLocationId());
        judOfficeAppointmentRow.put("region_id", officeAppoinemnt.getRegionId());
        judOfficeAppointmentRow.put("is_prinicple_appointment", officeAppoinemnt.getIsPrincipalAppointment());
        judOfficeAppointmentRow.put("start_date", officeAppoinemnt.getStartDate());
        judOfficeAppointmentRow.put("end_date", officeAppoinemnt.getEndDate());
        judOfficeAppointmentRow.put("active_flag", officeAppoinemnt.isActiveFlag());
        judOfficeAppointmentRow.put("extracted_date", getDateTimeStamp(officeAppoinemnt.getExtractedDate()));
        return  judOfficeAppointmentRow;
    }

    private Timestamp getDateTimeStamp(String date) {
        return Timestamp.valueOf(date);
    }

    private int generateId() {
        seqNumber = seqNumber + 1;
        return seqNumber;
    }

}
