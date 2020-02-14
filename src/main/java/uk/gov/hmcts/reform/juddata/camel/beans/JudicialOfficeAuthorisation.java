package uk.gov.hmcts.reform.juddata.camel.beans;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.springframework.stereotype.Component;

@Setter
@Getter
@CsvRecord(separator = ",", crlf = "UNIX", skipFirstLine = true)
@Component
public class JudicialOfficeAuthorisation {

    @DataField(pos = 1, columnName = "elinks_id")
    String elinksId;

    @DataField(pos = 2, columnName = "authorisation_id")
    String authorisationId;

    @DataField(pos = 3, columnName = "jurisdiction_id")
    String jurisdictionId;

    @DataField(pos = 4, pattern = "yyyy-MM-dd hh:mm:ss", columnName = "authorisation_date")
    LocalDate authorisationDate;

    @DataField(pos = 5,  pattern = "yyyy-MM-dd hh:mm:ss", columnName = "extracted_date")
    String extractedDate;
}
