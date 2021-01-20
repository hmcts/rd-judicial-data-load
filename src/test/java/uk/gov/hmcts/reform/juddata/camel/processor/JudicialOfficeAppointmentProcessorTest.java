package uk.gov.hmcts.reform.juddata.camel.processor;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spi.Registry;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.RouteProperties;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.JsrValidatorInitializer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.ROUTE_DETAILS;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialOfficeAppointmentMockMock;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserProfileMock;

public class JudicialOfficeAppointmentProcessorTest {

    Date currentDate = new Date();

    LocalDateTime dateTime = LocalDateTime.now();

    JudicialOfficeAppointment judicialOfficeAppointmentMock1 = createJudicialOfficeAppointmentMockMock(currentDate,
        dateTime);

    JudicialOfficeAppointment judicialOfficeAppointmentMock2 = createJudicialOfficeAppointmentMockMock(currentDate,
        dateTime);

    JudicialOfficeAppointmentProcessor judicialOfficeAppointmentProcessor;

    private JsrValidatorInitializer<JudicialOfficeAppointment> judicialOfficeAppointmentJsrValidatorInitializer;


    private Validator validator;

    CamelContext camelContext = new DefaultCamelContext();

    JudicialUserProfileProcessor judicialUserProfileProcessor = new JudicialUserProfileProcessor();

    Exchange exchangeMock;
    Message messageMock;
    Registry registryMock;
    ApplicationContext applicationContext = mock(ConfigurableApplicationContext.class);
    ConfigurableListableBeanFactory configurableListableBeanFactory = mock(ConfigurableListableBeanFactory.class);

    @Before
    public void setup() {

        judicialOfficeAppointmentProcessor = new JudicialOfficeAppointmentProcessor();
        judicialOfficeAppointmentMock2.setElinksId("elinks_2");
        judicialOfficeAppointmentJsrValidatorInitializer
            = new JsrValidatorInitializer<>();
        setField(judicialOfficeAppointmentProcessor,
            "judicialOfficeAppointmentJsrValidatorInitializer", judicialOfficeAppointmentJsrValidatorInitializer);
        setField(judicialOfficeAppointmentProcessor, "judicialUserProfileProcessor",
            judicialUserProfileProcessor);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        setField(judicialOfficeAppointmentJsrValidatorInitializer, "validator", validator);

        messageMock = mock(Message.class);
        RouteProperties routeProperties = new RouteProperties();
        routeProperties.setFileName("test");
        exchangeMock = mock(Exchange.class);
        registryMock = mock(Registry.class);
        when(exchangeMock.getIn()).thenReturn(messageMock);
        when(exchangeMock.getIn().getHeader(ROUTE_DETAILS)).thenReturn(routeProperties);
        when(exchangeMock.getContext()).thenReturn(new DefaultCamelContext());
        when(exchangeMock.getMessage()).thenReturn(messageMock);
        setField(judicialOfficeAppointmentProcessor, "applicationContext", applicationContext);
        setField(judicialUserProfileProcessor, "applicationContext", applicationContext);
        when(((ConfigurableApplicationContext)
            applicationContext).getBeanFactory()).thenReturn(configurableListableBeanFactory);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_return_JudicialOfficeAppointmentRow_response() {

        List<JudicialOfficeAppointment> judicialOfficeAppointments = new ArrayList<>();
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock1);
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock2);

