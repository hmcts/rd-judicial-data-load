package uk.gov.hmcts.reform.juddata.camel.beans;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX" , skipFirstLine = true)
public class JudicialUserRole {

    @DataField(pos = 1)
    String roleID;

    @DataField(pos = 2)
    String roleDescription;

}
