package uk.gov.hmcts.reform.juddata.camel.binder;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.domain.CommonCsvField;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.DatePattern;

import javax.validation.constraints.NotEmpty;

import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.DATE_PATTERN;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.DATE_TIME_FORMAT;

@Component
@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX", skipFirstLine = true, skipField = true)
public class JudicialBaseLocationType extends CommonCsvField {

    @DataField(pos = 1, columnName = "base_location_id")
    @NotEmpty
    String baseLocationId;

    @DataField(pos = 2, columnName = "court_name")
    String courtName;

    @DataField(pos = 3, columnName = "court_type")
    String courtType;

    @DataField(pos = 4, columnName = "circuit")
    String circuit;

    @DataField(pos = 5, columnName = "area")
    String area;

    @DataField(pos = 6, columnName = "mrd_created_time")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
            message = "date pattern should be " + DATE_TIME_FORMAT)
    String mrdCreatedTime;

    @DataField(pos = 7, columnName = "mrd_updated_time")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
            message = "date pattern should be " + DATE_TIME_FORMAT)
    String mrdUpdatedTime;

    @DataField(pos = 8, columnName = "mrd_deleted_time")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
            message = "date pattern should be " + DATE_TIME_FORMAT)
    String mrdDeletedTime;
}