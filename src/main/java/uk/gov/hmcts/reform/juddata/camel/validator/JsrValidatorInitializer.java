package uk.gov.hmcts.reform.juddata.camel.validator;

import static java.lang.Boolean.TRUE;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.INVALID_JSR_PARENT;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.ROUTE_DETAILS;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SCHEDULER_NAME;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SCHEDULER_START_TIME;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import uk.gov.hmcts.reform.juddata.camel.exception.RouteFailedException;
import uk.gov.hmcts.reform.juddata.camel.route.beans.RouteProperties;

@Component
@Slf4j
public class JsrValidatorInitializer<T> {

    private Validator validator;

    @Autowired
    @Qualifier("springJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Value("${invalid-exception-sql}")
    String invalidHeaderSql;

    @Autowired
    CamelContext camelContext;

    @Autowired
    @Qualifier("springJdbcTransactionManager")
    PlatformTransactionManager platformTransactionManager;

    private Set<ConstraintViolation<T>> constraintViolations;

    private List<T> invalidJsrRecords;

    @Value("${invalid-jsr-sql}")
    String invalidJsrSql;

    @Value("${jsr-threshold-limit:0}")
    int jsrThresholdLimit;

    @Value("${jdbc-batch-size:10}")
    int jdbcBatchSize;

    @PostConstruct
    public void initializeFactory() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * JSR validation.
     *
     * @param binders List
     * @return List binder list
     */
    public List<T> validate(List<T> binders) {

        log.info("::::JsrValidatorInitializer data processing validate starts::::");
        this.constraintViolations = new LinkedHashSet<>();
        List<T> binderFilter = new ArrayList<>();

        this.invalidJsrRecords = new ArrayList<>();

        binders.forEach(binder -> {
            Set<ConstraintViolation<T>> constraintViolations = validator.validate(binder);
            if (constraintViolations.size() == 0) {
                binderFilter.add(binder);
            } else {
                invalidJsrRecords.add(binder);
            }
            this.constraintViolations.addAll(constraintViolations);
        });

        log.info("::::JsrValidatorInitializer data processing validate complete::::");
        return binderFilter;
    }

    /**
     * Auditing JSR Exception.
     *
     * @param exchange Exchange
     */
    public void auditJsrExceptions(Exchange exchange) {

        log.info("::::JsrValidatorInitializer data processing audit start::::");

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("Jsr exception logs");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        Map<String, String> globalOptions = camelContext.getGlobalOptions();

        RouteProperties routeProperties = (RouteProperties) exchange.getIn().getHeader(ROUTE_DETAILS);
        String schedulerTime = globalOptions.get(SCHEDULER_START_TIME);

        List<ConstraintViolation<T>> violationList = constraintViolations.stream()
                .limit(jsrThresholdLimit == 0 ? constraintViolations.size() : jsrThresholdLimit)
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(
                invalidJsrSql,
                violationList,
                jdbcBatchSize,
                new ParameterizedPreparedStatementSetter<ConstraintViolation<T>>() {
                    public void setValues(PreparedStatement ps, ConstraintViolation<T> argument) throws SQLException {
                        ps.setString(1, routeProperties.getTableName());
                        ps.setTimestamp(2, new Timestamp(Long.valueOf(schedulerTime)));
                        ps.setString(3, globalOptions.get(SCHEDULER_NAME));
                        ps.setString(4, getKeyFiled(argument.getRootBean()));
                        ps.setString(5, argument.getPropertyPath().toString());
                        ps.setString(6, argument.getMessage());
                        ps.setTimestamp(7, new Timestamp(new Date().getTime()));
                    }
                });

        TransactionStatus status = platformTransactionManager.getTransaction(def);
        platformTransactionManager.commit(status);
        log.info("::::JsrValidatorInitializer data processing audit complete::::");
    }


    /**
     * Auditing JSR Exception for skipped parent in child.
     *
     * @param keys List
     * @param exchange Exchange
     *
     */
    public void auditJsrExceptions(List<String> keys, String fieldInError, Exchange exchange) {

        log.info("::::JsrValidatorInitializer data processing audit start for skipping parent table violation::::");

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("Jsr exception logs");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        RouteProperties routeProperties = (RouteProperties) exchange.getIn().getHeader(ROUTE_DETAILS);
        Map<String, String> globalOptions = camelContext.getGlobalOptions();
        String schedulerTime = globalOptions.get(SCHEDULER_START_TIME);

        jdbcTemplate.batchUpdate(
                invalidJsrSql,
                keys,
                jdbcBatchSize,
                new ParameterizedPreparedStatementSetter<String>() {
                    @Override
                    public void setValues(PreparedStatement ps, String argument) throws SQLException {
                        ps.setString(1, routeProperties.getTableName());
                        ps.setTimestamp(2, new Timestamp(Long.valueOf(schedulerTime)));
                        ps.setString(3, globalOptions.get(SCHEDULER_NAME));
                        ps.setString(4, argument);
                        ps.setString(5, fieldInError);
                        ps.setString(6, INVALID_JSR_PARENT);
                        ps.setTimestamp(7, new Timestamp(new Date().getTime()));
                    }
                });

        TransactionStatus status = platformTransactionManager.getTransaction(def);
        platformTransactionManager.commit(status);
        log.info("::::JsrValidatorInitializer data processing audit complete for skipping parent table violation::::");
    }

    /**
     * get key fields.
     *
     * @param bean Object
     * @return String
     */
    private String getKeyFiled(Object bean) {
        Class objectClass = bean.getClass();
        try {
            for (Field field : objectClass.getDeclaredFields()) {

                DataField dataField = AnnotationUtils.findAnnotation(field,
                        DataField.class);
                if (dataField.pos() == 1) {
                    field.setAccessible(TRUE);
                    return (String) field.get(bean);
                }
            }
        } catch (IllegalAccessException ex) {
            throw new RouteFailedException("JSR auditing failed getting auditing key values");
        }
        return "";
    }


    public Set<ConstraintViolation<T>> getConstraintViolations() {
        return constraintViolations;
    }

    public List<T> getInvalidJsrRecords() {
        return invalidJsrRecords;
    }
}

