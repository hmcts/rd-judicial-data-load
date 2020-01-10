package uk.gov.hmcts.reform.juddata.camel.beans;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.time.LocalDate;

@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX" , skipFirstLine = true)
public class JudicialOfficeAuthorisation {

    @DataField(pos = 1)
    String elinks_id;

    @DataField(pos = 2)
    String authorisation_id;

    @DataField(pos = 3)
    String jurisdiction_id;

    @DataField(pos = 4, pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDate authorisation_date;

    @DataField(pos = 5)
    String extracted_date;

}
