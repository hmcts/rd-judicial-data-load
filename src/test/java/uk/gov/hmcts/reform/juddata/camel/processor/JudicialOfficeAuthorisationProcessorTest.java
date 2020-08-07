package uk.gov.hmcts.reform.juddata.camel.processor;

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
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.ROUTE_DETAILS;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialOfficeAuthorisation;

import java.util.ArrayList;
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
import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.RouteProperties;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.JsrValidatorInitializer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAuthorisation;

public class JudicialOfficeAuthorisationProcessorTest {

    private Validator validator;
    String date = "2017-10-01 00:00:00.000";
    JudicialOfficeAuthorisation judicialOfficeAuthorisation1 = createJudicialOfficeAuthorisation(date);

    JudicialOfficeAuthorisation judicialOfficeAuthorisation2 = createJudicialOfficeAuthorisation(date);

    JudicialOfficeAuthorisationProcessor judicialOfficeAuthorisationProcessor;

    private JsrValidatorInitializer<JudicialOfficeAuthorisation> judicialOfficeAuthorisationJsrValidatorInitializer;
    JudicialUserProfileProcessor judicialUserProfileProcessor = new JudicialUserProfileProcessor();
    CamelContext camelContext = new DefaultCamelContext();


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
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_return_JudicialOfficeAppointmentRow_response() {

        List<JudicialOfficeAuthorisation> judicialOfficeAuthorisations = new ArrayList<>();
        judicialOfficeAuthorisations.add(judicialOfficeAuthorisation1);
        judicialOfficeAuthorisations.add(judicialOfficeAuthorisation2);

        Exchange exchangeMock = mock(Exchange.class);
        Message messageMock = mock(Message.class);
        when(exchangeMock.getIn()).thenReturn(messageMock);
        when(exchangeMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getBody()).thenReturn(judicialOfficeAuthorisations);
        judicialOfficeAuthorisationProcessor.process(exchangeMock);
        assertThat(((List) exchangeMock.getMessage().getBody()).size()).isEqualTo(2);
        assertThat(((List<JudicialOfficeAppointment>) exchangeMock.getMessage().getBody()))
                .isSameAs(judicialOfficeAuthorisations);

        verify(exchangeMock, times(2)).getIn();
        verify(exchangeMock, times(3)).getMessage();
        verify(messageMock, times(4)).getBody();
    }

    @Test
    public void should_return_JudicialOfficeAppointmentRow_with_single_record_response() {

        Exchange exchangeMock = mock(Exchange.class);
        Message messageMock = mock(Message.class);
        when(exchangeMock.getIn()).thenReturn(messageMock);
        when(exchangeMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getBody()).thenReturn(judicialOfficeAuthorisation1);

        judicialOfficeAuthorisationProcessor.process(exchangeMock);
        assertThat(((JudicialOfficeAuthorisation) exchangeMock.getMessage().getBody()))
                .isSameAs(judicialOfficeAuthorisation1);
        verify(exchangeMock, times(2)).getIn();
        verify(exchangeMock, times(2)).getMessage();
        verify(messageMock, times(3)).getBody();
    }

    @Test
    public void should_return_JudicialOfficeAuthorizationRow_with_single_record_with_elinks_id_null_response() {

        judicialOfficeAuthorisation1.setElinksId(null);
        Exchange exchangeMock = mock(Exchange.class);
        Message messageMock = mock(Message.class);
        final JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        final PlatformTransactionManager platformTransactionManager = mock(PlatformTransactionManager.class);
        final TransactionStatus transactionStatus = mock(TransactionStatus.class);

        when(exchangeMock.getIn()).thenReturn(messageMock);
        when(exchangeMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getBody()).thenReturn(judicialOfficeAuthorisation1);
        RouteProperties routeProperties = new RouteProperties();
        routeProperties.setTableName("test");

        setField(judicialOfficeAuthorisationProcessor, "jsrThresholdLimit", 5);
        setField(judicialOfficeAuthorisationJsrValidatorInitializer, "camelContext", camelContext);
        setField(judicialOfficeAuthorisationJsrValidatorInitializer, "jdbcTemplate", jdbcTemplate);
        setField(judicialOfficeAuthorisationJsrValidatorInitializer,
                "platformTransactionManager", platformTransactionManager);

        int[][] intArray = new int[1][];
        when(jdbcTemplate.batchUpdate(anyString(), anyList(), anyInt(), any())).thenReturn(intArray);
        when(platformTransactionManager.getTransaction(any())).thenReturn(transactionStatus);
        when(exchangeMock.getContext()).thenReturn(new DefaultCamelContext());
        doNothing().when(platformTransactionManager).commit(transactionStatus);
        when(exchangeMock.getIn().getHeader(ROUTE_DETAILS)).thenReturn(routeProperties);
        judicialOfficeAuthorisationProcessor.process(exchangeMock);
        assertThat(((JudicialOfficeAuthorisation) exchangeMock.getMessage().getBody()))
                .isSameAs(judicialOfficeAuthorisation1);
        verify(exchangeMock, times(4)).getIn();
        verify(exchangeMock, times(2)).getMessage();
        verify(messageMock, times(3)).getBody();
        verify(platformTransactionManager, times(1)).getTransaction(any());
        verify(exchangeMock, times(1)).getContext();
    }
}
