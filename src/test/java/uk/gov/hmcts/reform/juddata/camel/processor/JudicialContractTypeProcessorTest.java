package uk.gov.hmcts.reform.juddata.camel.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialContractType;

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
import uk.gov.hmcts.reform.juddata.camel.validator.JsrValidatorInitializer;

public class JudicialContractTypeProcessorTest {

    JudicialContractTypeProcessor judicialContractTypeProcessor = new JudicialContractTypeProcessor();

    List<JudicialContractType> judicialContractTypes = new ArrayList<>();

    JudicialContractType judicialContractTypeMock1 = createJudicialContractType();

    JudicialContractType judicialContractTypeMock2 = createJudicialContractType();

    JsrValidatorInitializer<JudicialContractType> judicialContractTypeJsrValidatorInitializer;

    private Validator validator;

    @Before
    public void setup() {

        judicialContractTypeJsrValidatorInitializer
                = new JsrValidatorInitializer<>();

        setField(judicialContractTypeProcessor,
                "judicialContractTypeJsrValidatorInitializer", judicialContractTypeJsrValidatorInitializer);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        setField(judicialContractTypeJsrValidatorInitializer, "validator", validator);
    }

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
