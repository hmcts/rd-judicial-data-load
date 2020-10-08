package uk.gov.hmcts.reform.juddata.camel.util;

import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.ERROR_MESSAGE;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.FAILURE;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.service.AuditServiceImpl;
import uk.gov.hmcts.reform.data.ingestion.camel.util.RouteExecutor;

@Slf4j
@Component
@RefreshScope
public class JrdExecutor extends RouteExecutor {

    @Autowired
    AuditServiceImpl judicialAuditServiceImpl;

    @Value("${logging-component-name}")
    private String logComponentName;

    @Override
    public String execute(CamelContext camelContext, String schedulerName, String route) {
        try {
            return super.execute(camelContext, schedulerName, route);
        } catch (Exception ex) {
            //Camel override error stack with route failed hence grabbing exception form context
            String errorMessage = camelContext.getGlobalOptions().get(ERROR_MESSAGE);
            judicialAuditServiceImpl.auditException(camelContext, errorMessage, false);
            log.error("{}:: {} failed:: {}", logComponentName, schedulerName, errorMessage);
            return FAILURE;
        } finally {
            //runs Job Auditing
            judicialAuditServiceImpl.auditSchedulerStatus(camelContext);
        }
    }
}
