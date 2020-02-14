package uk.gov.hmcts.reform.juddata.camel.beans;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX", skipFirstLine = true)
public class JudicialUserRoleType {

    @DataField(pos = 1, columnName = "role_id")
    String roleId;

    @DataField(pos = 2, columnName = "role_desc_en")
    String roleDescEn;

    @DataField(pos = 3, columnName = "role_desc_cy")
    String roleDescCy;

}
