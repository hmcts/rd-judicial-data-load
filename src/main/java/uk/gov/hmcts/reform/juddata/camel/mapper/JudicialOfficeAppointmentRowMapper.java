package uk.gov.hmcts.reform.juddata.camel.mapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.mapper.IMapper;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.util.CommonUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static uk.gov.hmcts.reform.juddata.camel.util.CommonUtils.getDateTimeStamp;
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
        judOfficeAppointmentRow.put("start_date", getDateTimeStamp(officeAppointment.getStartDate()));
        judOfficeAppointmentRow.put("end_date", getDateTimeStamp(officeAppointment.getEndDate()));
        judOfficeAppointmentRow.put("active_flag", officeAppointment.isActiveFlag());
        judOfficeAppointmentRow.put("extracted_date", getDateTimeStamp(officeAppointment.getExtractedDate()));
        judOfficeAppointmentRow.put("created_date", getCurrentTimeStamp());
        judOfficeAppointmentRow.put("last_loaded_date", getCurrentTimeStamp());
        judOfficeAppointmentRow.put("personal_code", officeAppointment.getPersonalCode());
        judOfficeAppointmentRow.put("appointment_type", officeAppointment.getAppointmentType());
        judOfficeAppointmentRow.put("object_id", officeAppointment.getObjectId());
        judOfficeAppointmentRow.put("appointment", officeAppointment.getAppointment());
        judOfficeAppointmentRow.put("primary_location", officeAppointment.getPrimaryLocation());
        judOfficeAppointmentRow.put("secondary_location", officeAppointment.getSecondaryLocation());
        judOfficeAppointmentRow.put("tertiary_location", officeAppointment.getTertiaryLocation());

        Optional<String> mrdCreatedTimeOptional =
                Optional.ofNullable(officeAppointment.getMrdCreatedTime()).filter(Predicate.not(String::isEmpty));
        judOfficeAppointmentRow.put("mrd_created_time", mrdCreatedTimeOptional.map(CommonUtils::getDateTimeStamp)
                .orElse(null));

        Optional<String> mrdUpdatedTimeOptional =
                Optional.ofNullable(officeAppointment.getMrdUpdatedTime()).filter(Predicate.not(String::isEmpty));
        judOfficeAppointmentRow.put("mrd_updated_time", mrdUpdatedTimeOptional.map(CommonUtils::getDateTimeStamp)
                .orElse(null));

        Optional<String> mrdDeletedTimeOptional =
                Optional.ofNullable(officeAppointment.getMrdDeletedTime()).filter(Predicate.not(String::isEmpty));
        judOfficeAppointmentRow.put("mrd_deleted_time", mrdDeletedTimeOptional.map(CommonUtils::getDateTimeStamp)
                .orElse(null));

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
