package uk.gov.hmcts.reform.juddata.camel.util;

import java.util.Map;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DataLoadUtil {

    @Value("${scheduler-name}")
    public String  schedulerName;

    public void setGlobalConstant(CamelContext camelContext) {

        Map<String, String> globalOptions = camelContext.getGlobalOptions();
        globalOptions.put(MappingConstants.ORCHESTRATED_ROUTE, MappingConstants.JUDICIAL_USER_PROFILE_ORCHESTRATION);
        globalOptions.put(MappingConstants.SCHEDULER_START_TIME, MappingConstants.getCurrentTimeStamp().toString());
        globalOptions.put(MappingConstants.SCHEDULER_NAME, schedulerName);
    }
}
