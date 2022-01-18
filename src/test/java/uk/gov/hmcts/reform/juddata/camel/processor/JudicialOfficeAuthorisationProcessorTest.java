package uk.gov.hmcts.reform.juddata.camel.processor;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spi.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.testcontainers.shaded.com.google.common.collect.ImmutableSet;
import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.RouteProperties;
import uk.gov.hmcts.reform.data.ingestion.camel.service.IEmailService;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.JsrValidatorInitializer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAuthorisation;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.util.EmailTemplate;
import uk.gov.hmcts.reform.juddata.camel.util.JrdConstants;
import uk.gov.hmcts.reform.juddata.configuration.EmailConfiguration;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Date;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.springframework.test.util.ReflectionTestUtils.invokeMethod;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.ROUTE_DETAILS;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.PERID_1;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.PERID_2;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.PERID_3;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialOfficeAuthorisation;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserProfileMock;

class JudicialOfficeAuthorisationProcessorTest  {

    private Validator validator;
    String date = "2017-10-01 00:00:00.000";

    Date currentDate = new Date();

    LocalDateTime dateTime = LocalDateTime.now();
    JudicialOfficeAuthorisation judicialOfficeAuthorisation1 = createJudicialOfficeAuthorisation(date);

    JudicialOfficeAuthorisation judicialOfficeAuthorisation2 = createJudicialOfficeAuthorisation(date);

    JudicialOfficeAuthorisationProcessor judicialOfficeAuthorisationProcessor;

    private JsrValidatorInitializer<JudicialOfficeAuthorisation> judicialOfficeAuthorisationJsrValidatorInitializer;
    JudicialUserProfileProcessor judicialUserProfileProcessor = spy(new JudicialUserProfileProcessor());
    CamelContext camelContext = new DefaultCamelContext();

    Exchange exchangeMock;
    Message messageMock;
    Registry registryMock;
    ApplicationContext applicationContext = mock(ConfigurableApplicationContext.class);
    ConfigurableListableBeanFactory configurableListableBeanFactory = mock(ConfigurableListableBeanFactory.class);
    final JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
    final PlatformTransactionManager platformTransactionManager = mock(PlatformTransactionManager.class);
    final TransactionStatus transactionStatus = mock(TransactionStatus.class);
    final EmailConfiguration emailConfiguration = mock(EmailConfiguration.class);
    final EmailTemplate emailTemplate = mock(EmailTemplate.class);
    EmailConfiguration.MailTypeConfig mailConfig = mock(EmailConfiguration.MailTypeConfig.class);
    final IEmailService emailService = mock(IEmailService.class);

    @BeforeEach
    public void setup() {

        judicialOfficeAuthorisationProcessor = spy(new JudicialOfficeAuthorisationProcessor());
        judicialOfficeAuthorisation1.setPerId("per_2");
        judicialOfficeAuthorisationJsrValidatorInitializer
            = new JsrValidatorInitializer<>();
        setField(judicialOfficeAuthorisationProcessor,
            "judicialOfficeAuthorisationJsrValidatorInitializer",
            judicialOfficeAuthorisationJsrValidatorInitializer);
        setField(judicialOfficeAuthorisationProcessor, "judicialUserProfileProcessor",
            judicialUserProfileProcessor);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        setField(judicialOfficeAuthorisationJsrValidatorInitializer, "validator", validator);
        setField(judicialOfficeAuthorisationJsrValidatorInitializer, "camelContext", camelContext);
        setField(judicialOfficeAuthorisationJsrValidatorInitializer, "jdbcTemplate", jdbcTemplate);
        setField(judicialOfficeAuthorisationProcessor, "jdbcTemplate", jdbcTemplate);
        setField(judicialOfficeAuthorisationProcessor, "fetchLowerLevels", "fetchLowerLevels");
        setField(judicialOfficeAuthorisationProcessor, "emailService", emailService);
        setField(judicialOfficeAuthorisationProcessor, "emailTemplate", emailTemplate);
        setField(judicialOfficeAuthorisationJsrValidatorInitializer, "platformTransactionManager",
            platformTransactionManager);

        messageMock = mock(Message.class);
        RouteProperties routeProperties = new RouteProperties();
        routeProperties.setFileName("test");
        exchangeMock = mock(Exchange.class);
        registryMock = mock(Registry.class);
        when(exchangeMock.getIn()).thenReturn(messageMock);

        when(exchangeMock.getIn().getHeader(ROUTE_DETAILS)).thenReturn(routeProperties);
        when(exchangeMock.getContext()).thenReturn(camelContext);
        when(exchangeMock.getMessage()).thenReturn(messageMock);
        setField(judicialOfficeAuthorisationProcessor, "applicationContext", applicationContext);
        setField(judicialUserProfileProcessor, "applicationContext", applicationContext);
        when(((ConfigurableApplicationContext)
            applicationContext).getBeanFactory()).thenReturn(configurableListableBeanFactory);
        when(judicialUserProfileProcessor.getValidPerIdInUserProfile()).thenReturn(ImmutableSet.of(PERID_1,
            PERID_2, "invalid"));

        int[][] intArray = new int[1][];
        when(jdbcTemplate.batchUpdate(anyString(), anyList(), anyInt(), any())).thenReturn(intArray);
        when(platformTransactionManager.getTransaction(any())).thenReturn(transactionStatus);
        doNothing().when(platformTransactionManager).commit(transactionStatus);

    }

