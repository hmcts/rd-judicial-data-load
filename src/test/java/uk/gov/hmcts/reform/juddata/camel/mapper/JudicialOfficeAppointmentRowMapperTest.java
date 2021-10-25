package uk.gov.hmcts.reform.juddata.camel.mapper;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.DATE_FORMAT;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.PERID_1;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialOfficeAppointmentMock;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateTimeWithFormat;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateWithFormat;

class JudicialOfficeAppointmentRowMapperTest {

    JudicialOfficeAppointmentRowMapper judicialOfficeAppointmentRowMapper = new JudicialOfficeAppointmentRowMapper();

    @Test
    void should_return_JudicialOfficeAppointmentRowMapper_response() {

        Date currentDate = new Date();
        LocalDateTime dateTime = LocalDateTime.now();
        JudicialOfficeAppointment judicialOfficeAppointmentMock = createJudicialOfficeAppointmentMock(currentDate,
            dateTime, PERID_1);
        Map<String, Object> response = judicialOfficeAppointmentRowMapper.getMap(judicialOfficeAppointmentMock);

        assertEquals(1, response.get("judicial_office_appointment_id"));
        assertEquals(PERID_1, response.get("per_id"));
        assertEquals("baseLocationId_1", response.get("base_location_id"));
        assertEquals("regionId_1", response.get("region_id"));
        assertEquals(true, response.get("is_prinicple_appointment"));
        assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), response.get("start_date"));
        assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), response.get("end_date"));
        assertEquals(true, response.get("active_flag"));
        assertEquals(getDateTimeWithFormat(dateTime), response.get("extracted_date"));
        assertThat(response.get("created_date")).isNotNull();
        assertThat(response.get("last_loaded_date")).isNotNull();
        assertEquals("111", response.get("personal_code"));
        assertEquals("779321b3-3170-44a0-bc7d-b4decc2aea10", response.get("object_id"));
        assertEquals("Magistrate", response.get("appointment"));
        assertEquals("1", response.get("appointment_type"));
    }

    @Test
    void test_returnNullIfBlank_with_param_non_null() {
        String returnString = judicialOfficeAppointmentRowMapper.returnNullIfBlank("testString");
        assertThat(returnString).isNotBlank();
    }

    @Test
    void test_returnNullIfBlank_with_param_null() {
        String returnString = judicialOfficeAppointmentRowMapper.returnNullIfBlank(null);
        assertThat(returnString).isNull();
    }
}


