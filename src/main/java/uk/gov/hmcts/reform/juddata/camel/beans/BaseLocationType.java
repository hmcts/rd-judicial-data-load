package uk.gov.hmcts.reform.juddata.camel.beans;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX", skipFirstLine = true)
public class BaseLocationType {

    @DataField(pos = 1, columnName = "base_location_id")
    String baseLocationId;

    @DataField(pos = 2, columnName = "court_name")
    String courtName;

    @DataField(pos = 3, columnName = "court_type")
    String courtType;

    @DataField(pos = 4)
    String circuit;

    @DataField(pos = 5)
    String area;

}
