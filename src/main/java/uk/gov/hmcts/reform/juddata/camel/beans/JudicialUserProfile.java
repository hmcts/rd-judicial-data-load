package uk.gov.hmcts.reform.juddata.camel.beans;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX" , skipFirstLine = true)
public class JudicialUserProfile {

    @DataField(pos = 1)
    String sno;
    @DataField(pos = 2)
    String firstName;
    @DataField(pos = 3)
    String lastName;
    @DataField(pos = 4)
    String circuit;
    @DataField(pos = 5)
    String area;
}
