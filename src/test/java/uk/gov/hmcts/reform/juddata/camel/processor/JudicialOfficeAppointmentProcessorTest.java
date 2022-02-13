package uk.gov.hmcts.reform.juddata.camel.processor;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spi.Registry;
import org.apache.camel.support.DefaultExchange;
import org.hamcrest.CoreMatchers;
import org.hamcrest.junit.MatcherAssert;
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
import uk.gov.hmcts.reform.data.ingestion.camel.service.IEmailService;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.JsrValidatorInitializer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.util.JrdConstants;
import uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants;
import uk.gov.hmcts.reform.juddata.configuration.EmailConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.springframework.test.util.ReflectionTestUtils.invokeMethod;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.ROUTE_DETAILS;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.PERID_1;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.PERID_2;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.PERID_3;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.PERID_4;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialOfficeAppointmentMock;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserProfileMock;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.regions;


@PrepareForTest(JudicialOfficeAppointmentProcessor.class)
class JudicialOfficeAppointmentProcessorTest {

    Date currentDate = new Date();

    LocalDateTime dateTime = LocalDateTime.now();

    JudicialOfficeAppointment judicialOfficeAppointmentMock1 = createJudicialOfficeAppointmentMock(currentDate,
        dateTime, PERID_1);


    JudicialOfficeAppointment judicialOfficeAppointmentMock2 = createJudicialOfficeAppointmentMock(currentDate,
        dateTime, PERID_2);


    JudicialOfficeAppointment judicialOfficeAppointmentMock3 = createJudicialOfficeAppointmentMock(currentDate,
        dateTime, PERID_3);

    JudicialOfficeAppointment judicialOfficeAppointmentMock4 = createJudicialOfficeAppointmentMock(currentDate,
        dateTime, PERID_4);

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

    final IEmailService emailService = mock(IEmailService.class);

    final EmailConfiguration emailConfiguration = mock(EmailConfiguration.class);

    final EmailConfiguration.MailTypeConfig config = mock(EmailConfiguration.MailTypeConfig.class);

    @BeforeEach
    public void setup() {

        judicialOfficeAppointmentProcessor = spy(new JudicialOfficeAppointmentProcessor());
        judicialOfficeAppointmentMock3.setRegionId("0");
        judicialOfficeAppointmentMock3.setBaseLocationId("0");
        judicialOfficeAppointmentJsrValidatorInitializer = spy(new JsrValidatorInitializer<>());

        setField(judicialOfficeAppointmentProcessor,
            "judicialOfficeAppointmentJsrValidatorInitializer", judicialOfficeAppointmentJsrValidatorInitializer);
        setField(judicialOfficeAppointmentProcessor, "judicialUserProfileProcessor",
            judicialUserProfileProcessor);
        setField(judicialOfficeAppointmentProcessor, "emailConfiguration", emailConfiguration);
        setField(judicialOfficeAppointmentProcessor, "emailService", emailService);

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
        when(judicialUserProfileProcessor.getValidPerIdInUserProfile()).thenReturn(ImmutableSet.of(PERID_1,
            PERID_2, "invalid"));
        int[][] intArray = new int[1][];
        when(jdbcTemplate.batchUpdate(anyString(), anyList(), anyInt(), any())).thenReturn(intArray);
        setField(judicialOfficeAppointmentProcessor, "fetchLocations", "locations");
        setField(judicialOfficeAppointmentProcessor, "fetchBaseLocations", "baseLocations");

        setField(judicialOfficeAppointmentProcessor, "jdbcTemplate", jdbcTemplate);

        when(jdbcTemplate.queryForList("locations", String.class)).thenReturn(ImmutableList.of("regionId_1",
            "regionId_2"));

        when(jdbcTemplate.queryForList("baseLocations", String.class)).thenReturn(ImmutableList.of(
            "baseLocationId_1", "baseLocationId_2"));
        when(platformTransactionManager.getTransaction(any())).thenReturn(transactionStatus);
        doNothing().when(platformTransactionManager).commit(transactionStatus);

        when(emailConfiguration.getMailTypes()).thenReturn(Map.of("key", config));
        when(emailService.sendEmail(any())).thenReturn(1);

    }

