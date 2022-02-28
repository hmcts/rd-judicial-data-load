package uk.gov.hmcts.reform.juddata.camel.binder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.DATE_FORMAT;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.PERID_1;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialOfficeAppointmentMock;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateTimeWithFormat;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.getDateWithFormat;

class JudicialOfficeAppointmentTest {

    @Test
    void test_objects_JudicialOfficeAppointment_correctly() {
        Date currentDate = new Date();
        LocalDateTime dateTime = LocalDateTime.now();
        JudicialOfficeAppointment judicialOfficeAppointment = createJudicialOfficeAppointmentMock(currentDate,
            dateTime, PERID_1);
        Assertions.assertEquals(PERID_1, judicialOfficeAppointment.getPerId());
        Assertions.assertEquals("baseLocationId_1", judicialOfficeAppointment.getBaseLocationId());
        Assertions.assertEquals("regionId_1", judicialOfficeAppointment.getRegionId());
        Assertions.assertEquals("111", judicialOfficeAppointment.getPersonalCode());
        Assertions.assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), judicialOfficeAppointment.getStartDate());
        Assertions.assertEquals(getDateWithFormat(currentDate, DATE_FORMAT), judicialOfficeAppointment.getEndDate());
        Assertions.assertEquals(getDateTimeWithFormat(dateTime), judicialOfficeAppointment.getExtractedDate());
        Assertions.assertTrue(judicialOfficeAppointment.getIsPrincipalAppointment());
        Assertions.assertTrue(judicialOfficeAppointment.isActiveFlag());

        judicialOfficeAppointment.setActiveFlag(false);
        judicialOfficeAppointment.setIsPrincipalAppointment(false);
        Assertions.assertFalse(judicialOfficeAppointment.isActiveFlag());
        Assertions.assertFalse(judicialOfficeAppointment.getIsPrincipalAppointment());
        Assertions.assertEquals("779321b3-3170-44a0-bc7d-b4decc2aea10", judicialOfficeAppointment.getObjectId());
        Assertions.assertEquals("Magistrate", judicialOfficeAppointment.getAppointment());
        Assertions.assertEquals("1", judicialOfficeAppointment.getAppointmentType());

    }
}
