package uk.gov.hmcts.reform.juddata.camel.processor;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spi.Registry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;
import uk.gov.hmcts.reform.data.ingestion.camel.exception.RouteFailedException;
import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.RouteProperties;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.JsrValidatorInitializer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.util.JrdUserProfileUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static java.util.Collections.emptySet;
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
import static org.springframework.test.util.ReflectionTestUtils.invokeMethod;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.ROUTE_DETAILS;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.PERID_1;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.PERID_2;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserProfileMock;

class JudicialUserProfileProcessorTest {

    Date currentDate = new Date();

    LocalDateTime dateTime = LocalDateTime.now();

    private JudicialUserProfile judicialUserProfileMock1;

    private JudicialUserProfile judicialUserProfileMock2;

    private JudicialUserProfileProcessor judicialUserProfileProcessor;

    private JsrValidatorInitializer<JudicialUserProfile> judicialUserProfileJsrValidatorInitializer;

    final PlatformTransactionManager platformTransactionManager = mock(PlatformTransactionManager.class);

    private JrdUserProfileUtil judicialUserProfileUtil;

    private Validator validator;

    private JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

    CamelContext camelContext = new DefaultCamelContext();
    Exchange exchangeMock;
    Message messageMock;
    Registry registryMock;
    ApplicationContext applicationContext = mock(ConfigurableApplicationContext.class);
    ConfigurableListableBeanFactory configurableListableBeanFactory = mock(ConfigurableListableBeanFactory.class);


    @BeforeEach
    public void setup() {
        judicialUserProfileMock1 = createJudicialUserProfileMock(currentDate, dateTime, PERID_1);
        judicialUserProfileMock2 = createJudicialUserProfileMock(currentDate, dateTime, PERID_2);
        judicialUserProfileProcessor = new JudicialUserProfileProcessor();
        judicialUserProfileUtil = new JrdUserProfileUtil();
        judicialUserProfileJsrValidatorInitializer
            = new JsrValidatorInitializer<>();
        setField(judicialUserProfileProcessor,
            "judicialUserProfileJsrValidatorInitializer", judicialUserProfileJsrValidatorInitializer);
        setField(judicialUserProfileProcessor, "jrdUserProfileUtil", judicialUserProfileUtil);
        setField(judicialUserProfileProcessor, "jdbcTemplate", jdbcTemplate);
        setField(judicialUserProfileProcessor,
            "loadPerId", "dummysql");
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        setField(judicialUserProfileJsrValidatorInitializer, "validator", validator);
        setField(judicialUserProfileJsrValidatorInitializer, "camelContext", camelContext);
        setField(judicialUserProfileJsrValidatorInitializer, "jdbcTemplate", jdbcTemplate);
        setField(judicialUserProfileJsrValidatorInitializer, "platformTransactionManager",
            platformTransactionManager);
        messageMock = mock(Message.class);
        RouteProperties routeProperties = new RouteProperties();
        routeProperties.setFileName("test");
        exchangeMock = mock(Exchange.class);
        registryMock = mock(Registry.class);
        when(exchangeMock.getIn()).thenReturn(messageMock);
        when(exchangeMock.getIn().getHeader(ROUTE_DETAILS)).thenReturn(routeProperties);
        when(exchangeMock.getContext()).thenReturn(new DefaultCamelContext());
        when(exchangeMock.getMessage()).thenReturn(messageMock);
        setField(judicialUserProfileProcessor, "applicationContext", applicationContext);
        when(((ConfigurableApplicationContext)
            applicationContext).getBeanFactory()).thenReturn(configurableListableBeanFactory);
        when(jdbcTemplate.queryForList("dummysql", String.class))
            .thenReturn(ImmutableList.of(PERID_1, PERID_2, "0"));

    }

