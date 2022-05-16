package uk.gov.hmcts.reform.juddata.camel.binder;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.domain.CommonCsvField;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.DatePattern;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.DATE_PATTERN;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.DATE_TIME_FORMAT;

@Getter
@Setter
@CsvRecord(separator = ",", crlf = "UNIX", skipFirstLine = true, skipField = true)
@Component
public class JudicialUserProfile extends CommonCsvField implements Serializable {

    @DataField(pos = 1, columnName = "per_id")
    @NotEmpty
    String perId;

    @DataField(pos = 2, columnName = "personal_Code")
    @NotEmpty
    String personalCode;

    @DataField(pos = 3, columnName = "known_As")
    String knownAs;

    @DataField(pos = 4)
    @NotEmpty
    String surName;

    @DataField(pos = 5)
    @NotEmpty
    String fullName;

    @DataField(pos = 6, columnName = "post_Nominals")
    String postNominals;

    @DataField(pos = 7, columnName = "work_Pattern")
    String workPattern;

    @DataField(pos = 8, columnName = "ejudiciary_email")
    String ejudiciaryEmail;

    @DataField(pos = 9, columnName = "joining_Date")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
            message = "date pattern should be " + DATE_TIME_FORMAT)
    String joiningDate;

    @DataField(pos = 10, columnName = "lastWorking_Date")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
            message = "date pattern should be " + DATE_TIME_FORMAT)
    String lastWorkingDate;

    @DataField(pos = 11, columnName = "active_Flag")
    boolean activeFlag;

    @DataField(pos = 12)
    @DatePattern(isNullAllowed = "false", regex = DATE_PATTERN,
            message = "date pattern should be " + DATE_TIME_FORMAT)
    String extractedDate;

    @DataField(pos = 13, columnName = "object_id")
    String objectId;

    @DataField(pos = 14, columnName = "Is_Judge")
    boolean judge;

    @DataField(pos = 15, columnName = "Is_Panel_Member")
    boolean panelMember;

    @DataField(pos = 16, columnName = "Is_Magistrate")
    boolean magistrate;

    @DataField(pos = 17, columnName = "MRD_Created_Time")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
            message = "date pattern should be " + DATE_TIME_FORMAT)
    String mrdCreatedTime;

    @DataField(pos = 18, columnName = "MRD_Updated_Time")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
            message = "date pattern should be " + DATE_TIME_FORMAT)
    String mrdUpdatedTime;

    @DataField(pos = 19, columnName = "MRD_Deleted_Time")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
            message = "date pattern should be " + DATE_TIME_FORMAT)
    String mrdDeletedTime;

}