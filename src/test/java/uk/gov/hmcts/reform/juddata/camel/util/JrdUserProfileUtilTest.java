package uk.gov.hmcts.reform.juddata.camel.util;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.RouteProperties;
import uk.gov.hmcts.reform.data.ingestion.camel.service.EmailServiceImpl;
import uk.gov.hmcts.reform.data.ingestion.camel.service.dto.Email;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.configuration.EmailConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.ROUTE_DETAILS;

@ExtendWith(MockitoExtension.class)
class JrdUserProfileUtilTest {

    @InjectMocks
    JrdUserProfileUtil jrdUserProfileUtil = spy(JrdUserProfileUtil.class);

    @Mock
    Exchange exchangeMock;
    @Mock
    Message messageMock;
    @Mock
    CamelContext camelContext;
    @Mock
    JdbcTemplate jdbcTemplate;
    @Mock
    PlatformTransactionManager platformTransactionManager;
    @Mock
    TransactionStatus transactionStatus;
    @Mock
    EmailServiceImpl emailService;

    ApplicationContext applicationContext = mock(ConfigurableApplicationContext.class);
    ConfigurableListableBeanFactory configurableListableBeanFactory = mock(ConfigurableListableBeanFactory.class);
    EmailConfiguration.MailTypeConfig mailTypeConfig = new EmailConfiguration.MailTypeConfig();

    List<JudicialUserProfile> judicialUserProfiles;
    List<JudicialUserProfile> judicialUserProfilesValidRecords;
    List<JudicialUserProfile> judicialUserProfilesInvalidObjectIds;
    List<JudicialUserProfile> judicialUserProfilesInvalidPersonalCodes;

    @BeforeEach
    public void setUp() {
        createUserProfiles();
        setUpEmailConfig();

        RouteProperties routeProperties = new RouteProperties();
        routeProperties.setFileName("test");

        setField(jrdUserProfileUtil, "applicationContext", applicationContext);

        when(exchangeMock.getIn()).thenReturn(messageMock);
        lenient().when(exchangeMock.getIn().getHeader(ROUTE_DETAILS)).thenReturn(routeProperties);
        lenient().when(exchangeMock.getContext()).thenReturn(new DefaultCamelContext());
        lenient().when(exchangeMock.getMessage()).thenReturn(messageMock);
        when(((ConfigurableApplicationContext)
                applicationContext).getBeanFactory()).thenReturn(configurableListableBeanFactory);

        int[][] intArray = new int[1][];
        lenient().when(jdbcTemplate.batchUpdate(anyString(), anyList(), anyInt(), any())).thenReturn(intArray);
        lenient().when(platformTransactionManager.getTransaction(any())).thenReturn(transactionStatus);
        lenient().doNothing().when(platformTransactionManager).commit(transactionStatus);
    }

    private void createUserProfiles() {
        judicialUserProfiles = new ArrayList<>();
        judicialUserProfilesValidRecords = new ArrayList<>(Arrays.asList(
                JudicialUserProfile.builder().personalCode("1234567").objectId("673sg825-7c34-5456-aa54-e126394793056")
                        .build(),
                JudicialUserProfile.builder().personalCode("1234567").objectId("673sg825-7c34-5456-aa54-e126394793056")
                        .build(),
                JudicialUserProfile.builder().personalCode("2034512").objectId("6427c339-79bd-47a7-b000-2356cb77c13b")
                        .build()
        ));
        judicialUserProfilesInvalidObjectIds = new ArrayList<>(Arrays.asList(
                JudicialUserProfile.builder().personalCode("16023").objectId("0d3b6f60-40b5-45d6-b526-d38a212992d9")
                        .build(),
                JudicialUserProfile.builder().personalCode("16023").objectId("0d3b6f60-40b5-45d6-b526-d38a212992d9")
                        .build(),
                JudicialUserProfile.builder().personalCode("4925916").objectId("0d3b6f60-40b5-45d6-b526-d38a212992d9")
                        .build(),
                JudicialUserProfile.builder().personalCode("4925916").objectId("0d3b6f60-40b5-45d6-b526-d38a212992d9")
                        .build()
        ));
        judicialUserProfilesInvalidPersonalCodes = new ArrayList<>(Arrays.asList(
                JudicialUserProfile.builder().personalCode("4927112").objectId("a01009ed-e6d1-47a3-add6-adf4365ca397")
                        .build(),
                JudicialUserProfile.builder().personalCode("4927112").objectId("bd30e3c4-c377-4da4-8eb5-d7b1ff71a4ba")
                        .build(),
                JudicialUserProfile.builder().personalCode("4927112").objectId("933fc903-4c39-4742-bb46-d69903835904")
                        .build()
        ));
    }

