package uk.gov.hmcts.reform.juddata.camel.beans;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX" , skipFirstLine = true)
public class JudicialAuthorisationType {

    @DataField(pos = 1)
    String authorisation_id;

    @DataField(pos = 2)
    String authorisation_desc_en;

    @DataField(pos = 3)
    String authorisation_desc_cy;

    @DataField(pos = 4)
    String jurisdiction_id;

    @DataField(pos = 5)
    String jurisdiction_desc_en;

    @DataField(pos = 6)
    String jurisdiction_desc_cy;

}
