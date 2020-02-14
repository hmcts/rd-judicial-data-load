package uk.gov.hmcts.reform.juddata.camel.beans;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;



@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX", skipFirstLine = true)
public class JudicialOfficeAppointment {

    @DataField(pos = 1, columnName = "elinks_id")
    String elinksId;

    @DataField(pos = 2, columnName = "role_id")
    String roleId;

    @DataField(pos = 3, columnName = "contract_type")
    String contractType;

    @DataField(pos = 4, columnName = "base_location_id")
    String baseLocationId;

    @DataField(pos = 5, columnName = "region_id")
    String regionId;

    @DataField(pos = 6, columnName = "is_Principal_Appointment")
    Boolean isPrincipalAppointment;

    @DataField(pos = 7, pattern = "yyyy-MM-dd hh:mm:ss", columnName = "start_Date")
    LocalDate startDate;

    @DataField(pos = 8,  pattern = "yyyy-MM-dd hh:mm:ss", columnName = "end_Date")
    LocalDate endDate;

    @DataField(pos = 9, columnName = "active_Flag")
    boolean activeFlag;

    @DataField(pos = 10)
    String extractedDate;
}