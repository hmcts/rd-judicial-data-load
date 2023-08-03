package uk.gov.hmcts.reform.juddata.camel.binder;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.domain.CommonCsvField;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.DatePattern;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.DATE_FORMAT_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.DATE_PATTERN;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.DATE_TIME_FORMAT;

@Component
@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX", skipFirstLine = true)
public class JudicialUserRoleType extends CommonCsvField implements Serializable {

    @DataField(pos = 1, columnName = "per_Id")
    @NotEmpty
    String perId;

    @DataField(pos = 2, columnName = "title")
    @NotEmpty
    String title;

    @DataField(pos = 3, columnName = "location")
    String location;

    @DataField(pos = 4, columnName = "start_date")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
            message = DATE_FORMAT_ERROR_MESSAGE)
    String startDate;

    @DataField(pos = 5, columnName = "end_date")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
            message = "date pattern should be " + DATE_TIME_FORMAT)
    String endDate;

}
