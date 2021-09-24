package uk.gov.hmcts.reform.juddata.camel.binder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.domain.CommonCsvField;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.DatePattern;
import uk.gov.hmcts.reform.juddata.validators.Appointment;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.DATE_PATTERN;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.DATE_PATTERN_TIMESTAMP;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.DATE_TIMESTAMP_FORMAT;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.DATE_TIME_FORMAT;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@CsvRecord(separator = ",", crlf = "UNIX", skipFirstLine = true, skipField = true)
@Component
public class JudicialUserProfile extends CommonCsvField implements Serializable {

    @DataField(pos = 1, columnName = "per_id")
    @NotEmpty
    String perId;

    @DataField(pos = 2, columnName = "personal_Code")
    @NotEmpty
    String personalCode;

    @DataField(pos = 3)
    @NotEmpty
    @Appointment
    String appointment;

    @DataField(pos = 4, columnName = "known_As")
    String knownAs;

    @DataField(pos = 5)
    @NotEmpty
    String surName;

    @DataField(pos = 6)
    @NotEmpty
    String fullName;

    @DataField(pos = 7, columnName = "post_Nominals")
    String postNominals;

    @DataField(pos = 8, columnName = "appointment_type_id")
    String appointmentTypeId;

    @DataField(pos = 9, columnName = "work_Pattern")
    String workPattern;

    @DataField(pos = 10, columnName = "ejudiciary_email")
    String ejudiciaryEmail;

    @DataField(pos = 11, columnName = "joining_Date")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
            message = "date pattern should be " + DATE_TIME_FORMAT)
    String joiningDate;

    @DataField(pos = 12, columnName = "lastWorking_Date")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
            message = "date pattern should be " + DATE_TIME_FORMAT)
    String lastWorkingDate;

    @DataField(pos = 13, columnName = "active_Flag")
    boolean activeFlag;

    @DataField(pos = 14)
    @DatePattern(isNullAllowed = "false", regex = DATE_PATTERN_TIMESTAMP,
            message = "date pattern should be " + DATE_TIMESTAMP_FORMAT)
    String extractedDate;

    @DataField(pos = 15, columnName = "object_id")
    String objectId;

}