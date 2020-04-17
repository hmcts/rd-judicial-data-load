package uk.gov.hmcts.reform.juddata.cameltest;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import uk.gov.hmcts.reform.juddata.camel.processor.ExceptionProcessor;
import uk.gov.hmcts.reform.juddata.camel.route.ParentOrchestrationRoute;
import uk.gov.hmcts.reform.juddata.camel.service.EmailService;
import uk.gov.hmcts.reform.juddata.camel.util.DataLoadUtil;

public abstract class ParentRouteAbstractTest {

    public static final String DB_SCHEDULER_STATUS = "scheduler_status";

    @Autowired
    protected CamelContext camelContext;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    ParentOrchestrationRoute parentRoute;

    @Value("${start-route}")
    protected String startRoute;

    @Autowired
    ProducerTemplate producerTemplate;

    @Value("${parent-select-jrd-sql}")
    protected String sql;

    @Value("${child-select-child1-sql}")
    protected String sqlChild1;

    @Value("${archival-cred}")
    String archivalCred;

    @Value("${select-dataload-schedular-audit}")
    String selectDataLoadSchedulerAudit;

    @Value("${scheduler-insert-sql}")
    protected String schedulerInsertJrdSql;

    @Value("${select-dataload-scheduler-audit-failure}")
    protected String schedulerInsertJrdSqlFailure;

    @Value("${select-dataload-scheduler-audit-partial-success}")
    protected String schedulerInsertJrdSqlPartialSuccess;

    @Value("${select-dataload-scheduler-audit-success}")
    protected String schedulerInsertJrdSqlSuccess;

    @Value("${audit-enable}")
    protected Boolean auditEnable;

    @Autowired
    protected DataLoadUtil dataLoadUtil;

    @Autowired
    protected ExceptionProcessor exceptionProcessor;

    @Autowired
    protected EmailService emailService;
}
