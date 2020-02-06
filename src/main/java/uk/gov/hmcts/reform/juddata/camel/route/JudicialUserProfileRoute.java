package uk.gov.hmcts.reform.juddata.camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.aggregate.ListAggregationStrategy;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialOfficeAppointmentRowMapper;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialOfficeAuthorisationRowMapper;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialUserProfileRowMapper;
import uk.gov.hmcts.reform.juddata.camel.processor.JudicialUserProfileFileReadProcessor;
import uk.gov.hmcts.reform.juddata.camel.processor.JudicialUserProfileProcessor;

/**
 * This class is Judicial User Profile Router Triggers Orchestrated data loading.
 *
 */
@Component
public class JudicialUserProfileRoute extends RouteBuilder {

    @Autowired
    JudicialUserProfileRowMapper judUserRowMapper;

    @Autowired
    JudicialOfficeAppointmentRowMapper judicialOfficeAppointmentRowMapper;

    @Autowired
    JudicialOfficeAuthorisationRowMapper judicialOfficeAuthorisationRowMapper;

    @Autowired
    PropertySourcesPlaceholderConfigurer props;

    @Value("${router.scheduler-expression:''}")
    private String pathToFileFolder;

    @Value("${router.id:''}")
    private String routerId;

    @Value("${router.user-profile-insert:''}")
    private String userProfileSql;

    @Value("${router.user-profile-aggregation-strategy-completion-size: 5000}")
    private int completionSize;

    @Value("${router.user-profile-aggregation-strategy-timeout: 2000}")
    private int completionTimeout;

    @Override
    public void configure() {
        //User Profile Route Insertion based on aggregate timeout(router:user-profile-aggregation-strategy-timeout)
        //or aggregate batch size (user-profile-aggregation-strategy-completion-size)
        from(pathToFileFolder)
                .id(routerId)
                .process(new JudicialUserProfileFileReadProcessor()).unmarshal().bindy(BindyType.Csv, JudicialUserProfile.class)
                .split().body()
                .aggregate(constant(true), new ListAggregationStrategy()).completionSize(completionSize)
                .completionTimeout(completionTimeout)
                .process(new JudicialUserProfileProcessor())
                .split().body()
                .streaming()
                .bean(judUserRowMapper, "getMap")
                .to("sql:" + userProfileSql)
                .end();
    }
}


