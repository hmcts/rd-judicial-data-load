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
    List<JudicialUserProfile> judicialUserProfilesWithNoObjectIds;

    @BeforeEach
    public void setUp() {
        createUserProfiles();
        setUpEmailConfig();

        RouteProperties routeProperties = new RouteProperties();
        routeProperties.setFileName("test");
        routeProperties.setTableName("test");

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
        JudicialUserProfile profile1 = new JudicialUserProfile();
        profile1.setPersonalCode("1234567");
        profile1.setObjectId("673sg825-7c34-5456-aa54-e126394793056");
        JudicialUserProfile profile2 = new JudicialUserProfile();
        profile2.setPersonalCode("1234567");
        profile2.setObjectId("673sg825-7c34-5456-aa54-e126394793056");
        JudicialUserProfile profile3 = new JudicialUserProfile();
        profile3.setPersonalCode("2034512");
        profile3.setObjectId("6427c339-79bd-47a7-b000-2356cb77c13b");
        JudicialUserProfile profile4 = new JudicialUserProfile();
        profile4.setPersonalCode("2034513");
        profile4.setObjectId("");
        judicialUserProfilesValidRecords = new ArrayList<>(Arrays.asList(profile1, profile2, profile3, profile4));

        JudicialUserProfile profile5 = new JudicialUserProfile();
        profile5.setPersonalCode("16023");
        profile5.setObjectId("0d3b6f60-40b5-45d6-b526-d38a212992d9");
        JudicialUserProfile profile6 = new JudicialUserProfile();
        profile6.setPersonalCode("16023");
        profile6.setObjectId("0d3b6f60-40b5-45d6-b526-d38a212992d9");
        JudicialUserProfile profile7 = new JudicialUserProfile();
        profile7.setPersonalCode("4925916");
        profile7.setObjectId("0d3b6f60-40b5-45d6-b526-d38a212992d9");
        JudicialUserProfile profile8 = new JudicialUserProfile();
        profile8.setPersonalCode("4925916");
        profile8.setObjectId("0d3b6f60-40b5-45d6-b526-d38a212992d9");
        judicialUserProfilesInvalidObjectIds = new ArrayList<>(Arrays.asList(profile5, profile6, profile7, profile8));

        JudicialUserProfile profile9 = new JudicialUserProfile();
        profile9.setPersonalCode("4927112");
        profile9.setObjectId("a01009ed-e6d1-47a3-add6-adf4365ca397");
        JudicialUserProfile profile10 = new JudicialUserProfile();
        profile10.setPersonalCode("4927112");
        profile10.setObjectId("bd30e3c4-c377-4da4-8eb5-d7b1ff71a4ba");
        JudicialUserProfile profile11 = new JudicialUserProfile();
        profile11.setPersonalCode("4927112");
        profile11.setObjectId("933fc903-4c39-4742-bb46-d69903835904");
        judicialUserProfilesInvalidPersonalCodes = new ArrayList<>(Arrays.asList(profile9, profile10, profile11));

        JudicialUserProfile profile12 = new JudicialUserProfile();
        profile12.setPersonalCode("1233456");
        profile12.setObjectId("");
        JudicialUserProfile profile13 = new JudicialUserProfile();
        profile13.setPersonalCode("1234556");
        profile13.setObjectId("");
        JudicialUserProfile profile14 = new JudicialUserProfile();
        profile14.setPersonalCode("1234345");
        profile14.setObjectId("");
        judicialUserProfilesWithNoObjectIds = new ArrayList<>(Arrays.asList(profile12, profile13, profile14));
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
        assertThat(resultList).isNotNull().hasSize(4).isEqualTo(judicialUserProfilesValidRecords);
        verify(jrdUserProfileUtil, times(1)).audit(anyList(), any());
        verify(emailService, times(1)).sendEmail(any(Email.class));
    }

    @Test
    void test_filter_and_remove_when_all_valid_profiles() {
        judicialUserProfiles.addAll(judicialUserProfilesValidRecords);

        List<JudicialUserProfile> resultList = jrdUserProfileUtil
                .removeInvalidRecords(judicialUserProfiles, exchangeMock);
        assertThat(resultList).isNotNull().hasSize(4).isEqualTo(judicialUserProfilesValidRecords);
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
        judicialUserProfiles.addAll(judicialUserProfilesWithNoObjectIds);
        judicialUserProfiles.addAll(judicialUserProfilesValidRecords);

        List<JudicialUserProfile> profiles = jrdUserProfileUtil.filterByPersonalCode(judicialUserProfiles);
        assertThat(profiles).isNotNull().hasSize(3).isEqualTo(judicialUserProfilesInvalidPersonalCodes);
    }

    @Test
    void test_filter_methods_when_profiles_with_empty_object_ids() {
        judicialUserProfiles.addAll(judicialUserProfilesWithNoObjectIds);

        List<JudicialUserProfile> filteredList1 = jrdUserProfileUtil.filterByObjectId(judicialUserProfiles);
        List<JudicialUserProfile> filteredList2 = jrdUserProfileUtil.filterByPersonalCode(judicialUserProfiles);

        assertThat(filteredList1).isEmpty();
        assertThat(filteredList2).isEmpty();
    }

    @Test
    void test_remove() {
        judicialUserProfiles.addAll(judicialUserProfilesInvalidPersonalCodes);

        jrdUserProfileUtil.remove(judicialUserProfilesInvalidPersonalCodes, judicialUserProfiles);
        assertThat(judicialUserProfiles).isNotNull().isEmpty();
    }

    @Test
    void test_audit() {
        judicialUserProfiles.addAll(judicialUserProfilesInvalidPersonalCodes);
        jrdUserProfileUtil.audit(judicialUserProfiles, exchangeMock);

        verify(platformTransactionManager,times(1)).commit(any());
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
