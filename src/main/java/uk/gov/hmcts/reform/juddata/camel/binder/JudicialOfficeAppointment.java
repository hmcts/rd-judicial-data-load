package uk.gov.hmcts.reform.juddata.camel.binder;

import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.DATE_PATTERN;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.DATE_PATTERN_TIMESTAMP;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.validator.DatePattern;

@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX", skipFirstLine = true, skipField = true)
@Component
public class JudicialOfficeAppointment implements Serializable {

    @DataField(pos = 1, columnName = "elinks_id")
    @NotEmpty
    String elinksId;

    @DataField(pos = 2, columnName = "role_id", defaultValue = "0")
    String roleId;

    @DataField(pos = 3, columnName = "contract_type", defaultValue = "0")
    String contractType;

    @DataField(pos = 4, columnName = "base_location_id", defaultValue = "0")
    String baseLocationId;

    @DataField(pos = 5, columnName = "region_id", defaultValue = "0")
    String regionId;

    @DataField(pos = 6, columnName = "is_Principal_Appointment")
    Boolean isPrincipalAppointment;

    @DataField(pos = 7, columnName = "start_Date")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
            message = "date pattern should be yyyy-MM-dd hh:mm:ss")
    String startDate;

    @DataField(pos = 8, columnName = "end_Date")
    @DatePattern(isNullAllowed = "true", regex = DATE_PATTERN,
            message = "date pattern should be yyyy-MM-dd hh:mm:ss")
    String endDate;

    @DataField(pos = 9, columnName = "active_Flag")
    boolean activeFlag;

    @DataField(pos = 10)
    @DatePattern(isNullAllowed = "false", regex = DATE_PATTERN_TIMESTAMP,
            message = "date pattern should be yyyy-MM-dd HH:MI:SS.MSUS")
    String extractedDate;
}