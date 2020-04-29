package uk.gov.hmcts.reform.juddata.camel.util;

import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createCurrentLocalDate;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SCHEDULER_NAME;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SCHEDULER_START_TIME;

import java.sql.Timestamp;

import org.apache.camel.CamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringRunner.class)
@Configuration()
@ContextConfiguration(classes = DataLoadUtil.class)
public class DataLoadUtilTest extends CamelTestSupport {

    @Autowired
    DataLoadUtil dataLoadUtil;

    @Test
    public void setGlobalConstant() throws Exception {
        CamelContext camelContext = createCamelContext();
        camelContext.start();
        dataLoadUtil.setGlobalConstant(camelContext, "judicial_leaf_scheduler");
        assertNotNull("judicial_leaf_scheduler",camelContext.getGlobalOption(SCHEDULER_NAME));
        assertNotNull(camelContext.getGlobalOption(SCHEDULER_START_TIME));
    }

    @Test
    public void test_getDateTimeStamp() {
        Timestamp ts = DataLoadUtil.getDateTimeStamp(createCurrentLocalDate());
        assertNotNull(ts);
    }

    @Test
    public void test_getCurrentTimeStamp() {
        Timestamp ts = DataLoadUtil.getCurrentTimeStamp();
        assertNotNull(ts);
    }
}