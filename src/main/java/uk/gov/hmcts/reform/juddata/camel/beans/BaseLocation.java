package uk.gov.hmcts.reform.juddata.camel.beans;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX" , skipFirstLine = true)
public class BaseLocation {

    @DataField(pos = 1)
    String base_location_Id;

    @DataField(pos = 2)
    String court_name;

    @DataField(pos = 3)
    String court_type;

    @DataField(pos = 4)
    String circuit;

    @DataField(pos = 5)
    String area;

}
