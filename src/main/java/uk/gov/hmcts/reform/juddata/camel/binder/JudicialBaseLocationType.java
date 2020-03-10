package uk.gov.hmcts.reform.juddata.camel.binder;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX", skipFirstLine = true, skipField = true)
public class JudicialBaseLocationType {

    @DataField(pos = 1, columnName = "base_location_id")
    String baseLocationId;

    @DataField(pos = 2, columnName = "court_name")
    String courtName;

    @DataField(pos = 3, columnName = "court_type")
    String courtType;

    @DataField(pos = 4, columnName = "circuit")
    String circuit;

    @DataField(pos = 5, columnName = "area")
    String area;
}