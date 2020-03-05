package uk.gov.hmcts.reform.juddata.camel.beans;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.dataformat.bindy.annotation.FixedLengthRecord;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX" , skipFirstLine = true, skipField = true)
public class JudicialBaseLocationType {

    @DataField(pos = 1)
    String base_location_id;

    @DataField(pos = 2)
    String court_name;

    @DataField(pos = 3)
    String court_type;

    @DataField(pos = 4)
    String circuit;

    @DataField(pos = 5)
    String area;

    @DataField(pos = 6)
    String national_court_code;

}