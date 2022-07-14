package uk.gov.hmcts.reform.juddata.camel.binder;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.domain.CommonCsvField;

import javax.validation.constraints.NotEmpty;

@Component
@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX", skipFirstLine = true)
public class JudicialRegionType extends CommonCsvField {

    @DataField(pos = 1, columnName = "region_id")
    @NotEmpty
    String regionId;

    @DataField(pos = 2, columnName = "region_desc_en")
    @NotEmpty
    String regionDescEn;

    @DataField(pos = 3, columnName = "region_desc_cy")
    String regionDescCy;

}
