package uk.gov.hmcts.reform.juddata.camel.util;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.FileStatus;
import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.RouteProperties;
import uk.gov.hmcts.reform.data.ingestion.camel.service.IEmailService;
import uk.gov.hmcts.reform.data.ingestion.camel.service.dto.Email;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.configuration.EmailConfiguration;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.DataLoadUtil.getFileDetails;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.DataLoadUtil.registerFileStatusBean;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.PARTIAL_SUCCESS;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.ROUTE_DETAILS;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.SCHEDULER_NAME;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.SCHEDULER_START_TIME;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.DATE_PATTERN;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.NEW_LINE;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.PER_CODE_OBJECT_ID_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.PER_CODE_OBJECT_ID_FIELD;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.USERPROFILE;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.CONTENT_TYPE_PLAIN;

@Component
public class JrdUserProfileUtil {

    private static final String ONE_OBJECT_ID_HAVING_MULTIPLE_PERSONAL_CODES_MESSAGE
            = "Profiles with one Object ID having multiple Personal Codes";
    private static final String ONE_PERSONAL_CODE_HAVING_MULTIPLE_OBJECT_IDS_MESSAGE
            = "Profiles with one Personal Code having multiple Object IDs";

    @Autowired
    @Qualifier("springJdbcTransactionManager")
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    CamelContext camelContext;

