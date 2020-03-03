package uk.gov.hmcts.reform.juddata.camel.mapper;

import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.getCurrentTimeStamp;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.getDateTimeStamp;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialOfficeAppointment;

@Slf4j
@Component
public class JudicialOfficeAppointmentRowMapper {

    private int seqNumber = 0;

    public Map<String, Object> getMap(JudicialOfficeAppointment officeAppoinemnt) {

        Map<String, Object> judOfficeAppointmentRow = new HashMap<>();

        judOfficeAppointmentRow.put("judicial_office_appointment_id", generateId());
        judOfficeAppointmentRow.put("elinks_id", officeAppoinemnt.getElinksId());
        judOfficeAppointmentRow.put("role_id",officeAppoinemnt.getRoleId());
        judOfficeAppointmentRow.put("contract_type_id", returnNullIfBlank(officeAppoinemnt.getContractType()));
        judOfficeAppointmentRow.put("base_location_id", returnNullIfBlank(officeAppoinemnt.getBaseLocationId()));
        judOfficeAppointmentRow.put("region_id", returnNullIfBlank(officeAppoinemnt.getRegionId()));
        judOfficeAppointmentRow.put("is_prinicple_appointment", officeAppoinemnt.getIsPrincipalAppointment());
        judOfficeAppointmentRow.put("start_date", officeAppoinemnt.getStartDate());
        judOfficeAppointmentRow.put("end_date", officeAppoinemnt.getEndDate());
        judOfficeAppointmentRow.put("active_flag", officeAppoinemnt.isActiveFlag());
        judOfficeAppointmentRow.put("extracted_date", getDateTimeStamp(officeAppoinemnt.getExtractedDate()));
        judOfficeAppointmentRow.put("created_date", getCurrentTimeStamp());
        judOfficeAppointmentRow.put("last_loaded_date", getCurrentTimeStamp());
        return  judOfficeAppointmentRow;
    }

    private int generateId() {
        seqNumber = seqNumber + 1;
        return seqNumber;
    }

    public String returnNullIfBlank(String fieldValue) {
        if (StringUtils.isBlank(fieldValue)) {
            return null;
        } else {
            return fieldValue;
        }
    }

}
