package uk.gov.hmcts.reform.juddata.camel.binder;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.DatePattern;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.DATE_PATTERN;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.DATE_PATTERN_TIMESTAMP;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.DATE_TIMESTAMP_FORMAT;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.DATE_TIME_FORMAT;

@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX", skipFirstLine = true, skipField = true)
@Component
public class JudicialUserProfile implements Serializable {

    @DataField(pos = 1, columnName = "elinks_id")
    @NotEmpty
    String elinksId;

    @DataField(pos = 2, columnName = "personal_Code")
    @NotEmpty
    String personalCode;

    @DataField(pos = 3)
    @NotEmpty
    String title;

    @DataField(pos = 4, columnName = "known_As")
    @NotEmpty
    String knownAs;

    @DataField(pos = 5)
    @NotEmpty
    String surName;

    @DataField(pos = 6)
    @NotEmpty
    String fullName;

    @DataField(pos = 7, columnName = "post_Nominals")
    String postNominals;

    @DataField(pos = 8, columnName = "contract_Type_id")
    String contractTypeId;

    @DataField(pos = 9, columnName = "work_Pattern")
    String workPattern;

    @DataField(pos = 10, columnName = "email_id")
    String emailId;

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


}