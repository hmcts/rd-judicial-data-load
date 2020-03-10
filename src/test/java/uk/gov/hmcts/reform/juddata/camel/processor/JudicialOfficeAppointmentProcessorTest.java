package uk.gov.hmcts.reform.juddata.camel.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper.createCurrentLocalDate;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper.createJudicialOfficeAppointmentMockMock;

import java.util.ArrayList;
import java.util.List;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;

public class JudicialOfficeAppointmentProcessorTest {

    JudicialOfficeAppointment judicialOfficeAppointmentMock1 = createJudicialOfficeAppointmentMockMock(createCurrentLocalDate());
    JudicialOfficeAppointment judicialOfficeAppointmentMock2 = createJudicialOfficeAppointmentMockMock(createCurrentLocalDate());
    JudicialOfficeAppointmentProcessor judicialOfficeAppointmentProcessor = new JudicialOfficeAppointmentProcessor();

    @Test
    @SuppressWarnings("unchecked")
    public void should_return_JudicialOfficeAppointmentRow_response() {

        List<JudicialOfficeAppointment> judicialOfficeAppointments = new ArrayList<>();
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock1);
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock2);

        Exchange exchangeMock = mock(Exchange.class);
        Message messageMock = mock(Message.class);
        when(exchangeMock.getIn()).thenReturn(messageMock);
        when(exchangeMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getBody()).thenReturn(judicialOfficeAppointments);

        judicialOfficeAppointmentProcessor.process(exchangeMock);
        assertThat(((List)exchangeMock.getMessage().getBody()).size()).isEqualTo(2);
        assertThat(((List<JudicialOfficeAppointment>)exchangeMock.getMessage().getBody())).isSameAs(judicialOfficeAppointments);
    }

    @Test
    public void should_return_JudicialOfficeAppointmentRow_with_single_record_response() {

        Exchange exchangeMock = mock(Exchange.class);
        Message messageMock = mock(Message.class);
        when(exchangeMock.getIn()).thenReturn(messageMock);
        when(exchangeMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getBody()).thenReturn(judicialOfficeAppointmentMock1);

        judicialOfficeAppointmentProcessor.process(exchangeMock);
        assertThat(((JudicialOfficeAppointment)exchangeMock.getMessage().getBody())).isSameAs(judicialOfficeAppointmentMock1);
    }

    @Test
    public void should_return_JudicialOfficeAppointmentRow_with_single_record_with_elinks_id_nullresponse() {

        judicialOfficeAppointmentMock1.setElinksId(null);
        Exchange exchangeMock = mock(Exchange.class);
        Message messageMock = mock(Message.class);
        when(exchangeMock.getIn()).thenReturn(messageMock);
        when(exchangeMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getBody()).thenReturn(judicialOfficeAppointmentMock1);

        judicialOfficeAppointmentProcessor.process(exchangeMock);
        assertThat(((JudicialOfficeAppointment)exchangeMock.getMessage().getBody())).isSameAs(judicialOfficeAppointmentMock1);
    }
}
