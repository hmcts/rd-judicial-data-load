package uk.gov.hmcts.reform.juddata.camel.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper.createJudicialContractType;

import java.util.ArrayList;
import java.util.List;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialContractType;

public class JudicialContractTypeProcessorTest {

    JudicialContractTypeProcessor judicialContractTypeProcessor = new JudicialContractTypeProcessor();
    List<JudicialContractType> judicialContractTypes = new ArrayList<JudicialContractType>();
    JudicialContractType judicialContractTypeMock1 = createJudicialContractType();
    JudicialContractType judicialContractTypeMock2 = createJudicialContractType();

    @Test
    @SuppressWarnings("unchecked")
    public void testProcess() throws Exception {

        judicialContractTypes.add(judicialContractTypeMock1);
        judicialContractTypes.add(judicialContractTypeMock2);

        Exchange exchangeMock = mock(Exchange.class);
        Message messageMock = mock(Message.class);
        when(exchangeMock.getIn()).thenReturn(messageMock);
        when(exchangeMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getBody()).thenReturn(judicialContractTypes);
        judicialContractTypeProcessor.process(exchangeMock);

        assertThat(((List)exchangeMock.getMessage().getBody()).size()).isEqualTo(2);
        assertThat(((List<JudicialContractType>)exchangeMock.getMessage().getBody())).isSameAs(judicialContractTypes);
    }
} 