    private void setUpEmailConfig() {
        mailTypeConfig.setEnabled(true);
        mailTypeConfig.setSubject("Official Sensitive: JRD - Incorrect JO Profile Configurations - %s");
        mailTypeConfig.setBody("Following JO profiles were deleted : \n %s");
        mailTypeConfig.setFrom("test@test.com");
        mailTypeConfig.setTo(List.of("test@test.com"));
        EmailConfiguration emailConfiguration = new EmailConfiguration();
        emailConfiguration.setMailTypes(Map.of("userprofile", mailTypeConfig));
        setField(jrdUserProfileUtil, "emailConfiguration", emailConfiguration);
    }

    @Test
    void test_filter_and_remove() {
        judicialUserProfiles.addAll(judicialUserProfilesValidRecords);
        judicialUserProfiles.addAll(judicialUserProfilesInvalidObjectIds);
        judicialUserProfiles.addAll(judicialUserProfilesInvalidPersonalCodes);

        List<JudicialUserProfile> resultList = jrdUserProfileUtil
                .removeInvalidRecords(judicialUserProfiles, exchangeMock);
        assertThat(resultList).isNotNull().hasSize(3).isEqualTo(judicialUserProfilesValidRecords);
        verify(jrdUserProfileUtil, times(1)).audit(anyList(), any());
        verify(emailService, times(1)).sendEmail(any(Email.class));
    }

    @Test
    void test_filter_and_remove_when_all_valid_profiles() {
        judicialUserProfiles.addAll(judicialUserProfilesValidRecords);

        List<JudicialUserProfile> resultList = jrdUserProfileUtil
                .removeInvalidRecords(judicialUserProfiles, exchangeMock);
        assertThat(resultList).isNotNull().hasSize(3).isEqualTo(judicialUserProfilesValidRecords);
        verify(jrdUserProfileUtil, times(0)).remove(anyList(), any());
        verify(jrdUserProfileUtil, times(0)).audit(anyList(), any());
        verify(emailService, times(0)).sendEmail(any(Email.class));
    }

    @Test
    void test_filter_and_remove_when_all_invalid_profiles() {
        judicialUserProfiles.addAll(judicialUserProfilesInvalidObjectIds);
        judicialUserProfiles.addAll(judicialUserProfilesInvalidPersonalCodes);

        List<JudicialUserProfile> resultList = jrdUserProfileUtil
                .removeInvalidRecords(judicialUserProfiles, exchangeMock);
        assertThat(resultList).isEmpty();
        verify(jrdUserProfileUtil, times(1)).audit(judicialUserProfiles, exchangeMock);
        verify(emailService, times(1)).sendEmail(any(Email.class));
    }

    @Test
    void test_filter_by_object_id() {
        judicialUserProfiles.addAll(judicialUserProfilesInvalidObjectIds);
        judicialUserProfiles.addAll(judicialUserProfilesValidRecords);

        List<JudicialUserProfile> profiles = jrdUserProfileUtil.filterByObjectId(judicialUserProfiles);
        assertThat(profiles).isNotNull().hasSize(4).isEqualTo(judicialUserProfilesInvalidObjectIds);
    }

    @Test
    void test_filter_by_personal_code() {
        judicialUserProfiles.addAll(judicialUserProfilesInvalidPersonalCodes);
        judicialUserProfiles.addAll(judicialUserProfilesValidRecords);

        List<JudicialUserProfile> profiles = jrdUserProfileUtil.filterByPersonalCode(judicialUserProfiles);
        assertThat(profiles).isNotNull().hasSize(3).isEqualTo(judicialUserProfilesInvalidPersonalCodes);
    }

    @Test
    void test_remove() {
        judicialUserProfiles.addAll(judicialUserProfilesInvalidPersonalCodes);

        jrdUserProfileUtil.remove(judicialUserProfilesInvalidPersonalCodes, judicialUserProfiles);
        assertThat(judicialUserProfiles).isNotNull().isEmpty();
    }

    @Test
    void test_sendEmail_when_email_disabled() {
        mailTypeConfig.setEnabled(false);

        assertEquals(-1, jrdUserProfileUtil.sendEmail(judicialUserProfiles));
        verify(emailService, times(0)).sendEmail(any(Email.class));
    }

    @Test
    void test_sendEmail_when_invalid_records() {
        when(emailService.sendEmail(any(Email.class))).thenReturn(202);

        assertEquals(202, jrdUserProfileUtil
                .sendEmail(judicialUserProfiles));
        verify(emailService, times(1)).sendEmail(any(Email.class));
    }

}
