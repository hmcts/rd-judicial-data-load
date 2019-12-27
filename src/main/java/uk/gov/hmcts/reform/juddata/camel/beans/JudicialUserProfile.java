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
    String elinks_Id;

    @DataField(pos = 2)
    String personal_Code;

    @DataField(pos = 3)
    String title;

    @DataField(pos = 4)
    String known_As;

    @DataField(pos = 5)
    String surName;

    @DataField(pos = 6)
    String fullName;

    @DataField(pos = 7)
    String post_Nominals;

    @DataField(pos = 8)
    String contract_Type_Id;

    @DataField(pos = 9)
    String work_Pattern;

    @DataField(pos = 10)
    String email_Id;

    @DataField(pos = 11, pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDate joining_Date;

    @DataField(pos = 12,  pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDate lastWorking_Date;

    @DataField(pos = 13)
    boolean active_Flag;

    @DataField(pos = 14)
    String extractedDate;

}