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
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.testcontainers.shaded.com.google.common.collect.ImmutableSet;
import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.RouteProperties;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.JsrValidatorInitializer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAuthorisation;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.ROUTE_DETAILS;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.ELINKSID_1;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.ELINKSID_2;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialOfficeAuthorisation;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserProfileMock;

public class JudicialOfficeAuthorisationProcessorTest {

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

    @Before
    public void setup() {

        judicialOfficeAuthorisationProcessor = new JudicialOfficeAuthorisationProcessor();
        judicialOfficeAuthorisation1.setElinksId("elinks_2");
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
        when(judicialUserProfileProcessor.getValidElinksInUserProfile()).thenReturn(ImmutableSet.of(ELINKSID_1,
            ELINKSID_2, "invalid"));

        int[][] intArray = new int[1][];
        when(jdbcTemplate.batchUpdate(anyString(), anyList(), anyInt(), any())).thenReturn(intArray);
        when(platformTransactionManager.getTransaction(any())).thenReturn(transactionStatus);
        doNothing().when(platformTransactionManager).commit(transactionStatus);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_return_JudicialOfficeAuthorizationRow_response() {

        List<JudicialOfficeAuthorisation> judicialOfficeAuthorisations = new ArrayList<>();
        judicialOfficeAuthorisations.add(judicialOfficeAuthorisation1);
        judicialOfficeAuthorisations.add(judicialOfficeAuthorisation2);

        when(messageMock.getBody()).thenReturn(judicialOfficeAuthorisations);
        judicialOfficeAuthorisationProcessor.process(exchangeMock);
        assertThat(((List) exchangeMock.getMessage().getBody()).size()).isEqualTo(2);
        assertThat(((List<JudicialOfficeAppointment>) exchangeMock.getMessage().getBody()))
                .isSameAs(judicialOfficeAuthorisations);

        verify(exchangeMock, times(5)).getIn();
        verify(exchangeMock, times(3)).getMessage();
        verify(messageMock, times(4)).getBody();
    }

    @Test
    public void should_return_JudicialOfficeAuthorizationRow_with_single_record_response() {


        when(messageMock.getBody()).thenReturn(judicialOfficeAuthorisation1);

        judicialOfficeAuthorisationProcessor.process(exchangeMock);
        assertThat(((JudicialOfficeAuthorisation) exchangeMock.getMessage().getBody()))
                .isSameAs(judicialOfficeAuthorisation1);
        verify(exchangeMock, times(5)).getIn();
        verify(exchangeMock, times(2)).getMessage();
        verify(messageMock, times(3)).getBody();
    }

    @Test
    public void should_return_JudicialOfficeAuthorizationRow_with_single_record_with_elinks_id_null_response() {

        judicialOfficeAuthorisation1.setElinksId(null);

        when(messageMock.getBody()).thenReturn(judicialOfficeAuthorisation1);
        RouteProperties routeProperties = new RouteProperties();
        routeProperties.setFileName("test");

        setField(judicialOfficeAuthorisationProcessor, "jsrThresholdLimit", 5);
        setField(judicialOfficeAuthorisationJsrValidatorInitializer, "camelContext", camelContext);
        setField(judicialOfficeAuthorisationJsrValidatorInitializer, "jdbcTemplate", jdbcTemplate);
        setField(judicialOfficeAuthorisationJsrValidatorInitializer,
                "platformTransactionManager", platformTransactionManager);

        //when(exchangeMock.getIn().getHeader(ROUTE_DETAILS)).thenReturn(routeProperties);
        judicialOfficeAuthorisationProcessor.process(exchangeMock);
        assertThat(((JudicialOfficeAuthorisation) exchangeMock.getMessage().getBody()))
                .isSameAs(judicialOfficeAuthorisation1);
        verify(exchangeMock, times(5)).getIn();
        verify(exchangeMock, times(2)).getMessage();
        verify(messageMock, times(3)).getBody();
        verify(platformTransactionManager, times(1)).getTransaction(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_return_JudicialOfficeAuthorizationRow_response_skip_invalidProfiles() {

        List<JudicialOfficeAuthorisation> judicialOfficeAuthorisations = new ArrayList<>();
        judicialOfficeAuthorisations.add(judicialOfficeAuthorisation1);
        judicialOfficeAuthorisations.add(judicialOfficeAuthorisation2);



        final PlatformTransactionManager platformTransactionManager = mock(PlatformTransactionManager.class);
        final TransactionStatus transactionStatus = mock(TransactionStatus.class);
        RouteProperties routeProperties = new RouteProperties();
        routeProperties.setFileName("test");

        when(messageMock.getBody()).thenReturn(judicialOfficeAuthorisations);//


        setField(judicialOfficeAuthorisationProcessor, "judicialUserProfileProcessor",
                judicialUserProfileProcessor);
        JudicialUserProfile judicialUserProfileMock = createJudicialUserProfileMock(currentDate, dateTime, ELINKSID_1);
        JudicialUserProfile judicialUserProfileMock2 = createJudicialUserProfileMock(currentDate, dateTime,
            "elinks_3");
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
        verify(exchangeMock, times(6)).getIn();
        verify(exchangeMock.getIn(), times(2)).getHeader(ROUTE_DETAILS);
        verify(messageMock, times(4)).getBody();
    }
}
