package uk.gov.hmcts.reform.juddata.camel.elinks.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.elinks.domain.ElinkDataSchedularAudit;
import uk.gov.hmcts.reform.elinks.repository.ElinkSchedularAuditRepository;
import uk.gov.hmcts.reform.elinks.util.ElinkDataIngestionSchedularAudit;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;


class ElinkDataIngestionSchedularAuditTest {

    @Mock
    private ElinkSchedularAuditRepository elinkSchedularAuditRepository;


    @InjectMocks
    ElinkDataIngestionSchedularAudit elinkDataIngestionSchedularAudit;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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

        when(elinkSchedularAuditRepository.save(any())).thenReturn(schedularAudit);
        elinkDataIngestionSchedularAudit.auditSchedulerStatus("Test User",
            LocalDateTime.now(), LocalDateTime.now(), "SUCCESS", "BaseLocations");

        verify(elinkSchedularAuditRepository, times(1))
            .save(any());

    }


}
