package uk.gov.hmcts.reform.juddata.camel.elinks.domain;


import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.elinks.domain.ElinkDataSchedularAudit;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class ElinkDataSchedularAuditTest {

    @Test
    void testSchedularAudit() {
        ElinkDataSchedularAudit schedularAudit = new ElinkDataSchedularAudit();
        schedularAudit.setId(1);
        schedularAudit.setSchedulerName("Test User");
        schedularAudit.setSchedulerStartTime(LocalDateTime.now());
        schedularAudit.setSchedulerEndTime(LocalDateTime.now());
        schedularAudit.setStatus("SUCCESS");
        schedularAudit.setApiName("BaseLocations");

        assertNotNull(schedularAudit);
        assertThat(schedularAudit.getId(), is(1));
        assertThat(schedularAudit.getSchedulerName(), is("Test User"));
        assertNotNull(schedularAudit.getSchedulerStartTime());
        assertNotNull(schedularAudit.getSchedulerEndTime());
        assertThat(schedularAudit.getStatus(), is("SUCCESS"));
        assertThat(schedularAudit.getApiName(), is("BaseLocations"));
    }

}
