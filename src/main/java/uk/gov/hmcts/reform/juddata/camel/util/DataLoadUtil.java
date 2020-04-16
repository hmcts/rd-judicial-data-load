package uk.gov.hmcts.reform.juddata.camel.util;

import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SCHEDULER_NAME;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SCHEDULER_START_TIME;

import java.util.Date;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.springframework.stereotype.Component;

@Component
public class DataLoadUtil {

    public void setGlobalConstant(CamelContext camelContext, String schedulerName) {
        Map<String, String> globalOptions = camelContext.getGlobalOptions();
        camelContext.getGlobalOptions().put(SCHEDULER_START_TIME, String.valueOf(new Date().getTime()));
        globalOptions.put(SCHEDULER_NAME, schedulerName);
    }
}
