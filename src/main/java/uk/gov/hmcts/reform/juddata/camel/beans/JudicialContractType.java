package uk.gov.hmcts.reform.juddata.camel.beans;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX", skipFirstLine = true)
public class JudicialContractType {

    @DataField(pos = 1, columnName = "contract_type_id")
    String contractTypeId;

    @DataField(pos = 2, columnName = "contract_type_desc_en")
    String contractTypeDescEn;

    @DataField(pos = 3, columnName = "contract_type_desc_cy")
    String contractTypeDescCy;

}
