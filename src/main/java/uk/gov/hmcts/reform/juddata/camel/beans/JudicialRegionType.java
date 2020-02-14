package uk.gov.hmcts.reform.juddata.camel.beans;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX", skipFirstLine = true)
public class JudicialRegionType {

    @DataField(pos = 1, columnName = "region_id")
    String regionId;

    @DataField(pos = 2, columnName = "region_desc_en")
    String regionDescEn;

    @DataField(pos = 3, columnName = "region_desc_cy")
    String regionDescCy;
}