    @Test
    @SuppressWarnings("unchecked")
    void should_return_JudicialOfficeAppointmentRow_response() {

        List<JudicialOfficeAppointment> judicialOfficeAppointments = new ArrayList<>();
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock1);
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock2);
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock3);

        when(messageMock.getBody()).thenReturn(judicialOfficeAppointments);
        judicialUserProfileProcessor = new JudicialUserProfileProcessor();
        judicialOfficeAppointmentProcessor.process(exchangeMock);
        assertThat(((List) exchangeMock.getMessage().getBody())).hasSize(3);
        assertThat(((List<JudicialOfficeAppointment>) exchangeMock.getMessage().getBody()))
            .isSameAs(judicialOfficeAppointments);
        verify(judicialOfficeAppointmentProcessor).filterInvalidUserProfileRecords(any(), any(), any(), any(), any());
        verify(judicialOfficeAppointmentJsrValidatorInitializer)
            .auditJsrExceptions(anyList(), anyString(), anyString(), any());
        verify(judicialOfficeAppointmentProcessor).audit(any(), any());
        verify(messageMock).setBody(any());
        verify(exchangeMock, times(4)).getMessage();
        verify(judicialOfficeAppointmentProcessor).filterInvalidUserProfileRecords(anyList(),
            isNull(), any(), any(), isNull());
    }


    @Test
    @SuppressWarnings("unchecked")
    void should_return_JudicialOfficeAppointmentRow_response_filter_defaults() {

        List<JudicialOfficeAppointment> judicialOfficeAppointments = new ArrayList<>();
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock1);
        judicialOfficeAppointmentMock2.setRegionId("0");
        judicialOfficeAppointmentMock2.setBaseLocationId("0");
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock2);

        when(messageMock.getBody()).thenReturn(judicialOfficeAppointments);
        judicialUserProfileProcessor = new JudicialUserProfileProcessor();
        judicialOfficeAppointmentProcessor.process(exchangeMock);
        assertThat(((List) exchangeMock.getMessage().getBody())).hasSize(2);
        assertThat(((List<JudicialOfficeAppointment>) exchangeMock.getMessage().getBody()))
            .isSameAs(judicialOfficeAppointments);
        verify(judicialOfficeAppointmentProcessor).filterInvalidUserProfileRecords(any(), any(), any(), any(), any());
    }

    @Test
    void should_return_JudicialOfficeAppointmentRow_with_single_record_response() {

        when(messageMock.getBody()).thenReturn(judicialOfficeAppointmentMock1);

        judicialOfficeAppointmentProcessor.process(exchangeMock);
        assertThat(((JudicialOfficeAppointment) exchangeMock.getMessage().getBody()))
            .isSameAs(judicialOfficeAppointmentMock1);
    }

    @Test
    void should_return_JudicialOfficeAppointmentRow_with_single_record_with_per_id_nullresponse() {

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
    void should_return_JudicialOfficeAppointmentRow_response_skip_invalidProfiles() {

        judicialOfficeAppointmentMock4.setPerId("perid_4");
        List<JudicialOfficeAppointment> judicialOfficeAppointments = new ArrayList<>();
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock1);
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock2);
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock3);

        RouteProperties routeProperties = new RouteProperties();
        routeProperties.setFileName("test");

        when(messageMock.getBody()).thenReturn(judicialOfficeAppointments);
        judicialUserProfileProcessor = mock(JudicialUserProfileProcessor.class);

        setField(judicialOfficeAppointmentProcessor, "judicialUserProfileProcessor",
            judicialUserProfileProcessor);
        JudicialUserProfile judicialUserProfileMock = createJudicialUserProfileMock(currentDate, dateTime, PERID_1);
        JudicialUserProfile judicialUserProfileMock2 = createJudicialUserProfileMock(currentDate, dateTime, PERID_3);

        List<JudicialUserProfile> judicialUserProfiles = new ArrayList<>();
        judicialUserProfiles.add(judicialUserProfileMock);
        judicialUserProfiles.add(judicialUserProfileMock2);

        when(judicialUserProfileProcessor.getInvalidRecords()).thenReturn(judicialUserProfiles);
        when(judicialUserProfileProcessor.getValidPerIdInUserProfile()).thenReturn(Collections.singleton(PERID_2));
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
            dateTime, "PERID_5");
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock5);
        JudicialOfficeAppointment judicialOfficeAppointmentMock0 = createJudicialOfficeAppointmentMock(currentDate,
            dateTime, "PERID_0");
        judicialOfficeAppointmentMock0.setBaseLocationId("0");
        judicialOfficeAppointmentMock0.setRegionId("0");
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock0);


        JudicialUserProfile judicialUserProfileMock = createJudicialUserProfileMock(currentDate, dateTime, PERID_1);
        JudicialUserProfile judicialUserProfileMock2 = createJudicialUserProfileMock(currentDate, dateTime, PERID_3);
        List<JudicialUserProfile> judicialUserProfiles = new ArrayList<>();
        judicialUserProfiles.add(judicialUserProfileMock);
        judicialUserProfiles.add(judicialUserProfileMock2);

        when(judicialUserProfileProcessor.getInvalidRecords()).thenReturn(judicialUserProfiles);
        when(judicialUserProfileProcessor.getValidPerIdInUserProfile()).thenReturn(Collections.singleton(PERID_2));
        invokeMethod(judicialOfficeAppointmentProcessor, "filterAppointmentsRecordsForForeignKeyViolation",
            judicialOfficeAppointments, exchangeMock);
        verify(judicialOfficeAppointmentProcessor, times(3))
                .removeForeignKeyElements(anyList(), any(), anyString(), any(), any(), anyString());
        verify(judicialOfficeAppointmentJsrValidatorInitializer, times(1))
            .auditJsrExceptions(anyList(), anyString(), anyString(), any());
        assertEquals(1, judicialOfficeAppointments.size());
    }

    @Test
    void testFilterAppointmentsRecordsForForeignKeyViolation_extraLocations() {
        List<JudicialOfficeAppointment> judicialOfficeAppointments = new ArrayList<>();
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock1);
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock2);
        JudicialOfficeAppointment judicialOfficeAppointmentMock5 = createJudicialOfficeAppointmentMock(currentDate,
                dateTime, "PERID_5");
        judicialOfficeAppointmentMock5.setRegionId("region_5");
        judicialOfficeAppointmentMock5.setBaseLocationId("location_5");
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock5);
        JudicialOfficeAppointment judicialOfficeAppointmentMock0 = createJudicialOfficeAppointmentMock(currentDate,
                dateTime, "PERID_0");
        judicialOfficeAppointmentMock0.setBaseLocationId("location_0");
        judicialOfficeAppointmentMock0.setRegionId("region_0");
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock0);


        JudicialUserProfile judicialUserProfileMock = createJudicialUserProfileMock(currentDate, dateTime, PERID_1);
        JudicialUserProfile judicialUserProfileMock2 = createJudicialUserProfileMock(currentDate, dateTime, PERID_3);
        List<JudicialUserProfile> judicialUserProfiles = new ArrayList<>();
        judicialUserProfiles.add(judicialUserProfileMock);
        judicialUserProfiles.add(judicialUserProfileMock2);

        when(judicialUserProfileProcessor.getInvalidRecords()).thenReturn(judicialUserProfiles);
        when(judicialUserProfileProcessor.getValidPerIdInUserProfile()).thenReturn(Set.of("PERID_0", PERID_1, PERID_2,
                "PERID_5"));
        when(jdbcTemplate.queryForList(any(), eq(String.class)))
                .thenReturn(List.of("region_5", regions.get(PERID_2)))
                .thenReturn(Collections.emptyList());
        when(emailConfiguration.getMailTypes()).thenReturn(Map.of(JrdConstants.REGION, config,
                JrdConstants.BASE_LOCATION, config));
        when(config.isEnabled()).thenReturn(true);
        when(config.getBody()).thenReturn("email sample body");
        when(config.getSubject()).thenReturn("email sample subject");

        invokeMethod(judicialOfficeAppointmentProcessor, "filterAppointmentsRecordsForForeignKeyViolation",
                judicialOfficeAppointments, exchangeMock);
        verify(judicialOfficeAppointmentProcessor, times(3))
                .removeForeignKeyElements(anyList(), any(), anyString(), any(), any(), anyString());
        verify(judicialOfficeAppointmentJsrValidatorInitializer, times(2))
                .auditJsrExceptions(anyList(), anyString(), anyString(), any());
        verify(emailService, times(2)).sendEmail(any());
    }

    @Test
    void testSendEmail() {
        Set<JudicialOfficeAppointment> judicialOfficeAppointments = new HashSet<>();
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock1);
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock2);
        when(emailConfiguration.getMailTypes()).thenReturn(Map.of(JrdConstants.REGION, config,
                JrdConstants.BASE_LOCATION, config,"TEST1",config));
        when(config.isEnabled()).thenReturn(true);
        when(config.getBody()).thenReturn("email sample body");
        when(config.getSubject()).thenReturn("email sample subject");

        for (String key: List.of(JrdMappingConstants.LOCATION_ID, JrdMappingConstants.BASE_LOCATION_ID)) {
            int result = judicialOfficeAppointmentProcessor.sendEmail(judicialOfficeAppointments, key);
            assertEquals(1, result);
        }
        verify(emailService, times(2)).sendEmail(any());
    }

    @Test
    void testSendEmail_withOutConfig() {
        Set<JudicialOfficeAppointment> judicialOfficeAppointments = new HashSet<>();
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock1);
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock2);
        JudicialOfficeAppointment judicialOfficeAppointmentMock5 = createJudicialOfficeAppointmentMock(currentDate,
                dateTime, "PERID_5");
        judicialOfficeAppointmentMock5.setRegionId("region_5");
        judicialOfficeAppointmentMock5.setBaseLocationId("location_5");
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock5);
        JudicialOfficeAppointment judicialOfficeAppointmentMock0 = createJudicialOfficeAppointmentMock(currentDate,
                dateTime, "PERID_0");
        judicialOfficeAppointmentMock0.setBaseLocationId("location_0");
        judicialOfficeAppointmentMock0.setRegionId("region_0");
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock0);

        int result = judicialOfficeAppointmentProcessor.sendEmail(judicialOfficeAppointments,
                JrdMappingConstants.LOCATION_ID);
        verify(emailService, times(0)).sendEmail(any());
        assertEquals(-1,result);
    }

    @Test
    void testCreateLocationEmailBody() {
        Set<JudicialOfficeAppointment> judicialOfficeAppointments = new HashSet<>();
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock1);
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock2);
        String result = invokeMethod(judicialOfficeAppointmentProcessor, "createLocationEmailBody",
                judicialOfficeAppointments);
        MatcherAssert.assertThat(result, CoreMatchers.containsString("779321b3-3170-44a0-bc7d-b4decc2aea10"));
    }

    @Test
    void testCreateRegionEmailBody() {
        Set<JudicialOfficeAppointment> judicialOfficeAppointments = new HashSet<>();
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock1);
        judicialOfficeAppointments.add(judicialOfficeAppointmentMock2);
        String result = invokeMethod(judicialOfficeAppointmentProcessor, "createRegionEmailBody",
                judicialOfficeAppointments);
        MatcherAssert.assertThat(result, CoreMatchers.containsString("779321b3-3170-44a0-bc7d-b4decc2aea10"));
    }

}
