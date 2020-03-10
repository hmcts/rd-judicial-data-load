package uk.gov.hmcts.reform.juddata.camel.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper.createJudicialUserProfileMock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Test;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialUserProfile;

public class JudicialUserProfileProcessorTest {

    private LocalDate localDate = LocalDate.now();
    private JudicialUserProfile judicialUserProfileMock1 = createJudicialUserProfileMock(localDate);
    private JudicialUserProfile judicialUserProfileMock2 = createJudicialUserProfileMock(localDate);

    @Test
    public void should_return_JudicialOfficeAuthorisationRow_response() {

        List<JudicialUserProfile> judicialUserProfiles = new ArrayList<>();
        judicialUserProfiles.add(judicialUserProfileMock1);
        judicialUserProfiles.add(judicialUserProfileMock2);

        Exchange exchangeMock = mock(Exchange.class);
        Message messageMock = mock(Message.class);
        when(exchangeMock.getIn()).thenReturn(messageMock);
        when(exchangeMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getBody()).thenReturn(judicialUserProfiles);

        JudicialUserProfileProcessor judicialUserProfileProcessor = new JudicialUserProfileProcessor();
        judicialUserProfileProcessor.process(exchangeMock);

        assertThat(((List)exchangeMock.getMessage().getBody()).size()).isEqualTo(2);
    }

    @Test
    public void should_return_JudicialOfficeAuthorisationRow_with_single_record_response() {

        Exchange exchangeMock = mock(Exchange.class);
        Message messageMock = mock(Message.class);
        when(exchangeMock.getIn()).thenReturn(messageMock);
        when(exchangeMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getBody()).thenReturn(judicialUserProfileMock1);

        JudicialUserProfileProcessor judicialUserProfileProcessor = new JudicialUserProfileProcessor();
        judicialUserProfileProcessor.process(exchangeMock);

        assertThat(((JudicialUserProfile)exchangeMock.getMessage().getBody())).isSameAs(judicialUserProfileMock1);
    }

    @Test
    public void should_return_JudicialOfficeAuthorisationRow_with_single_record_with_elinks_id_nullresponse() {

        judicialUserProfileMock1.setElinksId(null);

        Exchange exchangeMock = mock(Exchange.class);
        Message messageMock = mock(Message.class);
        when(exchangeMock.getIn()).thenReturn(messageMock);
        when(exchangeMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getBody()).thenReturn(judicialUserProfileMock1);

        JudicialUserProfileProcessor judicialUserProfileProcessor = new JudicialUserProfileProcessor();
        judicialUserProfileProcessor.process(exchangeMock);

        assertThat(((JudicialUserProfile)exchangeMock.getMessage().getBody())).isSameAs(judicialUserProfileMock1);
    }
}
