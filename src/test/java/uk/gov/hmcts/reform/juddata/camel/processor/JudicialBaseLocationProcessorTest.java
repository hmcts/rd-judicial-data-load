package uk.gov.hmcts.reform.juddata.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spi.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.RouteProperties;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.JsrValidatorInitializer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialBaseLocationType;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.ROUTE_DETAILS;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialOfficeAppointmentMock;

class JudicialBaseLocationProcessorTest {

    JudicialBaseLocationProcessor judicialBaseLocationProcessor = spy(new JudicialBaseLocationProcessor());

    List<JudicialBaseLocationType> judicialBaseLocationTypes = new ArrayList<>();

    JudicialBaseLocationType judicialBaseLocationType1 = createJudicialOfficeAppointmentMock();

    JudicialBaseLocationType judicialBaseLocationType2 = createJudicialOfficeAppointmentMock();

    JsrValidatorInitializer<JudicialBaseLocationType> judicialBaseLocationTypeJsrValidatorInitializer;

    private Validator validator;

    Exchange exchangeMock;
    Message messageMock;
    Registry registryMock;
    ApplicationContext applicationContext = mock(ConfigurableApplicationContext.class);
    ConfigurableListableBeanFactory configurableListableBeanFactory = mock(ConfigurableListableBeanFactory.class);


    @BeforeEach
    public void setup() {

        judicialBaseLocationTypeJsrValidatorInitializer
            = new JsrValidatorInitializer<>();

        setField(judicialBaseLocationProcessor,
            "judicialBaseLocationTypeJsrValidatorInitializer", judicialBaseLocationTypeJsrValidatorInitializer);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        setField(judicialBaseLocationTypeJsrValidatorInitializer, "validator", validator);

        messageMock = mock(Message.class);
        RouteProperties routeProperties = new RouteProperties();
        routeProperties.setFileName("test");
        exchangeMock = mock(Exchange.class);
        registryMock = mock(Registry.class);
        when(exchangeMock.getIn()).thenReturn(messageMock);
        when(exchangeMock.getIn().getHeader(ROUTE_DETAILS)).thenReturn(routeProperties);
        when(exchangeMock.getContext()).thenReturn(new DefaultCamelContext());
        when(exchangeMock.getMessage()).thenReturn(messageMock);
        setField(judicialBaseLocationProcessor, "applicationContext", applicationContext);
        when(((ConfigurableApplicationContext)
            applicationContext).getBeanFactory()).thenReturn(configurableListableBeanFactory);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testProcess() throws Exception {

        judicialBaseLocationTypes.add(judicialBaseLocationType1);
        judicialBaseLocationTypes.add(judicialBaseLocationType2);

        when(messageMock.getBody()).thenReturn(judicialBaseLocationTypes);
        judicialBaseLocationProcessor.process(exchangeMock);

        assertThat(((List) exchangeMock.getMessage().getBody()).size()).isEqualTo(2);
        verify(judicialBaseLocationProcessor).audit(judicialBaseLocationTypeJsrValidatorInitializer,exchangeMock);
        verify(messageMock).setBody(any());
        verify(exchangeMock, times(2)).getMessage();
    }
} 
