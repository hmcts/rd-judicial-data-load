package uk.gov.hmcts.reform.juddata.camel.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialRegionType;

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
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialRegionType;
import uk.gov.hmcts.reform.juddata.camel.validator.JsrValidatorInitializer;

public class JudicialRegionTypeProcessorTest {

    JudicialRegionTypeProcessor judicialRegionTypeProcessor = new JudicialRegionTypeProcessor();

    List<JudicialRegionType> judicialRegionTypes = new ArrayList<>();

    JudicialRegionType judicialRegionType1 = createJudicialRegionType();

    JudicialRegionType judicialRegionType2 = createJudicialRegionType();

    JsrValidatorInitializer<JudicialRegionType> judicialRegionTypeJsrValidatorInitializer;

    private Validator validator;

    @Before
    public void setup() {

        judicialRegionTypeJsrValidatorInitializer
                = new JsrValidatorInitializer<>();

        setField(judicialRegionTypeProcessor,
                "judicialRegionTypeJsrValidatorInitializer", judicialRegionTypeJsrValidatorInitializer);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        setField(judicialRegionTypeJsrValidatorInitializer, "validator", validator);
    }

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
