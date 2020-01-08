package uk.gov.hmcts.reform.juddata.camel.beans;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;



@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX" , skipFirstLine = true)
public class JudicialOfficeAppointment {

    @DataField(pos = 1)
    String elinks_id;

    @DataField(pos = 2)
    String role_id;

    @DataField(pos = 3)
    String contract_type;

    @DataField(pos = 4)
    String base_location_id;

    @DataField(pos = 5)
    String region_id;

    @DataField(pos = 6)
    Boolean is_Principal_Appointment;

    @DataField(pos = 7, pattern = "dd/MM/yyyy hh:mm")
    LocalDate start_Date;

    @DataField(pos = 8,  pattern = "dd/MM/yyyy hh:mm")
    LocalDate end_Date;

    @DataField(pos = 9)
    boolean active_Flag;

    @DataField(pos = 10)
    String extractedDate;

}