        when(messageMock.getBody()).thenReturn(judicialOfficeAppointments);
        judicialUserProfileProcessor = new JudicialUserProfileProcessor();
        judicialOfficeAppointmentProcessor.process(exchangeMock);
        assertThat(((List) exchangeMock.getMessage().getBody()).size()).isEqualTo(2);
        assertThat(((List<JudicialOfficeAppointment>) exchangeMock.getMessage().getBody()))
            .isSameAs(judicialOfficeAppointments);
        verify(exchangeMock, times(3)).getMessage();
    }

    @Test
    public void should_return_JudicialOfficeAppointmentRow_with_single_record_response() {

        when(messageMock.getBody()).thenReturn(judicialOfficeAppointmentMock1);

        judicialOfficeAppointmentProcessor.process(exchangeMock);
        assertThat(((JudicialOfficeAppointment) exchangeMock.getMessage().getBody()))
            .isSameAs(judicialOfficeAppointmentMock1);
    }

    @Test
    public void should_return_JudicialOfficeAppointmentRow_with_single_record_with_elinks_id_nullresponse() {

        judicialOfficeAppointmentMock1.setElinksId(null);

        final JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        final PlatformTransactionManager platformTransactionManager = mock(PlatformTransactionManager.class);
        final TransactionStatus transactionStatus = mock(TransactionStatus.class);

        when(messageMock.getBody()).thenReturn(judicialOfficeAppointmentMock1);
        RouteProperties routeProperties = new RouteProperties();
        routeProperties.setFileName("test");

        setField(judicialOfficeAppointmentProcessor, "jsrThresholdLimit", 5);
        setField(judicialOfficeAppointmentJsrValidatorInitializer, "camelContext", camelContext);
        setField(judicialOfficeAppointmentJsrValidatorInitializer, "jdbcTemplate", jdbcTemplate);
        setField(judicialOfficeAppointmentJsrValidatorInitializer,
            "platformTransactionManager", platformTransactionManager);

        int[][] intArray = new int[1][];
        when(jdbcTemplate.batchUpdate(anyString(), anyList(), anyInt(), any())).thenReturn(intArray);
        when(platformTransactionManager.getTransaction(any())).thenReturn(transactionStatus);
        when(exchangeMock.getContext()).thenReturn(new DefaultCamelContext());
        doNothing().when(platformTransactionManager).commit(transactionStatus);
        when(exchangeMock.getIn().getHeader(ROUTE_DETAILS)).thenReturn(routeProperties);
        judicialOfficeAppointmentProcessor.process(exchangeMock);
        assertThat(((JudicialOfficeAppointment) exchangeMock.getMessage().getBody()))
            .isSameAs(judicialOfficeAppointmentMock1);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_return_JudicialOfficeAppointmentRow_response_skip_invalidProfiles() {

        List<JudicialOfficeAppointment> judicialOfficeAppointments = new ArrayList<>();
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock1);
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock2);

        final JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        final PlatformTransactionManager platformTransactionManager = mock(PlatformTransactionManager.class);
        final TransactionStatus transactionStatus = mock(TransactionStatus.class);
        RouteProperties routeProperties = new RouteProperties();
        routeProperties.setFileName("test");

        when(messageMock.getBody()).thenReturn(judicialOfficeAppointments);
        judicialUserProfileProcessor = mock(JudicialUserProfileProcessor.class);

        setField(judicialOfficeAppointmentProcessor, "judicialUserProfileProcessor",
            judicialUserProfileProcessor);
        JudicialUserProfile judicialUserProfileMock = createJudicialUserProfileMock(currentDate, dateTime);
        JudicialUserProfile judicialUserProfileMock2 = createJudicialUserProfileMock(currentDate, dateTime);
        judicialUserProfileMock2.setElinksId("elinks_3");
        List<JudicialUserProfile> judicialUserProfiles = new ArrayList<>();
        judicialUserProfiles.add(judicialUserProfileMock);
        judicialUserProfiles.add(judicialUserProfileMock2);

        when(judicialUserProfileProcessor.getInvalidRecords()).thenReturn(judicialUserProfiles);
        setField(judicialOfficeAppointmentProcessor, "jsrThresholdLimit", 5);
        setField(judicialOfficeAppointmentJsrValidatorInitializer, "camelContext", camelContext);
        setField(judicialOfficeAppointmentJsrValidatorInitializer, "jdbcTemplate", jdbcTemplate);
        setField(judicialOfficeAppointmentJsrValidatorInitializer,
            "platformTransactionManager", platformTransactionManager);

        int[][] intArray = new int[1][];
        when(jdbcTemplate.batchUpdate(anyString(), anyList(), anyInt(), any())).thenReturn(intArray);
        when(platformTransactionManager.getTransaction(any())).thenReturn(transactionStatus);
        when(exchangeMock.getContext()).thenReturn(new DefaultCamelContext());
        doNothing().when(platformTransactionManager).commit(transactionStatus);
        when(exchangeMock.getIn().getHeader(ROUTE_DETAILS)).thenReturn(routeProperties);

        judicialOfficeAppointmentProcessor.process(exchangeMock);
        assertThat(((List) exchangeMock.getMessage().getBody()).size()).isEqualTo(2);
        judicialOfficeAppointments = new ArrayList<>();
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock2);
        assertThat(((List<JudicialOfficeAppointment>) exchangeMock.getMessage().getBody()))
            .containsAll(judicialOfficeAppointments);
    }
}
