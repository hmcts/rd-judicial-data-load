package uk.gov.hmcts.reform.juddata.camel.binder;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.springframework.stereotype.Component;

@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX", skipFirstLine = true, skipField = true)
@Component
public class JudicialUserProfile implements Serializable {

    @DataField(pos = 1, columnName = "elinks_id")
    String elinksId;

    @DataField(pos = 2, columnName = "personal_Code")
    String personalCode;

    @DataField(pos = 3)
    String title;

    @DataField(pos = 4, columnName = "known_As")
    String knownAs;

    @DataField(pos = 5)
    String surName;

    @DataField(pos = 6)
    String fullName;

    @DataField(pos = 7, columnName = "post_Nominals")
    String postNominals;

    @DataField(pos = 8, columnName = "contract_Type_id")
    String contractTypeId;

    @DataField(pos = 9, columnName = "work_Pattern")
    String workPattern;

    @DataField(pos = 10, columnName = "email_id")
    String emailId;

    @DataField(pos = 11, pattern = "yyyy-MM-dd hh:mm:ss", columnName = "joining_Date")
    LocalDate joiningDate;

    @DataField(pos = 12,  pattern = "yyyy-MM-dd hh:mm:ss", columnName = "lastWorking_Date")
    LocalDate lastWorkingDate;

    @DataField(pos = 13, columnName = "active_Flag")
    boolean activeFlag;

    @DataField(pos = 14)
    String extractedDate;
}