    @Test
    @SuppressWarnings("unchecked")
    void should_return_JudicialOfficeAuthorizationRow_response() {

        List<JudicialOfficeAuthorisation> judicialOfficeAuthorisations = new ArrayList<>();
        judicialOfficeAuthorisations.add(judicialOfficeAuthorisation1);
        judicialOfficeAuthorisations.add(judicialOfficeAuthorisation2);

        when(messageMock.getBody()).thenReturn(judicialOfficeAuthorisations);
        judicialOfficeAuthorisationProcessor.process(exchangeMock);
        assertThat(((List) exchangeMock.getMessage().getBody()).size()).isEqualTo(2);
        assertThat(((List<JudicialOfficeAppointment>) exchangeMock.getMessage().getBody()))
            .isSameAs(judicialOfficeAuthorisations);

        verify(exchangeMock, times(6)).getIn();
        verify(exchangeMock, times(3)).getMessage();
        verify(messageMock, times(4)).getBody();
        verify(judicialOfficeAuthorisationProcessor).audit(any(), any());
        verify(messageMock).setBody(any());
        verify(judicialOfficeAuthorisationProcessor).filterInvalidUserProfileRecords(anyList(),
            isNull(),
            any(), any(),
            isNull());
    }

    @Test
    void should_return_JudicialOfficeAuthorizationRow_with_single_record_response() {

        when(messageMock.getBody()).thenReturn(judicialOfficeAuthorisation1);

        judicialOfficeAuthorisationProcessor.process(exchangeMock);
        assertThat(((JudicialOfficeAuthorisation) exchangeMock.getMessage().getBody()))
            .isSameAs(judicialOfficeAuthorisation1);
        verify(exchangeMock, times(6)).getIn();
        verify(exchangeMock, times(2)).getMessage();
        verify(messageMock, times(3)).getBody();
    }

