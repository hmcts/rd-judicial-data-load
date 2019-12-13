package uk.gov.hmcts.reform.juddata.camel.beans;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;


@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX" , skipFirstLine = true)
public class JudicialUserProfile {

    @DataField(pos = 1)
    String elinksId;

    @DataField(pos = 2)
    String personalCode;

    @DataField(pos = 3)
    String title;

    @DataField(pos = 4)
    String knownAs;

    @DataField(pos = 5)
    String surName;

    @DataField(pos = 6)
    String fullName;

    @DataField(pos = 7)
    String postNominals;

    @DataField(pos = 8)
    String contractType;

    @DataField(pos = 9)
    String workPattern;

    @DataField(pos = 10)
    String emailId;

    @DataField(pos = 11, pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDate joinDate;

    @DataField(pos = 12,  pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDate lastWorkingDate;

    @DataField(pos = 13)
    boolean activeFlag;

    @DataField(pos = 14)
    String extractedDate;

}
