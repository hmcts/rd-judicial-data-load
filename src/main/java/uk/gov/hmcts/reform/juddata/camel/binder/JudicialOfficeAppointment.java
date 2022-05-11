package uk.gov.hmcts.reform.juddata.camel.binder;

import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.DATE_PATTERN;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.DATE_TIME_FORMAT;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.domain.CommonCsvField;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.DatePattern;
import uk.gov.hmcts.reform.juddata.validators.Appointment;

@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX", skipFirstLine = true, skipField = true)
@Component
public class JudicialOfficeAppointment extends CommonCsvField implements Serializable {

    @DataField(pos = 1, columnName = "per_id")
    @NotEmpty
    String perId;

    @DataField(pos = 2, columnName = "base_location_id", defaultValue = "0")
    String baseLocationId;

    @DataField(pos = 3, columnName = "region_id", defaultValue = "0")
    String regionId;

    @DataField(pos = 4, columnName = "is_Principal_Appointment")
    Boolean isPrincipalAppointment;

    @DataField(pos = 5, columnName = "start_Date")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
            message = "date pattern should be " + DATE_TIME_FORMAT)
    String startDate;

    @DataField(pos = 6, columnName = "end_Date")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
            message = "date pattern should be " + DATE_TIME_FORMAT)
    String endDate;

    @DataField(pos = 7, columnName = "active_Flag")
    boolean activeFlag;

    @DataField(pos = 8)
    @DatePattern(isNullAllowed = "false", regex = DATE_PATTERN,
            message = "date pattern should be " + DATE_TIME_FORMAT)
    String extractedDate;

    @DataField(pos = 9, columnName = "personal_code")
    String personalCode;

    @DataField(pos = 10, columnName = "appointment_type")
    String appointmentType;

    @DataField(pos = 11, columnName = "object_id")
    String objectId;

    @DataField(pos = 12, columnName = "appointment")
    @NotEmpty
    @Appointment
    String appointment;

    @DataField(pos = 13, columnName = "primary_location", defaultValue = "0")
    String primaryLocation;

    @DataField(pos = 14, columnName = "secondary_location", defaultValue = "0")
    String secondaryLocation;

    @DataField(pos = 15, columnName = "tertiary_location", defaultValue = "0")
    String tertiaryLocation;

    @DataField(pos = 16, columnName = "mrd_created_time")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
            message = "date pattern should be " + DATE_TIME_FORMAT)
    String mrdCreatedTime;

    @DataField(pos = 17, columnName = "mrd_updated_time")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
            message = "date pattern should be " + DATE_TIME_FORMAT)
    String mrdUpdatedTime;

    @DataField(pos = 18, columnName = "mrd_deleted_time")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
            message = "date pattern should be " + DATE_TIME_FORMAT)
    String mrdDeletedTime;

}