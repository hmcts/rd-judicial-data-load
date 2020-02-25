package uk.gov.hmcts.reform.juddata.camel.mapper;

import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.getCurrentTimeStamp;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.getDateTimeStamp;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialOfficeAuthorisation;

@Slf4j
@Component
public class JudicialOfficeAuthorisationRowMapper {

    private static int seqNumber = 0;

    public Map<String, Object> getMap(JudicialOfficeAuthorisation judicialOfficeAuthorisation) {

        Map<String, Object> judOfficeAuthorisationRow = new HashMap<>();

        judOfficeAuthorisationRow.put("judicial_office_auth_id", generateId());
        judOfficeAuthorisationRow.put("elinks_id", judicialOfficeAuthorisation.getElinksId());
        judOfficeAuthorisationRow.put("authorisation_id", judicialOfficeAuthorisation.getAuthorisationId());
        judOfficeAuthorisationRow.put("jurisdiction_id", judicialOfficeAuthorisation.getJurisdictionId());
        judOfficeAuthorisationRow.put("authorisation_date", judicialOfficeAuthorisation.getAuthorisationDate());
        judOfficeAuthorisationRow.put("extracted_date", getDateTimeStamp(judicialOfficeAuthorisation.getExtractedDate()));
        judOfficeAuthorisationRow.put("created_date", getCurrentTimeStamp());
        judOfficeAuthorisationRow.put("last_loaded_date", getCurrentTimeStamp());
        return  judOfficeAuthorisationRow;
    }

    private int generateId() {
        seqNumber = seqNumber + 1;
        return seqNumber;
    }

}
