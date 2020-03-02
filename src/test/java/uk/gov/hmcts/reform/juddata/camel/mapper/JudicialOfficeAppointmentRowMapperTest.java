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

        assertEquals(1, response.get("judicial_office_appointment_id"));
        assertEquals("elinksid_1", response.get("elinks_id"));
        assertEquals("roleId_1", response.get("role_id"));
        assertEquals("contractTypeId_1", response.get("contract_type_id"));
        assertEquals("baseLocationId_1",response.get("base_location_id"));
        assertEquals("regionId_1", response.get("region_id"));
        assertEquals(true, response.get("is_prinicple_appointment"));
        assertEquals(LocalDate.parse(currentDateString, getDateFormatter()), response.get("start_date"));
        assertEquals(LocalDate.parse(currentDateString, getDateFormatter()), response.get("end_date"));
        assertEquals(true, response.get("active_flag"));
        assertEquals(getDateTimeStamp(judicialOfficeAppointmentMock.getExtractedDate()), response.get("extracted_date"));
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


