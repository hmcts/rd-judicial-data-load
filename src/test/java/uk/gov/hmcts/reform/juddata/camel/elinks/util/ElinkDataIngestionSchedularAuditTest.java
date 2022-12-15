package uk.gov.hmcts.reform.juddata.camel.elinks.util;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import uk.gov.hmcts.reform.elinks.domain.ElinkDataSchedularAudit;
import uk.gov.hmcts.reform.elinks.repository.ElinkSchedularAuditRepository;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class ElinkDataIngestionSchedularAuditTest {

    @Mock
    private ElinkSchedularAuditRepository elinkSchedularAuditRepository;

    @InjectMocks
    ElinkDataSchedularAudit elinkDataSchedularAudit;

    @Test
    @SuppressWarnings("unchecked")
    void testSaveScheduleAudit() {
        ElinkDataSchedularAudit schedularAudit = new ElinkDataSchedularAudit();
        schedularAudit.setId(1);
        schedularAudit.setSchedulerName("Test User");
        schedularAudit.setSchedulerStartTime(LocalDateTime.now());
        schedularAudit.setSchedulerEndTime(LocalDateTime.now());
        schedularAudit.setStatus("SUCCESS");
        schedularAudit.setApiName("BaseLocations");

        //when(elinkSchedularAuditRepository.save(any())).thenReturn(schedularAudit);
        assertNotNull(schedularAudit);
        assertThat(schedularAudit.getId(), is(1));
        assertThat(schedularAudit.getSchedulerName(), is("Test User"));
        assertNotNull(schedularAudit.getSchedulerStartTime());
        assertNotNull(schedularAudit.getSchedulerEndTime());
        assertThat(schedularAudit.getStatus(), is("SUCCESS"));
        assertThat(schedularAudit.getApiName(), is("BaseLocations"));

    }


}
