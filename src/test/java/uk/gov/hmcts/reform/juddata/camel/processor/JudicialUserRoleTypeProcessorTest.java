package uk.gov.hmcts.reform.juddata.camel.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserRoleType;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Before;
import org.junit.Test;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialContractType;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserRoleType;
import uk.gov.hmcts.reform.juddata.camel.validator.JsrValidatorInitializer;

public class JudicialUserRoleTypeProcessorTest {

    JudicialUserRoleTypeProcessor judicialUserRoleTypeProcessor = new JudicialUserRoleTypeProcessor();

    JsrValidatorInitializer<JudicialUserRoleType> judicialUserRoleTypeJsrValidatorInitializer;

    private Validator validator;

    @Before
    public void setup() {

        judicialUserRoleTypeJsrValidatorInitializer
                = new JsrValidatorInitializer<>();

        setField(judicialUserRoleTypeProcessor,
                "judicialUserRoleTypeJsrValidatorInitializer", judicialUserRoleTypeJsrValidatorInitializer);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        setField(judicialUserRoleTypeJsrValidatorInitializer, "validator", validator);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testProcess() throws Exception {
        List<JudicialUserRoleType> judicialUserRoleTypes = new ArrayList<>();
        JudicialUserRoleType judicialUserRoleTypeMock1 = createJudicialUserRoleType();
        JudicialUserRoleType judicialUserRoleTypeMock2 = createJudicialUserRoleType();
        judicialUserRoleTypes.add(judicialUserRoleTypeMock1);
        judicialUserRoleTypes.add(judicialUserRoleTypeMock2);


        Exchange exchangeMock = mock(Exchange.class);
        Message messageMock = mock(Message.class);
        when(exchangeMock.getIn()).thenReturn(messageMock);
        when(exchangeMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getBody()).thenReturn(judicialUserRoleTypes);
        judicialUserRoleTypeProcessor.process(exchangeMock);

        assertThat(((List)exchangeMock.getMessage().getBody()).size()).isEqualTo(2);
        assertThat(((List<JudicialContractType>)exchangeMock.getMessage().getBody())).isSameAs(judicialUserRoleTypes);
    }
} 
