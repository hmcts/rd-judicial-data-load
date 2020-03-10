package uk.gov.hmcts.reform.juddata.camel.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper.createJudicialOfficeAppointmentMock;

import java.util.ArrayList;
import java.util.List;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialBaseLocationType;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialContractType;

public class JudicialBaseLocationProcessorTest {

    JudicialBaseLocationProcessor judicialBaseLocationProcessor = new JudicialBaseLocationProcessor();

    List<JudicialBaseLocationType> judicialBaseLocationTypes = new ArrayList<>();
    JudicialBaseLocationType judicialBaseLocationType1 = createJudicialOfficeAppointmentMock();
    JudicialBaseLocationType judicialBaseLocationType2 = createJudicialOfficeAppointmentMock();

    @Test
    @SuppressWarnings("unchecked")
    public void testProcess() throws Exception {

        judicialBaseLocationTypes.add(judicialBaseLocationType1);
        judicialBaseLocationTypes.add(judicialBaseLocationType2);

        Exchange exchangeMock = mock(Exchange.class);
        Message messageMock = mock(Message.class);
        when(exchangeMock.getIn()).thenReturn(messageMock);
        when(exchangeMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getBody()).thenReturn(judicialBaseLocationTypes);
        judicialBaseLocationProcessor.process(exchangeMock);

        assertThat(((List) exchangeMock.getMessage().getBody()).size()).isEqualTo(2);
        assertThat(((List<JudicialContractType>) exchangeMock.getMessage().getBody())).isSameAs(judicialBaseLocationTypes);

    }

} 
