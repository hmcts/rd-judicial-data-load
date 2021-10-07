package uk.gov.hmcts.reform.juddata.camel.util;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
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
import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.RouteProperties;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.PER_CODE_OBJECT_ID_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.PER_CODE_OBJECT_ID_FIELD;


@Component
public class JrdUserProfileUtil {

    @Autowired
    @Qualifier("springJdbcTransactionManager")
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    protected ApplicationContext applicationContext;

    @Autowired
    CamelContext camelContext;

    @Autowired
    @Qualifier("springJdbcTemplate")
    protected JdbcTemplate jdbcTemplate;

    @Value("${invalid-jsr-sql}")
    String invalidJsrSql;

    public List<JudicialUserProfile> removeInvalidRecords(List<JudicialUserProfile> judicialUserProfiles,
                                                          Exchange exchange) {

        var userProfiles = new ArrayList<>(judicialUserProfiles);

        var invalidRecords = getInvalidRecords(userProfiles);

        //remove the invalid entries from the original user profile list
        remove(invalidRecords, userProfiles);

        audit(invalidRecords, exchange);

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
     * remove the entries where the object id is correctly associated with no more than 1 distinct personal code.
     * @param userProfiles original list of user profiles
     * @return map containing only the object ids where the associated personal codes are invalid
     */
    private Map<String, List<JudicialUserProfile>> filterAndGroupByObjectId(
            List<JudicialUserProfile> userProfiles) {

        return userProfiles.stream()
                .filter(userProfile -> nonNull(userProfile.getObjectId()))
                .collect(collectingAndThen(groupingBy(JudicialUserProfile::getObjectId), map -> {
                    map.values().removeIf(l -> l.stream()
                            .map(JudicialUserProfile::getPersonalCode)
                            .distinct().count() < 2);
                    return map; }));
    }


    /**
     * Iterate through the list of user profiles and group them by personal code,
     * remove the entries where the personal code is correctly associated with no more than 1 distinct object id.
     * @param userProfiles original list of user profiles
     * @return map containing only the personal codes where the associated object ids are invalid
     */
    private Map<String, List<JudicialUserProfile>> filterAndGroupByPersonalCode(
            List<JudicialUserProfile> userProfiles) {

        return userProfiles.stream()
                .filter(userProfile -> nonNull(userProfile.getObjectId()))
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
        var fileStatus = getFileDetails(exchange.getContext(), routeProperties.getFileName());

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
                        ps.setString(4, argument.getPerId());//key -> either per_code or object_id
                        ps.setString(5, PER_CODE_OBJECT_ID_FIELD);//field in error -> percode or object id
                        ps.setString(6, PER_CODE_OBJECT_ID_ERROR_MESSAGE);//error description -> hardcoded
                        ps.setTimestamp(7, new Timestamp(new Date().getTime()));//updated timestamp
                        ps.setLong(8, argument.getRowId());//rowId
                    }
                });

        TransactionStatus status = platformTransactionManager.getTransaction(def);
        platformTransactionManager.commit(status);
    }

}
