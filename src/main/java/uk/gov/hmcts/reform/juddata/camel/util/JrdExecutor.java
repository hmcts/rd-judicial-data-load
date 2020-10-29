package uk.gov.hmcts.reform.juddata.camel.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.service.AuditServiceImpl;
import uk.gov.hmcts.reform.data.ingestion.camel.util.RouteExecutor;

import static java.util.Objects.nonNull;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.ERROR_MESSAGE;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.FAILURE;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.IS_PARENT;

@Slf4j
@Component
@RefreshScope
public class JrdExecutor extends RouteExecutor {

    @Autowired
    AuditServiceImpl judicialAuditServiceImpl;

    @Value("${logging-component-name}")
    private String logComponentName;

    @Autowired
    ApplicationContext chunkContext;

    @Override
    public String execute(CamelContext camelContext, String schedulerName, String route) {
        try {
            return super.execute(camelContext, schedulerName, route);
        } catch (Exception ex) {
            //Camel override error stack with route failed hence grabbing exception form context
            String errorMessage = camelContext.getGlobalOptions().get(ERROR_MESSAGE);
            judicialAuditServiceImpl.auditException(camelContext, errorMessage);
            log.error("{}:: {} failed:: {}", logComponentName, schedulerName, errorMessage);
            return FAILURE;
        } finally {
            if (nonNull(camelContext.getGlobalOption(IS_PARENT))) {
                //runs Job Auditing
                judicialAuditServiceImpl.auditSchedulerStatus(camelContext);
            }
        }
    }
}
