package uk.gov.hmcts.reform.juddata.camel.beans;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX" , skipFirstLine = true)
public class JudicialUserRoleType {

    @DataField(pos = 1)
    String role_id;

    @DataField(pos = 2)
    String role_desc_en;

    @DataField(pos = 3)
    String role_desc_cy;

}
