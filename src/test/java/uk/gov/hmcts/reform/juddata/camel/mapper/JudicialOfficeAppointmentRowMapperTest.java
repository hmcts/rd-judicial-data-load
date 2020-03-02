package uk.gov.hmcts.reform.juddata.camel.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper.createCurrentLocalDate;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper.createJudicialOfficeAppointmentMockMock;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper.getDateFormatter;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.getDateTimeStamp;

import java.time.LocalDate;
import java.util.Map;
import org.junit.Test;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialOfficeAppointment;

public class JudicialOfficeAppointmentRowMapperTest {

    JudicialOfficeAppointmentRowMapper judicialOfficeAppointmentRowMapper = new JudicialOfficeAppointmentRowMapper();

    @Test
    public void should_return_JudicialOfficeAppointmentRowMapper_response() {


        String currentDateString = createCurrentLocalDate();
        JudicialOfficeAppointment judicialOfficeAppointmentMock = createJudicialOfficeAppointmentMockMock(currentDateString);
        Map<String, Object> response = judicialOfficeAppointmentRowMapper.getMap(judicialOfficeAppointmentMock);

        assertEquals(response.get("judicial_office_appointment_id"),1);
        assertEquals(response.get("elinks_id"), "elinksid_1");
        assertEquals(response.get("role_id"), "roleId_1");
        assertEquals(response.get("contract_type_id"),"contractTypeId_1");
        assertEquals(response.get("base_location_id"), "baseLocationId_1");
        assertEquals(response.get("region_id"), "regionId_1");
        assertEquals(response.get("is_prinicple_appointment"), true);
        assertEquals(response.get("start_date"), LocalDate.parse(currentDateString, getDateFormatter()));
        assertEquals(response.get("end_date"), LocalDate.parse(currentDateString, getDateFormatter()));
        assertEquals(response.get("active_flag"), true);
        assertEquals(response.get("extracted_date"), getDateTimeStamp(judicialOfficeAppointmentMock.getExtractedDate()));
        assertThat(response.get("created_date")).isNotNull();
        assertThat(response.get("last_loaded_date")).isNotNull();

    }

    @Test
    public void test_returnNullIfBlank_with_param_non_null() {
        String returnString = judicialOfficeAppointmentRowMapper.returnNullIfBlank("testString");
        assertThat(returnString).isNotBlank();
    }

    @Test
    public void test_returnNullIfBlank_with_param_null() {
        String returnString = judicialOfficeAppointmentRowMapper.returnNullIfBlank(null);
        assertThat(returnString).isNull();
    }
}


