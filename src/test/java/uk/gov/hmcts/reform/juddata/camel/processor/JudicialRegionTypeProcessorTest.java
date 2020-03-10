package uk.gov.hmcts.reform.juddata.camel.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper.createJudicialRegionType;

import java.util.ArrayList;
import java.util.List;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialContractType;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialRegionType;

public class JudicialRegionTypeProcessorTest {

    JudicialRegionTypeProcessor judicialRegionTypeProcessor = new JudicialRegionTypeProcessor();

    List<JudicialRegionType> judicialRegionTypes = new ArrayList<>();
    JudicialRegionType judicialRegionType1 = createJudicialRegionType();
    JudicialRegionType judicialRegionType2 = createJudicialRegionType();

    @Test
    @SuppressWarnings("unchecked")
    public void testProcess() throws Exception {

        judicialRegionTypes.add(judicialRegionType1);
        judicialRegionTypes.add(judicialRegionType2);

        Exchange exchangeMock = mock(Exchange.class);
        Message messageMock = mock(Message.class);
        when(exchangeMock.getIn()).thenReturn(messageMock);
        when(exchangeMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getBody()).thenReturn(judicialRegionTypes);
        judicialRegionTypeProcessor.process(exchangeMock);

        assertThat(((List) exchangeMock.getMessage().getBody()).size()).isEqualTo(2);
        assertThat(((List<JudicialContractType>) exchangeMock.getMessage().getBody())).isSameAs(judicialRegionTypes);
    }

} 
