package uk.gov.hmcts.reform.juddata.camel.binder;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.domain.CommonCsvField;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.DatePattern;

import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.DATE_FORMAT_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.DATE_FORMAT_WITH_MILLIS;

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

    @DataField(pos = 4, columnName = "mrd_created_time")
    @DatePattern(isNullAllowed = "true", regex = DATE_FORMAT_WITH_MILLIS,
            message = DATE_FORMAT_ERROR_MESSAGE)
    String mrdCreatedTime;

    @DataField(pos = 5, columnName = "mrd_updated_time")
    @DatePattern(isNullAllowed = "true", regex = DATE_FORMAT_WITH_MILLIS,
            message = DATE_FORMAT_ERROR_MESSAGE)
    String mrdUpdatedTime;

    @DataField(pos = 6, columnName = "mrd_deleted_time")
    @DatePattern(isNullAllowed = "true", regex = DATE_FORMAT_WITH_MILLIS,
            message = DATE_FORMAT_ERROR_MESSAGE)
    String mrdDeletedTime;

}
