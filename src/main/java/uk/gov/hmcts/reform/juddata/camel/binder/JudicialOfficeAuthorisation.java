package uk.gov.hmcts.reform.juddata.camel.binder;

import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.DATE_FORMAT_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.DATE_PATTERN;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.DATE_TIME_FORMAT;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.domain.CommonCsvField;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.DatePattern;

@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX", skipFirstLine = true, skipField = true)
@Component
public class JudicialOfficeAuthorisation extends CommonCsvField implements Serializable {

    @DataField(pos = 1, columnName = "per_id")
    @NotEmpty
    String perId;

    @DataField(pos = 2, columnName = "jurisdiction")
    String jurisdiction;

    @DataField(pos = 3, columnName = "ticketid")
    Long ticketId;

    @DataField(pos = 4, columnName = "startdate")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
            message = DATE_FORMAT_ERROR_MESSAGE)
    String startDate;

    @DataField(pos = 5, columnName = "enddate")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
            message = DATE_FORMAT_ERROR_MESSAGE)
    String endDate;

    @DataField(pos = 6, columnName = "lowerlevel")
    String lowerLevel;

    @DataField(pos = 7, columnName = "personal_code")
    String personalCode;

    @DataField(pos = 8, columnName = "object_id")
    String objectId;

    @DataField(pos = 9, columnName = "mrd_created_time")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
        message = "date pattern should be " + DATE_TIME_FORMAT)
    String mrdCreatedTime;

    @DataField(pos = 10, columnName = "mrd_updated_time")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
        message = "date pattern should be " + DATE_TIME_FORMAT)
    String mrdUpdatedTime;

    @DataField(pos = 11, columnName = "mrd_deleted_time")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
        message = "date pattern should be " + DATE_TIME_FORMAT)
    String mrdDeletedTime;


}