package uk.gov.hmcts.reform.juddata.camel.mapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.mapper.IMapper;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;

import java.util.HashMap;
import java.util.Map;

import static uk.gov.hmcts.reform.data.ingestion.camel.util.DataLoadUtil.getCurrentTimeStamp;

@Slf4j
@Component
public class JudicialOfficeAppointmentRowMapper implements IMapper {

    private int seqNumber = 0;

    public Map<String, Object> getMap(Object officeAppoinemntObject) {

        JudicialOfficeAppointment officeAppointment = (JudicialOfficeAppointment) officeAppoinemntObject;
        Map<String, Object> judOfficeAppointmentRow = new HashMap<>();

        judOfficeAppointmentRow.put("judicial_office_appointment_id", generateId());
        judOfficeAppointmentRow.put("per_id", officeAppointment.getPerId());
        judOfficeAppointmentRow.put("base_location_id", returnNullIfBlank(officeAppointment.getBaseLocationId()));
        judOfficeAppointmentRow.put("region_id", returnNullIfBlank(officeAppointment.getRegionId()));
        judOfficeAppointmentRow.put("is_prinicple_appointment", officeAppointment.getIsPrincipalAppointment());
        judOfficeAppointmentRow.put("start_date", officeAppointment.getStartDate());
        judOfficeAppointmentRow.put("end_date", officeAppointment.getEndDate());
        judOfficeAppointmentRow.put("active_flag", officeAppointment.isActiveFlag());
        judOfficeAppointmentRow.put("extracted_date", officeAppointment.getExtractedDate());
        judOfficeAppointmentRow.put("created_date", getCurrentTimeStamp());
        judOfficeAppointmentRow.put("last_loaded_date", getCurrentTimeStamp());
        judOfficeAppointmentRow.put("personal_code", officeAppointment.getPersonalCode());
        judOfficeAppointmentRow.put("object_id", officeAppointment.getObjectId());
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
