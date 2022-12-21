package uk.gov.hmcts.reform.juddata.camel.elinks.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @InjectMocks
    ElinkDataIngestionSchedularAudit elinkDataIngestionSchedularAudit;
    @Mock
    private ElinkSchedularAuditRepository elinkSchedularAuditRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @CsvSource(value = {"Location:SUCCESS", "BaseLocation:SUCCESS", "People:SUCCESS"}, delimiter = ':')
    void testSaveScheduleSuccessAudit(String apiName, String status) {
        ElinkDataSchedularAudit schedularAudit = new ElinkDataSchedularAudit();
        schedularAudit.setId(1);
        schedularAudit.setSchedulerName("Test User");
        schedularAudit.setSchedulerStartTime(LocalDateTime.now());
        schedularAudit.setSchedulerEndTime(LocalDateTime.now());
        schedularAudit.setStatus(status);
        schedularAudit.setApiName(apiName);

        when(elinkSchedularAuditRepository.save(any())).thenReturn(schedularAudit);
        elinkDataIngestionSchedularAudit.auditSchedulerStatus("Test User",
            LocalDateTime.now(), LocalDateTime.now(), schedularAudit.getStatus(), schedularAudit.getApiName());

        verify(elinkSchedularAuditRepository, times(1))
            .save(any());

    }


    @ParameterizedTest
    @CsvSource(value = {"Location:FAILURE", "BaseLocation:FAILURE", "People:FAILURE"}, delimiter = ':')
    void testSaveScheduleFailureAudit(String apiName, String status) {
        ElinkDataSchedularAudit schedularAudit = new ElinkDataSchedularAudit();
        schedularAudit.setId(1);
        schedularAudit.setSchedulerName("Test User");
        schedularAudit.setSchedulerStartTime(LocalDateTime.now());
        schedularAudit.setSchedulerEndTime(LocalDateTime.now().plusHours(1));
        schedularAudit.setStatus(status);
        schedularAudit.setApiName(apiName);

        when(elinkSchedularAuditRepository.save(any())).thenReturn(schedularAudit);
        elinkDataIngestionSchedularAudit.auditSchedulerStatus("Test User",
            LocalDateTime.now(), LocalDateTime.now().plusHours(1),
            schedularAudit.getStatus(), schedularAudit.getApiName());

        verify(elinkSchedularAuditRepository, times(1))
            .save(any());

    }


}
