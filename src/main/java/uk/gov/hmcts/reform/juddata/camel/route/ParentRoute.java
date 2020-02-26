package uk.gov.hmcts.reform.juddata.camel.route;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang.WordUtils.uncapitalize;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.BLOBPATH;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.CHILD_ROUTE_NAME;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.CSVBINDER;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.MAPPER;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.MAPPING_METHOD;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.PARENT_ROUTE_NAME;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.PROCESSOR;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SQL;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.TIMER;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.TRUNCATE_SQL;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.Route;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.model.OnExceptionDefinition;
import org.apache.camel.model.dataformat.BindyType;
import org.apache.camel.model.language.SimpleExpression;
import org.apache.camel.spi.Policy;
import org.apache.camel.spi.Registry;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.reform.juddata.camel.aggregate.ListAggregationStrategy;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialOfficeAuthorisation;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialOfficeAppointmentRowMapper;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialOfficeAuthorisationRowMapper;
import uk.gov.hmcts.reform.juddata.camel.processor.FileReadProcessor;
import uk.gov.hmcts.reform.juddata.camel.processor.JudicialOfficeAppointmentProcessor;
import uk.gov.hmcts.reform.juddata.camel.processor.JudicialOfficeAuthorisationProcessor;
import uk.gov.hmcts.reform.juddata.predicate.BooleanPredicate;

/**
 * This class is Judicial User Profile Router Triggers Orchestrated data loading.
 */
@Component
@Slf4j
public class ParentRoute {

    @Value("${aggregation-strategy-completion-size: 5000}")
    private int completionSize;

    @Value("${aggregation-strategy-timeout: 2000}")
    private int completionTimeout;

    @Autowired
    FileReadProcessor fileReadProcessor;

    @Autowired
    ApplicationContext ctx;

    @Autowired
    Environment environment;

    @Autowired
    ChildRoute childRoute;

    @Autowired
    BooleanPredicate booleanPredicate;


    @SuppressWarnings("unchecked")
    @Transactional
    public void startRoute() throws Exception {

        CamelContext camelContext = ctx.getBean(CamelContext.class);
        String parentRouteName = camelContext.getGlobalOptions().get(PARENT_ROUTE_NAME);
        String parentName = PARENT_ROUTE_NAME + "." + parentRouteName;
        String childNames = PARENT_ROUTE_NAME + "." + parentRouteName + "." + CHILD_ROUTE_NAME;
        List<String> childes = environment.containsProperty(childNames) ? (List<String>) environment.getProperty(childNames, List.class) : new ArrayList<>();

        String truncateRoute = "direct:truncate_child_tables";
        String parentRoute= "direct:judicial_user_profile";
        String childRoute_appointment= "direct:judicial_office_appointment";
        String childRoute_Auth = "direct:judicial_office_authorisation";

        camelContext.addRoutes(
                new SpringRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        from("timer://orchastrator?repeatCount=1&delay=5000")
                                .transacted()
                                .multicast()
                                .stopOnException()
                                .to(parentRoute, childRoute_Auth, childRoute_appointment)
                                .end();

                        from(parentRoute)
                                .id(parentRoute)
                                .transacted()
                                .setProperty(BLOBPATH, new SimpleExpression(environment.getProperty(parentName + "." + BLOBPATH)))
                                .process(fileReadProcessor).unmarshal().bindy(BindyType.Csv,
                                ctx.getBean(uncapitalize(environment.getProperty(parentName + "." + CSVBINDER))).getClass())
                                //.split().body()
                                //.aggregate(constant(true), new ListAggregationStrategy()).completionSize(completionSize)
                                //.completionTimeout(completionTimeout)
                                .process((Processor) ctx.getBean(uncapitalize(environment.getProperty(parentName + "." + PROCESSOR))))
                                .split().body()
                                .streaming()
                                .bean(ctx.getBean(uncapitalize(environment.getProperty(parentName + "." + MAPPER)), MAPPING_METHOD))
                                .to("sql:" + environment.getProperty(parentName + "." + SQL))
                                .end();

                        /*from(truncateRoute)
                                .id(truncateRoute)
                                .transacted()
                                .to("sql:" + "truncate judicial_office_authorisation?dataSource=dataSource")
                                .to("sql:" + "truncate judicial_office_appointment?dataSource=dataSource")
                                .end();*/

                        from(childRoute_Auth)
                                .transacted()
                                .id("judicial_office_authorisation")
                                .setProperty(BLOBPATH, new SimpleExpression("azure-blob://rddemo/jrdtest/judicial_office_authorisation.csv?credentials=#credsreg&operation=updateBlockBlob"))
                                .process(fileReadProcessor).unmarshal().bindy(BindyType.Csv, JudicialOfficeAuthorisation.class)
                                //.split().body()
                                .to("sql:" + "truncate judicial_office_authorisation?dataSource=dataSource") // to do validate null check for truncate sql
                                //.aggregate(constant(true), new ListAggregationStrategy()).completionSize(completionSize)
                                //.completionTimeout(completionTimeout)
                                .process((Processor) ctx.getBean(JudicialOfficeAuthorisationProcessor.class))
                                .split().body()
                                .streaming()
                                .bean(JudicialOfficeAuthorisationRowMapper.class, MAPPING_METHOD)
                                .to("sql:" + "insert into judicial_office_authorisation (judicial_office_auth_id,elinks_id,authorisation_id,jurisdiction_id,authorisation_date,extracted_date,created_date,last_loaded_date)" +
                                        " values(:#judicial_office_auth_id,:#elinks_id,:#authorisation_id, :#jurisdiction_id,:#authorisation_date,:#extracted_date,:#created_date,:#last_loaded_date)?dataSource=dataSource")
                                .end();

                        from(childRoute_appointment)
                                .transacted()
                                .id("judicial_office_appointment")
                                .setProperty(BLOBPATH, new SimpleExpression("azure-blob://rddemo/jrdtest/Appointments.csv?credentials=#credsreg&operation=updateBlockBlob"))
                                .process(fileReadProcessor).unmarshal().bindy(BindyType.Csv, JudicialOfficeAppointment.class)
                                //.split().body()
                                .to("sql:" + "truncate judicial_office_appointment?dataSource=dataSource") // to do validate null check for truncate sql
                                //.aggregate(constant(true), new ListAggregationStrategy()).completionSize(completionSize)
                                //.completionTimeout(completionTimeout)
                                .process((Processor) ctx.getBean(JudicialOfficeAppointmentProcessor.class))
                                .split().body()
                                .streaming()
                                .bean(JudicialOfficeAppointmentRowMapper.class, MAPPING_METHOD)
                                .to("sql:" + "insert into judicial_office_appointment (judicial_office_appointment_id,elinks_id,role_id,contract_type_id,base_location_id,region_id,is_prinicple_appointment,start_date,end_date,active_flag,extracted_date) " +
                                        "values(:#judicial_office_appointment_id, :#elinks_id, :#role_id, :#contract_type_id, :#base_location_id, :#region_id, :#is_prinicple_appointment, :#start_date, :#end_date, :#active_flag, :#extracted_date)?dataSource=dataSource")
                                .end();
                    }

                }
        );

    }

}
