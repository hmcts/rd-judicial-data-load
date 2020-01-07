package uk.gov.hmcts.reform.juddata.camel.mapper;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialOfficeAppointment;

@Slf4j
@Component
public class JudicialOfficeAppointmentRowMapper {

    private static int seqNumber = 0;

    public Map<String, Object> getMap(JudicialOfficeAppointment officeAppoinemnt) {

        Map<String, Object> judOfficeAppointmentRow = new HashMap<>();

        judOfficeAppointmentRow.put("judicial_office_appointment_Id", generateId());
        judOfficeAppointmentRow.put("elinks_id", officeAppoinemnt.getElinks_Id());
        judOfficeAppointmentRow.put("role_id", officeAppoinemnt.getRole_id());
        judOfficeAppointmentRow.put("contract_type_Id", officeAppoinemnt.getContract_type());
        judOfficeAppointmentRow.put("base_location_Id", officeAppoinemnt.getBase_location_id());
        judOfficeAppointmentRow.put("region_Id", officeAppoinemnt.getRegion_id());
        judOfficeAppointmentRow.put("is_prinicple_appointment", officeAppoinemnt.getIs_Principal_Appointment());
        judOfficeAppointmentRow.put("start_date", officeAppoinemnt.getStart_Date());
        judOfficeAppointmentRow.put("end_date", officeAppoinemnt.getEnd_Date());
        judOfficeAppointmentRow.put("active_flag", officeAppoinemnt.isActive_Flag());
        judOfficeAppointmentRow.put("extracted_date", getDateTimeStamp(officeAppoinemnt.getExtractedDate()));
        return  judOfficeAppointmentRow;
    }

    private Timestamp getDateTimeStamp(String date) {

        System.out.println(":::date:: " + date);
        return Timestamp.valueOf(date);
    }


    private int generateId() {
        seqNumber = seqNumber +1;
        return seqNumber;
    }

}
