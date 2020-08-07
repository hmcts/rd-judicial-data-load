package uk.gov.hmcts.reform.juddata.camel.util;

import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.ERROR_MESSAGE;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.FAILURE;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.util.RouteExecutor;
import uk.gov.hmcts.reform.juddata.camel.service.JudicialAuditServiceImpl;

@Slf4j
@Component
@RefreshScope
public class JrdExecutor extends RouteExecutor {

    @Autowired
    JudicialAuditServiceImpl judicialAuditServiceImpl;

    @Override
    public String execute(CamelContext camelContext, String schedulerName, String route) {
        try {
            return super.execute(camelContext, schedulerName, route);
        } catch (Exception ex) {
            //Camel override error stack with route failed hence grabbing exception form context
            String errorMessage = camelContext.getGlobalOptions().get(ERROR_MESSAGE);
            judicialAuditServiceImpl.auditException(camelContext, errorMessage);
            log.error(":: " + schedulerName + " failed:: {}", errorMessage);
            return FAILURE;
        } finally {
            //runs Job Auditing
            judicialAuditServiceImpl.auditSchedulerStatus(camelContext);
        }
    }
}
