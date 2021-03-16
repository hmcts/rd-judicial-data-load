package uk.gov.hmcts.reform.juddata.camel.processor;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spi.Registry;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;
import org.testcontainers.shaded.com.google.common.collect.ImmutableSet;
import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.RouteProperties;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.JsrValidatorInitializer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.springframework.test.util.ReflectionTestUtils.invokeMethod;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.ROUTE_DETAILS;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.ELINKSID_1;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.ELINKSID_2;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.ELINKSID_3;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.ELINKSID_4;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialOfficeAppointmentMock;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserProfileMock;

@PrepareForTest(JudicialOfficeAppointmentProcessor.class)
class JudicialOfficeAppointmentProcessorTest {

    Date currentDate = new Date();

    LocalDateTime dateTime = LocalDateTime.now();

    JudicialOfficeAppointment judicialOfficeAppointmentMock1 = createJudicialOfficeAppointmentMock(currentDate,
        dateTime, ELINKSID_1);


    JudicialOfficeAppointment judicialOfficeAppointmentMock2 = createJudicialOfficeAppointmentMock(currentDate,
        dateTime, ELINKSID_2);


    JudicialOfficeAppointment judicialOfficeAppointmentMock3 = createJudicialOfficeAppointmentMock(currentDate,
        dateTime, ELINKSID_3);

    JudicialOfficeAppointment judicialOfficeAppointmentMock4 = createJudicialOfficeAppointmentMock(currentDate,
        dateTime, ELINKSID_4);

    JudicialOfficeAppointmentProcessor judicialOfficeAppointmentProcessor;

    private JsrValidatorInitializer<JudicialOfficeAppointment> judicialOfficeAppointmentJsrValidatorInitializer;


    private Validator validator;

    CamelContext camelContext = new DefaultCamelContext();

    JudicialUserProfileProcessor judicialUserProfileProcessor = spy(new JudicialUserProfileProcessor());

    Exchange exchangeMock = spy(new DefaultExchange(camelContext));

    Message messageMock;

    Registry registryMock;

    ApplicationContext applicationContext = mock(ConfigurableApplicationContext.class);

    ConfigurableListableBeanFactory configurableListableBeanFactory = mock(ConfigurableListableBeanFactory.class);

    final JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

    final PlatformTransactionManager platformTransactionManager = mock(PlatformTransactionManager.class);

    final TransactionStatus transactionStatus = mock(TransactionStatus.class);

    @BeforeEach
    public void setup() {

        judicialOfficeAppointmentProcessor = spy(new JudicialOfficeAppointmentProcessor());
        judicialOfficeAppointmentMock3.setRegionId("0");
        judicialOfficeAppointmentMock3.setRoleId("0");
        judicialOfficeAppointmentMock3.setBaseLocationId("0");
        judicialOfficeAppointmentMock3.setContractType("0");
        judicialOfficeAppointmentJsrValidatorInitializer = spy(new JsrValidatorInitializer<>());

        setField(judicialOfficeAppointmentProcessor,
            "judicialOfficeAppointmentJsrValidatorInitializer", judicialOfficeAppointmentJsrValidatorInitializer);
        setField(judicialOfficeAppointmentProcessor, "judicialUserProfileProcessor",
            judicialUserProfileProcessor);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        setField(judicialOfficeAppointmentJsrValidatorInitializer, "validator", validator);
        setField(judicialOfficeAppointmentJsrValidatorInitializer, "camelContext", camelContext);
        setField(judicialOfficeAppointmentJsrValidatorInitializer, "jdbcTemplate", jdbcTemplate);
        setField(judicialOfficeAppointmentJsrValidatorInitializer, "platformTransactionManager",
            platformTransactionManager);

        messageMock = mock(Message.class);
        RouteProperties routeProperties = new RouteProperties();
        routeProperties.setFileName("test");
        registryMock = mock(Registry.class);
        when(exchangeMock.getIn()).thenReturn(messageMock);
        when(exchangeMock.getIn().getHeader(ROUTE_DETAILS)).thenReturn(routeProperties);
        when(exchangeMock.getContext()).thenReturn(new DefaultCamelContext());
        when(exchangeMock.getMessage()).thenReturn(messageMock);
        setField(judicialOfficeAppointmentProcessor, "applicationContext", applicationContext);
        setField(judicialUserProfileProcessor, "applicationContext", applicationContext);
        when(((ConfigurableApplicationContext)
            applicationContext).getBeanFactory()).thenReturn(configurableListableBeanFactory);
        when(judicialUserProfileProcessor.getValidElinksInUserProfile()).thenReturn(ImmutableSet.of(ELINKSID_1,
            ELINKSID_2, "invalid"));
        int[][] intArray = new int[1][];
        when(jdbcTemplate.batchUpdate(anyString(), anyList(), anyInt(), any())).thenReturn(intArray);
        setField(judicialOfficeAppointmentProcessor, "fetchRoles", "roles");
        setField(judicialOfficeAppointmentProcessor, "fetchLocations", "locations");
        setField(judicialOfficeAppointmentProcessor, "fetchContracts", "contracts");
        setField(judicialOfficeAppointmentProcessor, "fetchBaseLocations", "baseLocations");

        setField(judicialOfficeAppointmentProcessor, "jdbcTemplate", jdbcTemplate);

        when(jdbcTemplate.queryForList("roles", String.class)).thenReturn(ImmutableList.of("roleId_1",
            "roleId_2"));
        when(jdbcTemplate.queryForList("locations", String.class)).thenReturn(ImmutableList.of("regionId_1",
            "regionId_2"));
        when(jdbcTemplate.queryForList("contracts", String.class)).thenReturn(ImmutableList.of("contractTypeId_1",
            "contractTypeId_2"));
        when(jdbcTemplate.queryForList("baseLocations", String.class)).thenReturn(ImmutableList.of(
            "baseLocationId_1", "baseLocationId_2"));
        when(platformTransactionManager.getTransaction(any())).thenReturn(transactionStatus);
        doNothing().when(platformTransactionManager).commit(transactionStatus);
    }