    @Test
    @SuppressWarnings("unchecked")
    void should_return_JudicialUserProfileRow_with_single_record_with_per_id_nullresponse() {

        judicialUserProfileMock1.setPerId(null);

        final JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        final PlatformTransactionManager platformTransactionManager = mock(PlatformTransactionManager.class);
        final TransactionStatus transactionStatus = mock(TransactionStatus.class);

        when(messageMock.getBody()).thenReturn(judicialUserProfileMock1);
        RouteProperties routeProperties = new RouteProperties();
        routeProperties.setFileName("test");

        setField(judicialUserProfileProcessor, "jsrThresholdLimit", 5);
        setField(judicialUserProfileJsrValidatorInitializer, "camelContext", camelContext);
        setField(judicialUserProfileJsrValidatorInitializer, "jdbcTemplate", jdbcTemplate);
        setField(judicialUserProfileJsrValidatorInitializer,
            "platformTransactionManager", platformTransactionManager);

        int[][] intArray = new int[1][];
        when(jdbcTemplate.batchUpdate(anyString(), anyList(), anyInt(), any())).thenReturn(intArray);
        when(platformTransactionManager.getTransaction(any())).thenReturn(transactionStatus);
        doNothing().when(platformTransactionManager).commit(transactionStatus);
        when(exchangeMock.getIn().getHeader(ROUTE_DETAILS)).thenReturn(routeProperties);

        judicialUserProfileProcessor.process(exchangeMock);
        verify(messageMock, times(1)).setBody(any());
        assertThat(((JudicialUserProfile) exchangeMock.getMessage().getBody())).isSameAs(judicialUserProfileMock1);
    }

    @Test
    @SuppressWarnings("unchecked")
    void should_return_JudicialUserProfileRow_with_single_record_with_per_id_null_exceeds_threshold() {
        judicialUserProfileMock1.setPerId(null);
        judicialUserProfileMock1.setFullName(null);
        Exchange exchangeMock = mock(Exchange.class);
        Message messageMock = mock(Message.class);

        when(exchangeMock.getContext()).thenReturn(new DefaultCamelContext());
        final JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        final PlatformTransactionManager platformTransactionManager = mock(PlatformTransactionManager.class);
        final TransactionStatus transactionStatus = mock(TransactionStatus.class);

        when(exchangeMock.getIn()).thenReturn(messageMock);
        when(exchangeMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getBody()).thenReturn(judicialUserProfileMock1);
        RouteProperties routeProperties = new RouteProperties();
        routeProperties.setFileName("test");

        setField(judicialUserProfileProcessor, "jsrThresholdLimit", 1);
        setField(judicialUserProfileJsrValidatorInitializer, "camelContext", camelContext);
        setField(judicialUserProfileJsrValidatorInitializer, "jdbcTemplate", jdbcTemplate);
        setField(judicialUserProfileJsrValidatorInitializer,
            "platformTransactionManager", platformTransactionManager);

        int[][] intArray = new int[1][];
        when(jdbcTemplate.batchUpdate(anyString(), anyList(), anyInt(), any())).thenReturn(intArray);
        when(platformTransactionManager.getTransaction(any())).thenReturn(transactionStatus);
        doNothing().when(platformTransactionManager).commit(transactionStatus);
        when(exchangeMock.getIn().getHeader(ROUTE_DETAILS)).thenReturn(routeProperties);

        Assertions.assertThrows(RouteFailedException.class, () ->
            judicialUserProfileProcessor.process(exchangeMock));
        assertThat(((JudicialUserProfile) exchangeMock.getMessage().getBody())).isSameAs(judicialUserProfileMock1);
    }

    @Test
    void should_return_JudicialUserProfileRow_with_empty_response() {
        when(jdbcTemplate.queryForList("dummysql", String.class))
            .thenReturn(new ArrayList<>());
        when(messageMock.getBody()).thenReturn(new ArrayList<>());
        judicialUserProfileProcessor.process(exchangeMock);
        List<JudicialUserProfile> judicialUserProfiles = new ArrayList<>();
        judicialUserProfiles.add(judicialUserProfileMock1);
        assertThat(judicialUserProfileProcessor.getValidPerIdInUserProfile()).isSameAs(emptySet());
    }

    @Test
    void testLoadPerId() {
        when(jdbcTemplate.queryForList("dummysql", String.class))
            .thenReturn(ImmutableList.of(PERID_1, PERID_2, "0"));
        List<String> resultList = invokeMethod(judicialUserProfileProcessor, "loadPerId");
        assertThat(resultList).hasSize(3);
    }
}
