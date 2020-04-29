package uk.gov.hmcts.reform.juddata.camel.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserProfileMock;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.ROUTE_DETAILS;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.exception.RouteFailedException;
import uk.gov.hmcts.reform.juddata.camel.route.beans.RouteProperties;
import uk.gov.hmcts.reform.juddata.camel.validator.JsrValidatorInitializer;

public class JudicialUserProfileProcessorTest {

    Date currentDate = new Date();

    LocalDateTime dateTime = LocalDateTime.now();

    private JudicialUserProfile judicialUserProfileMock1 = createJudicialUserProfileMock(currentDate, dateTime);

    private JudicialUserProfile judicialUserProfileMock2 = createJudicialUserProfileMock(currentDate, dateTime);

    private JudicialUserProfileProcessor judicialUserProfileProcessor;

    private JsrValidatorInitializer<JudicialUserProfile> judicialUserProfileJsrValidatorInitializer;

    private Validator validator;

    CamelContext camelContext = new DefaultCamelContext();

    @Before
    public void setup() {

        judicialUserProfileProcessor = new JudicialUserProfileProcessor();
        judicialUserProfileJsrValidatorInitializer
                = new JsrValidatorInitializer<>();
        setField(judicialUserProfileProcessor,
                "judicialUserProfileJsrValidatorInitializer", judicialUserProfileJsrValidatorInitializer);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        setField(judicialUserProfileJsrValidatorInitializer, "validator", validator);
    }

    @Test
    public void should_return_JudicialOfficeAuthorisationRow_response() {

        List<JudicialUserProfile> judicialUserProfiles = new ArrayList<>();
        judicialUserProfiles.add(judicialUserProfileMock1);
        judicialUserProfiles.add(judicialUserProfileMock2);

        Exchange exchangeMock = mock(Exchange.class);
        Message messageMock = mock(Message.class);
        when(exchangeMock.getIn()).thenReturn(messageMock);
        when(exchangeMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getBody()).thenReturn(judicialUserProfiles);
        judicialUserProfileProcessor.process(exchangeMock);
        assertThat(((List) exchangeMock.getMessage().getBody()).size()).isEqualTo(2);
    }

    @Test
    public void should_return_JudicialOfficeAuthorisationRow_with_single_record_response() {

        Exchange exchangeMock = mock(Exchange.class);
        Message messageMock = mock(Message.class);
        when(exchangeMock.getContext()).thenReturn(new DefaultCamelContext());
        when(exchangeMock.getIn()).thenReturn(messageMock);
        when(exchangeMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getBody()).thenReturn(judicialUserProfileMock1);

        judicialUserProfileProcessor.process(exchangeMock);
        List<JudicialUserProfile> judicialUserProfiles = new ArrayList<>();
        judicialUserProfiles.add(judicialUserProfileMock1);

        assertThat(((JudicialUserProfile) exchangeMock.getMessage().getBody())).isSameAs(judicialUserProfileMock1);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_return_JudicialOfficeAuthorisationRow_with_single_record_with_elinks_id_nullresponse() {

        judicialUserProfileMock1.setElinksId(null);
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
        routeProperties.setTableName("test");

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
        assertThat(((JudicialUserProfile) exchangeMock.getMessage().getBody())).isSameAs(judicialUserProfileMock1);
    }

    @Test(expected = RouteFailedException.class)
    @SuppressWarnings("unchecked")
    public void should_return_JudicialOfficeAuthorisationRow_with_single_record_with_elinks_id_null_exceeds_threshold() {
        judicialUserProfileMock1.setElinksId(null);
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
        routeProperties.setTableName("test");

        setField(judicialUserProfileProcessor, "jsrThresholdLimit", 0);
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
        assertThat(((JudicialUserProfile) exchangeMock.getMessage().getBody())).isSameAs(judicialUserProfileMock1);
    }
}