    @Test
    void should_return_JudicialOfficeAuthorizationRow_with_single_record_with_per_id_null_response() {
        when(messageMock.getBody()).thenReturn(judicialOfficeAuthorisation1);
        setField(judicialOfficeAuthorisationProcessor, "jsrThresholdLimit", 5);
        setField(judicialOfficeAuthorisationJsrValidatorInitializer, "camelContext", camelContext);
        setField(judicialOfficeAuthorisationJsrValidatorInitializer, "jdbcTemplate", jdbcTemplate);
        setField(judicialOfficeAuthorisationJsrValidatorInitializer,
            "platformTransactionManager", platformTransactionManager);

        judicialOfficeAuthorisationProcessor.process(exchangeMock);
        assertThat(((JudicialOfficeAuthorisation) exchangeMock.getMessage().getBody()))
            .isSameAs(judicialOfficeAuthorisation1);
        verify(exchangeMock, times(6)).getIn();
        verify(exchangeMock, times(2)).getMessage();
        verify(messageMock, times(3)).getBody();
        verify(platformTransactionManager, times(1)).getTransaction(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    void should_return_JudicialOfficeAuthorizationRow_response_skip_invalidProfiles() {

        List<JudicialOfficeAuthorisation> judicialOfficeAuthorisations = new ArrayList<>();
        judicialOfficeAuthorisations.add(judicialOfficeAuthorisation1);
        judicialOfficeAuthorisations.add(judicialOfficeAuthorisation2);


        final PlatformTransactionManager platformTransactionManager = mock(PlatformTransactionManager.class);
        final TransactionStatus transactionStatus = mock(TransactionStatus.class);
        RouteProperties routeProperties = new RouteProperties();
        routeProperties.setFileName("test");

        when(messageMock.getBody()).thenReturn(judicialOfficeAuthorisations);


        setField(judicialOfficeAuthorisationProcessor, "judicialUserProfileProcessor",
            judicialUserProfileProcessor);
        JudicialUserProfile judicialUserProfileMock = createJudicialUserProfileMock(currentDate, dateTime, PERID_1);
        JudicialUserProfile judicialUserProfileMock2 = createJudicialUserProfileMock(currentDate, dateTime,
            "per_3");
        List<JudicialUserProfile> judicialUserProfiles = new ArrayList<>();
        judicialUserProfiles.add(judicialUserProfileMock);
        judicialUserProfiles.add(judicialUserProfileMock2);

        when(judicialUserProfileProcessor.getInvalidRecords()).thenReturn(judicialUserProfiles);//done
        setField(judicialOfficeAuthorisationProcessor, "jsrThresholdLimit", 5);
        setField(judicialOfficeAuthorisationJsrValidatorInitializer, "camelContext", camelContext);
        setField(judicialOfficeAuthorisationJsrValidatorInitializer, "jdbcTemplate", jdbcTemplate);
        setField(judicialOfficeAuthorisationJsrValidatorInitializer,
            "platformTransactionManager", platformTransactionManager);

        int[][] intArray = new int[1][];
        when(jdbcTemplate.batchUpdate(any(String.class), any(List.class), any(Integer.class),
            any(ParameterizedPreparedStatementSetter.class))).thenReturn(intArray);
        when(platformTransactionManager.getTransaction(any())).thenReturn(transactionStatus);//done
        doNothing().when(platformTransactionManager).commit(transactionStatus);//done
        when(exchangeMock.getIn().getHeader(ROUTE_DETAILS)).thenReturn(routeProperties);//done

        judicialOfficeAuthorisationProcessor.process(exchangeMock);
        assertThat(((List) exchangeMock.getMessage().getBody()).size()).isEqualTo(2);
        judicialOfficeAuthorisations = new ArrayList<>();
        judicialOfficeAuthorisations.add(judicialOfficeAuthorisation2);
        assertThat(((List<JudicialOfficeAuthorisation>) exchangeMock.getMessage().getBody()))
            .containsAll(judicialOfficeAuthorisations);

        verify(judicialUserProfileProcessor, times(1)).getInvalidRecords();
        verify(platformTransactionManager, times(1)).getTransaction(any());
        verify(platformTransactionManager, times(1)).commit(transactionStatus);
        verify(exchangeMock, times(3)).getMessage();
        verify(exchangeMock, times(7)).getIn();
        verify(exchangeMock.getIn(), times(3)).getHeader(ROUTE_DETAILS);
        verify(messageMock, times(4)).getBody();
    }

    @Test
    void testFilterAuthorizationRecordsForForeignKeyViolation() {

        List<JudicialOfficeAuthorisation> judicialOfficeAuthorisations = new ArrayList<>();
        JudicialOfficeAuthorisation judicialOfficeAuthorisation1 = createJudicialOfficeAuthorisation(date);
        judicialOfficeAuthorisation1.setPerId(PERID_1);
        JudicialOfficeAuthorisation judicialOfficeAuthorisation2 = createJudicialOfficeAuthorisation(date);
        judicialOfficeAuthorisation2.setPerId(PERID_2);
        judicialOfficeAuthorisations.add(judicialOfficeAuthorisation1);
        judicialOfficeAuthorisations.add(judicialOfficeAuthorisation2);

        JudicialUserProfile judicialUserProfileMock = createJudicialUserProfileMock(currentDate, dateTime, PERID_1);
        JudicialUserProfile judicialUserProfileMock2 = createJudicialUserProfileMock(currentDate, dateTime, PERID_3);

        List<JudicialUserProfile> judicialUserProfiles = new ArrayList<>();
        judicialUserProfiles.add(judicialUserProfileMock);
        judicialUserProfiles.add(judicialUserProfileMock2);

        when(judicialUserProfileProcessor.getInvalidRecords()).thenReturn(judicialUserProfiles);
        when(judicialUserProfileProcessor.getValidPerIdInUserProfile()).thenReturn(Collections.singleton(PERID_2));
        when(emailTemplate.getMailTypeConfig(any(), any())).thenReturn(mailConfig);
        when(emailConfiguration.getMailTypes()).thenReturn(Map.of(JrdConstants.LOWER_LEVEL_AUTH, mailConfig));
        when(mailConfig.isEnabled()).thenReturn(true);
        when(mailConfig.getBody()).thenReturn("email body");
        when(mailConfig.getSubject()).thenReturn("email subject");

        invokeMethod(judicialOfficeAuthorisationProcessor, "filterAuthorizationsRecordsForForeignKeyViolation",
            judicialOfficeAuthorisations, exchangeMock);
        assertEquals(1, judicialOfficeAuthorisations.size());
        verify(emailService, times(1)).sendEmail(any());
    }

    @Test
    void should_return_new_lower_level_authorisations() {
        JudicialOfficeAuthorisation joAuth1 = createJudicialOfficeAuthorisation(date);
        joAuth1.setPerId(PERID_1);
        joAuth1.setLowerLevel("01 - Social Security");

        JudicialOfficeAuthorisation joAuth2 = createJudicialOfficeAuthorisation(date);
        joAuth2.setPerId(PERID_2);
        joAuth2.setLowerLevel("05 - Industrial Injuries");

        JudicialOfficeAuthorisation joAuth3 = createJudicialOfficeAuthorisation(date);
        joAuth3.setPerId(PERID_3);
        joAuth3.setLowerLevel("07 - Vaccine Damage");

        when(jdbcTemplate.queryForList("fetchLowerLevels", String.class))
                .thenReturn(List.of(
                        "01 - Social Security",
                        "02 - Child Support",
                        "03 - Disability Living Allowance"));

        var joAuths = List.of(joAuth1, joAuth2, joAuth3);

        List<JudicialOfficeAuthorisation> judicialOfficeAuthorisations =
            invokeMethod(judicialOfficeAuthorisationProcessor, "retrieveNewLowerLevelAuthorisations", joAuths);
        assert judicialOfficeAuthorisations != null;
        assertEquals(2, judicialOfficeAuthorisations.size());
    }

    @Test
    void should_not_return_any_new_lower_level_authorisation() {
        JudicialOfficeAuthorisation joAuth1 = createJudicialOfficeAuthorisation(date);
        joAuth1.setPerId(PERID_1);
        joAuth1.setLowerLevel("01 - Social Security");

        JudicialOfficeAuthorisation joAuth2 = createJudicialOfficeAuthorisation(date);
        joAuth2.setPerId(PERID_2);
        joAuth2.setLowerLevel("02 - Child Support");

        JudicialOfficeAuthorisation joAuth3 = createJudicialOfficeAuthorisation(date);
        joAuth3.setPerId(PERID_3);
        joAuth3.setLowerLevel("03 - Disability Living Allowance");

        when(jdbcTemplate.queryForList("fetchLowerLevels", String.class))
                .thenReturn(List.of(
                        "01 - Social Security",
                        "02 - Child Support",
                        "03 - Disability Living Allowance"));

        var joAuths = List.of(joAuth1, joAuth2, joAuth3);

        List<JudicialOfficeAuthorisation> judicialOfficeAuthorisations =
                invokeMethod(judicialOfficeAuthorisationProcessor, "retrieveNewLowerLevelAuthorisations", joAuths);
        assert judicialOfficeAuthorisations != null;
        assertEquals(0, judicialOfficeAuthorisations.size());
    }
}
