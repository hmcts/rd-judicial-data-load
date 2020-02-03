package uk.gov.hmcts.reform.juddata.camel.beans;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX", skipFirstLine = true)
public class JudicialAuthorisationType {

    @DataField(pos = 1, columnName = "authorisation_id")
    String authorisationId;

    @DataField(pos = 2, columnName = "authorisation_desc_en")
    String authorisationDescEn;

    @DataField(pos = 3, columnName = "authorisation_desc_cy")
    String authorisationDescCy;

    @DataField(pos = 4, columnName = "jurisdiction_id")
    String jurisdictionId;

    @DataField(pos = 5, columnName = "jurisdiction_desc_en")
    String jurisdictionDescEn;

    @DataField(pos = 6, columnName = "jurisdiction_desc_cy")
    String jurisdictionDescCy;

}
