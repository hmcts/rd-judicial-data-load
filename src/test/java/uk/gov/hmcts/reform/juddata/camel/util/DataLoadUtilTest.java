package uk.gov.hmcts.reform.juddata.camel.util;

import org.apache.camel.CamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringRunner.class)
@Configuration()
@ContextConfiguration(classes = DataLoadUtil.class)
public class DataLoadUtilTest extends CamelTestSupport {

    @Value("${scheduler-name}")
    private String startRoute;

    @Autowired
    DataLoadUtil dataLoadUtil;

    @Before
    public void setUp() throws Exception {
        dataLoadUtil.schedulerName = "judicial_leaf_scheduler";
    }

    @Test
    public void setGlobalConstant() throws Exception {
        CamelContext camelContext = createCamelContext();
        camelContext.start();
        dataLoadUtil.setGlobalConstant(camelContext);
        assertEquals("judicial_leaf_scheduler", dataLoadUtil.schedulerName);
        assertEquals("judicial-user-profile-orchestration",camelContext.getGlobalOption(MappingConstants.ORCHESTRATED_ROUTE));



    }
}