    @Autowired
    @Qualifier("springJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @Value("${invalid-jsr-sql}")
    String invalidJsrSql;

    @Autowired
    EmailConfiguration emailConfiguration;

    @Autowired
    IEmailService emailService;



    public List<JudicialUserProfile> removeInvalidRecords(List<JudicialUserProfile> judicialUserProfiles,
                                                          Exchange exchange) {

        var userProfiles = new ArrayList<>(judicialUserProfiles);

        var invalidRecords = getInvalidRecords(userProfiles);

        //remove and audit the invalid entries from the original user profile list
        if (!CollectionUtils.isEmpty(invalidRecords)) {
            remove(invalidRecords, userProfiles);

            audit(invalidRecords, exchange);

            sendEmail(judicialUserProfiles);
        }

        return List.copyOf(userProfiles);
    }

    public List<JudicialUserProfile> filterByObjectId(List<JudicialUserProfile> userProfiles) {
        var filteredList = new ArrayList<JudicialUserProfile>();
        //filter by object ids
        Map<String, List<JudicialUserProfile>> filteredMap = filterAndGroupByObjectId(userProfiles);
        filteredMap.forEach((k, v) -> filteredList.addAll(v));

        return List.copyOf(filteredList);
    }

    public List<JudicialUserProfile> filterByPersonalCode(List<JudicialUserProfile> userProfiles) {
        var filteredList = new ArrayList<JudicialUserProfile>();
        //filter by personal codes
        Map<String, List<JudicialUserProfile>> filteredMap = filterAndGroupByPersonalCode(userProfiles);
        filteredMap.forEach((k, v) -> filteredList.addAll(v));

        return List.copyOf(filteredList);
    }

    /**
     * Iterate through the list of user profiles and group them by object id,
     * remove the entries where the object id is correctly associated with no more than 1 distinct personal code,
     * respecting 1-1 mapping between object ids and personal codes.
     * @param userProfiles original list of user profiles
     * @return map containing only the object ids where the associated personal codes are invalid
     */
    private Map<String, List<JudicialUserProfile>> filterAndGroupByObjectId(
            List<JudicialUserProfile> userProfiles) {

        return userProfiles.stream()
                .filter(userProfile -> StringUtils.isNotBlank(userProfile.getObjectId()))
                .collect(collectingAndThen(groupingBy(JudicialUserProfile::getObjectId), map -> {
                    map.values().removeIf(l -> l.stream()
                            .map(JudicialUserProfile::getPersonalCode)
                            .distinct().count() < 2);
                    return map; }));
    }


    /**
     * Iterate through the list of user profiles and group them by personal code,
     * remove the entries where the personal code is correctly associated with no more than 1 distinct object id,
     * respecting 1-1 mapping between personal codes and object ids.
     * @param userProfiles original list of user profiles
     * @return map containing only the personal codes where the associated object ids are invalid
     */
    private Map<String, List<JudicialUserProfile>> filterAndGroupByPersonalCode(
            List<JudicialUserProfile> userProfiles) {

        return userProfiles.stream()
                .filter(userProfile -> StringUtils.isNotBlank(userProfile.getObjectId()))
                .collect(collectingAndThen(groupingBy(JudicialUserProfile::getPersonalCode), map -> {
                    map.values().removeIf(l -> l.stream()
                            .map(JudicialUserProfile::getObjectId)
                            .distinct().count() < 2);
                    return map; }));
    }

    private List<JudicialUserProfile> getInvalidRecords(List<JudicialUserProfile> userProfiles) {
        //get list of user profiles with invalid object id
        var userProfilesWithInvalidObjectIds = filterByObjectId(userProfiles);

        //get list of user profiles with invalid personal code
        var userProfilesWithInvalidPersonalCodes = filterByPersonalCode(userProfiles);

        var invalidRecordsList = new ArrayList<JudicialUserProfile>();
        Stream.of(userProfilesWithInvalidObjectIds, userProfilesWithInvalidPersonalCodes)
                .forEach(invalidRecordsList::addAll);

        return List.copyOf(invalidRecordsList);
    }

    public void remove(List<JudicialUserProfile> userProfilesToBeDeleted, List<JudicialUserProfile> userProfiles) {
        var erroneousUserProfileSet = new HashSet<>(userProfilesToBeDeleted);
        userProfiles.removeIf(erroneousUserProfileSet::contains);
    }

    public void audit(List<JudicialUserProfile> invalidUserProfiles, Exchange exchange) {
        var routeProperties = (RouteProperties) exchange.getIn().getHeader(ROUTE_DETAILS);
        FileStatus fileStatus = getFileDetails(exchange.getContext(), routeProperties.getFileName());

        if (nonNull(invalidUserProfiles) && !CollectionUtils.isEmpty(invalidUserProfiles)) {
            auditInvalidUserProfiles(invalidUserProfiles, routeProperties.getTableName());

            fileStatus.setAuditStatus(PARTIAL_SUCCESS);
            registerFileStatusBean(applicationContext, routeProperties.getFileName(), fileStatus,
                    exchange.getContext());
        }
    }

    private void auditInvalidUserProfiles(List<JudicialUserProfile> invalidUserProfiles, String tableName) {
        var def = new DefaultTransactionDefinition();
        def.setName("Jsr exception logs");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        Map<String, String> globalOptions = camelContext.getGlobalOptions();
        String schedulerTime = globalOptions.get(SCHEDULER_START_TIME);

        jdbcTemplate.batchUpdate(
                invalidJsrSql,
                invalidUserProfiles,
                10,
                new ParameterizedPreparedStatementSetter<>() {
                    public void setValues(PreparedStatement ps, JudicialUserProfile argument) throws SQLException {
                        ps.setString(1, tableName);
                        ps.setTimestamp(2, new Timestamp(Long.parseLong(schedulerTime)));
                        ps.setString(3, globalOptions.get(SCHEDULER_NAME));
                        ps.setString(4, argument.getPerId());
                        ps.setString(5, PER_CODE_OBJECT_ID_FIELD);
                        ps.setString(6, PER_CODE_OBJECT_ID_ERROR_MESSAGE);
                        ps.setTimestamp(7, new Timestamp(new Date().getTime()));
                        ps.setLong(8, argument.getRowId());
                    }
                });

        TransactionStatus status = platformTransactionManager.getTransaction(def);
        platformTransactionManager.commit(status);
    }

    public int sendEmail(List<JudicialUserProfile> userProfiles) {
        EmailConfiguration.MailTypeConfig mailTypeConfig = emailConfiguration.getMailTypes().get(USERPROFILE);

        if (mailTypeConfig.isEnabled()) {
            Email email = Email.builder()
                    .contentType(CONTENT_TYPE_PLAIN)
                    .from(mailTypeConfig.getFrom())
                    .to(mailTypeConfig.getTo())
                    .messageBody(String.format(mailTypeConfig.getBody(), createMessageBody(userProfiles)))
                    .subject(String.format(mailTypeConfig.getSubject(), LocalDate.now()
                            .format(DateTimeFormatter.ofPattern(DATE_PATTERN))))
                    .build();
            return emailService.sendEmail(email);
        }
        return -1;
    }

    private String createMessageBody(List<JudicialUserProfile> userProfiles) {
        var messageBody = new StringBuilder();
        var invalidObjectIdRows =  new StringBuilder();
        var invalidPersonalCodeRows =  new StringBuilder();

        messageBody.append(NEW_LINE);
        messageBody.append(ONE_OBJECT_ID_HAVING_MULTIPLE_PERSONAL_CODES_MESSAGE);
        messageBody.append(NEW_LINE);
        messageBody.append(createRows(invalidObjectIdRows, filterByObjectId(userProfiles)));
        messageBody.append(NEW_LINE);
        messageBody.append("\n=====================================================================================\n");
        messageBody.append(NEW_LINE);
        messageBody.append(ONE_PERSONAL_CODE_HAVING_MULTIPLE_OBJECT_IDS_MESSAGE);
        messageBody.append(NEW_LINE);
        messageBody.append(createRows(invalidPersonalCodeRows, filterByPersonalCode(userProfiles)));
        messageBody.append(NEW_LINE);

        return messageBody.toString();
    }

    private String createRows(StringBuilder messageBody, List<JudicialUserProfile> userProfiles) {
        messageBody.append(String.format("%-30s %50s %70s %n", "Per Code", "Object ID", "Per Id"));

        userProfiles.forEach(judicialUserProfile ->
                messageBody.append(String.format("%-30s ",judicialUserProfile.getPersonalCode()))
                        .append(String.format("%50s ",judicialUserProfile.getObjectId()))
                        .append(String.format("%40s",judicialUserProfile.getPerId()))
                        .append("\n"));

        return messageBody.toString();
    }

}