    @Test
    @SuppressWarnings("unchecked")
    void should_return_JudicialOfficeAppointmentRow_response() throws Exception {

        List<JudicialOfficeAppointment> judicialOfficeAppointments = new ArrayList<>();
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock1);
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock2);
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock3);

        when(messageMock.getBody()).thenReturn(judicialOfficeAppointments);
        judicialUserProfileProcessor = new JudicialUserProfileProcessor();
        judicialOfficeAppointmentProcessor.process(exchangeMock);
        assertThat(((List) exchangeMock.getMessage().getBody()).size()).isEqualTo(3);
        assertThat(((List<JudicialOfficeAppointment>) exchangeMock.getMessage().getBody()))
            .isSameAs(judicialOfficeAppointments);
        verify(judicialOfficeAppointmentProcessor).filterInvalidUserProfileRecords(any(), any(), any(), any(), any());
        verify(judicialOfficeAppointmentJsrValidatorInitializer)
            .auditJsrExceptions(anyList(), anyString(), anyString(), any());
        verify(judicialOfficeAppointmentProcessor).audit(any(), any());
        verify(messageMock).setBody(any());
        verify(exchangeMock, times(4)).getMessage();
    }

    @Test
    void should_return_JudicialOfficeAppointmentRow_with_single_record_response() {

        when(messageMock.getBody()).thenReturn(judicialOfficeAppointmentMock1);

        judicialOfficeAppointmentProcessor.process(exchangeMock);
        assertThat(((JudicialOfficeAppointment) exchangeMock.getMessage().getBody()))
            .isSameAs(judicialOfficeAppointmentMock1);
    }

    @Test
    void should_return_JudicialOfficeAppointmentRow_with_single_record_with_elinks_id_nullresponse() {

        when(messageMock.getBody()).thenReturn(judicialOfficeAppointmentMock1);

        setField(judicialOfficeAppointmentProcessor, "jsrThresholdLimit", 5);
        setField(judicialOfficeAppointmentJsrValidatorInitializer, "camelContext", camelContext);
        setField(judicialOfficeAppointmentJsrValidatorInitializer, "jdbcTemplate", jdbcTemplate);
        setField(judicialOfficeAppointmentJsrValidatorInitializer,
            "platformTransactionManager", platformTransactionManager);

        int[][] intArray = new int[1][];
        when(jdbcTemplate.batchUpdate(anyString(), anyCollection(), anyInt(), any())).thenReturn(intArray);
        when(platformTransactionManager.getTransaction(any())).thenReturn(transactionStatus);
        when(exchangeMock.getContext()).thenReturn(new DefaultCamelContext());
        doNothing().when(platformTransactionManager).commit(transactionStatus);
        judicialOfficeAppointmentProcessor.process(exchangeMock);
        assertThat(((JudicialOfficeAppointment) exchangeMock.getMessage().getBody()))
            .isSameAs(judicialOfficeAppointmentMock1);
    }

    @Test
    @SuppressWarnings("unchecked")
    void should_return_JudicialOfficeAppointmentRow_response_skip_invalidProfiles() throws Exception {

        judicialOfficeAppointmentMock4.setElinksId("elinksid_4");
        List<JudicialOfficeAppointment> judicialOfficeAppointments = new ArrayList<>();
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock1);
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock2);

        RouteProperties routeProperties = new RouteProperties();
        routeProperties.setFileName("test");

        when(messageMock.getBody()).thenReturn(judicialOfficeAppointments);
        judicialUserProfileProcessor = mock(JudicialUserProfileProcessor.class);

        setField(judicialOfficeAppointmentProcessor, "judicialUserProfileProcessor",
            judicialUserProfileProcessor);
        JudicialUserProfile judicialUserProfileMock = createJudicialUserProfileMock(currentDate, dateTime, ELINKSID_1);
        JudicialUserProfile judicialUserProfileMock2 = createJudicialUserProfileMock(currentDate, dateTime, ELINKSID_3);

        List<JudicialUserProfile> judicialUserProfiles = new ArrayList<>();
        judicialUserProfiles.add(judicialUserProfileMock);
        judicialUserProfiles.add(judicialUserProfileMock2);

        when(judicialUserProfileProcessor.getInvalidRecords()).thenReturn(judicialUserProfiles);
        when(judicialUserProfileProcessor.getValidElinksInUserProfile()).thenReturn(Collections.singleton(ELINKSID_2));
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

        judicialOfficeAppointments = new ArrayList<>();
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock2);

        assertThat(((List<JudicialOfficeAppointment>) exchangeMock.getMessage().getBody()))
            .containsAll(judicialOfficeAppointments);
        verify(judicialOfficeAppointmentProcessor).setFileStatus(any(), any());
        verify(judicialOfficeAppointmentJsrValidatorInitializer)
            .auditJsrExceptions(anyList(), anyString(), anyString(), any());
    }

    @Test
    void testFilterAppointmentsRecordsForForeignKeyViolation() {
        List<JudicialOfficeAppointment> judicialOfficeAppointments = new ArrayList<>();
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock1);
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock2);
        JudicialOfficeAppointment judicialOfficeAppointmentMock5 = createJudicialOfficeAppointmentMock(currentDate,
            dateTime, "ELINKSID_5");
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock5);
        JudicialOfficeAppointment judicialOfficeAppointmentMock0 = createJudicialOfficeAppointmentMock(currentDate,
            dateTime, "ELINKSID_0");
        judicialOfficeAppointmentMock0.setRoleId("0");
        judicialOfficeAppointmentMock0.setContractType("0");
        judicialOfficeAppointmentMock0.setBaseLocationId("0");
        judicialOfficeAppointmentMock0.setRegionId("0");
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock0);


        JudicialUserProfile judicialUserProfileMock = createJudicialUserProfileMock(currentDate, dateTime, ELINKSID_1);
        JudicialUserProfile judicialUserProfileMock2 = createJudicialUserProfileMock(currentDate, dateTime, ELINKSID_3);
        List<JudicialUserProfile> judicialUserProfiles = new ArrayList<>();
        judicialUserProfiles.add(judicialUserProfileMock);
        judicialUserProfiles.add(judicialUserProfileMock2);

        when(judicialUserProfileProcessor.getInvalidRecords()).thenReturn(judicialUserProfiles);
        when(judicialUserProfileProcessor.getValidElinksInUserProfile()).thenReturn(Collections.singleton(ELINKSID_2));
        invokeMethod(judicialOfficeAppointmentProcessor, "filterAppointmentsRecordsForForeignKeyViolation",
            judicialOfficeAppointments, exchangeMock);
        verify(judicialOfficeAppointmentProcessor, times(5))
            .removeForeignKeyElements(anyList(), any(), anyString(), any(), any(), anyString());
        verify(judicialOfficeAppointmentJsrValidatorInitializer, times(1))
            .auditJsrExceptions(anyList(), anyString(), anyString(), any());
        assertEquals(1, judicialOfficeAppointments.size());
    }

}
