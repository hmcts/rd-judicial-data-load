package uk.gov.hmcts.reform.juddata.camel.beans;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.springframework.stereotype.Component;

@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX", skipFirstLine = true)
@Component
public class JudicialContractType implements Serializable {

    @DataField(pos = 1, columnName = "contract_type_id")
    String contractTypeId;

    @DataField(pos = 2, columnName = "contract_type_desc_en")
    String contractTypeDescEn;

    @DataField(pos = 3, columnName = "contract_type_desc_cy")
    String contractTypeDescCy;

}
