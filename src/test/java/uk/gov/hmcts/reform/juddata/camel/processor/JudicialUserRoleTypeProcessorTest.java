package uk.gov.hmcts.reform.juddata.camel.processor;

import com.google.common.collect.ImmutableSet;
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.RouteProperties;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.JsrValidatorInitializer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserRoleType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserProfileMock;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialUserRoleType;

class JudicialUserRoleTypeProcessorTest {

    private Validator validator;
    String date = "2017-10-01 00:00:00.000";

    Date currentDate = new Date();

    LocalDateTime dateTime = LocalDateTime.now();

    JudicialUserRoleTypeProcessor judicialUserRoleTypeProcessor = spy(new JudicialUserRoleTypeProcessor());

    JsrValidatorInitializer<JudicialUserRoleType> judicialUserRoleTypeJsrValidatorInitializer;

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


    @BeforeEach
    public void setup() {

        judicialUserRoleTypeJsrValidatorInitializer = new JsrValidatorInitializer<>();

        setField(judicialUserRoleTypeProcessor,
                "judicialUserRoleTypeJsrValidatorInitializer", judicialUserRoleTypeJsrValidatorInitializer);
        setField(judicialUserRoleTypeProcessor, "judicialUserProfileProcessor",
                judicialUserProfileProcessor);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        setField(judicialUserRoleTypeJsrValidatorInitializer, "validator", validator);
        setField(judicialUserRoleTypeJsrValidatorInitializer, "camelContext", camelContext);
        setField(judicialUserRoleTypeJsrValidatorInitializer, "jdbcTemplate", jdbcTemplate);
        setField(judicialUserRoleTypeJsrValidatorInitializer, "platformTransactionManager",
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

        setField(judicialUserRoleTypeProcessor, "applicationContext", applicationContext);
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
    void shouldReturnJudicialUserRoleTypeResponse() throws Exception {
        List<JudicialUserRoleType> judicialUserRoleTypes = new ArrayList<>();
        JudicialUserRoleType judicialUserRoleTypeMock1 = createJudicialUserRoleType();
        JudicialUserRoleType judicialUserRoleTypeMock2 = createJudicialUserRoleType();
        judicialUserRoleTypes.add(judicialUserRoleTypeMock1);
        judicialUserRoleTypes.add(judicialUserRoleTypeMock2);


        when(messageMock.getBody()).thenReturn(judicialUserRoleTypes);
        judicialUserRoleTypeProcessor.process(exchangeMock);

        assertThat(((List) exchangeMock.getMessage().getBody())).hasSize(2);
        verify(exchangeMock, times(2)).getMessage();
        verify(judicialUserRoleTypeProcessor).audit(judicialUserRoleTypeJsrValidatorInitializer, exchangeMock);
        verify(messageMock).setBody(any());
    }

    @Test
    void testFilterJudicialUserRoleTypeRecordsForForeignKeyViolation() {

        List<JudicialUserRoleType> judicialUserRoleTypes = new ArrayList<>();

        JudicialUserRoleType judicialUserRoleTypeMock1 = createJudicialUserRoleType();
        judicialUserRoleTypeMock1.setPerId(PERID_1);
        JudicialUserRoleType judicialUserRoleTypeMock2 = createJudicialUserRoleType();
        judicialUserRoleTypeMock2.setPerId(PERID_2);


        judicialUserRoleTypes.add(judicialUserRoleTypeMock1);
        judicialUserRoleTypes.add(judicialUserRoleTypeMock2);

        JudicialUserProfile judicialUserProfileMock = createJudicialUserProfileMock(currentDate, dateTime, PERID_1);
        JudicialUserProfile judicialUserProfileMock2 = createJudicialUserProfileMock(currentDate, dateTime, PERID_3);

        List<JudicialUserProfile> judicialUserProfiles = new ArrayList<>();
        judicialUserProfiles.add(judicialUserProfileMock);
        judicialUserProfiles.add(judicialUserProfileMock2);

        when(judicialUserProfileProcessor.getInvalidRecords()).thenReturn(judicialUserProfiles);
        when(judicialUserProfileProcessor.getValidPerIdInUserProfile()).thenReturn(Collections.singleton(PERID_2));

        invokeMethod(judicialUserRoleTypeProcessor, "filterAuthorizationsRecordsForForeignKeyViolation",
                judicialUserRoleTypes, exchangeMock);
        assertEquals(1, judicialUserRoleTypes.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnJudicialUserRoleTypeRowResponse() {

        List<JudicialUserRoleType> judicialUserRoleTypes = new ArrayList<>();

        JudicialUserRoleType judicialUserRoleTypeMock1 = createJudicialUserRoleType();
        judicialUserRoleTypeMock1.setPerId(PERID_1);
        JudicialUserRoleType judicialUserRoleTypeMock2 = createJudicialUserRoleType();
        judicialUserRoleTypeMock2.setPerId(PERID_2);


        judicialUserRoleTypes.add(judicialUserRoleTypeMock1);
        judicialUserRoleTypes.add(judicialUserRoleTypeMock2);

        when(messageMock.getBody()).thenReturn(judicialUserRoleTypes);
        judicialUserRoleTypeProcessor.process(exchangeMock);
        assertThat(((List) exchangeMock.getMessage().getBody())).hasSize(2);
        assertThat(((List<JudicialUserRoleType>) exchangeMock.getMessage().getBody()))
                .isSameAs(judicialUserRoleTypes);

        verify(exchangeMock, times(4)).getIn();
        verify(exchangeMock, times(3)).getMessage();
        verify(messageMock, times(4)).getBody();
        verify(judicialUserRoleTypeProcessor).audit(any(), any());
        verify(messageMock).setBody(any());
        verify(judicialUserRoleTypeProcessor).filterInvalidUserProfileRecords(anyList(),
                isNull(),
                any(), any(),
                isNull());
        JudicialUserProfile judicialUserProfileMock = createJudicialUserProfileMock(currentDate, dateTime, PERID_1);
        JudicialUserProfile judicialUserProfileMock2 = createJudicialUserProfileMock(currentDate, dateTime, PERID_3);

        List<JudicialUserProfile> judicialUserProfiles = new ArrayList<>();
        judicialUserProfiles.add(judicialUserProfileMock);
        judicialUserProfiles.add(judicialUserProfileMock2);
        when(judicialUserProfileProcessor.getInvalidRecords()).thenReturn(judicialUserProfiles);
        when(judicialUserProfileProcessor.getValidPerIdInUserProfile()).thenReturn(Collections.singleton(PERID_2));

        invokeMethod(judicialUserRoleTypeProcessor, "filterInvalidUserProfileRecords",
                judicialUserRoleTypes,judicialUserProfiles,judicialUserRoleTypeJsrValidatorInitializer,
                exchangeMock,"JudicialUserRoleType");
        assertEquals(1, judicialUserRoleTypes.size());
    }

}
