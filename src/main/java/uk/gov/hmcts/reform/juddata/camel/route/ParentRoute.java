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
import org.apache.camel.model.OnExceptionDefinition;
import org.apache.camel.model.dataformat.BindyType;
import org.apache.camel.model.language.SimpleExpression;
import org.apache.camel.spi.Policy;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.reform.juddata.camel.aggregate.ListAggregationStrategy;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialOfficeAppointmentRowMapper;
import uk.gov.hmcts.reform.juddata.camel.processor.FileReadProcessor;
import uk.gov.hmcts.reform.juddata.camel.processor.JudicialOfficeAppointmentProcessor;
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
        List<String> childes = environment.containsProperty(childNames)
                ? (List<String>) environment.getProperty(childNames, List.class) : new ArrayList<>();

        camelContext.addRoutes(
                new SpringRouteBuilder() {
                    @Override
                    @Transactional
                    public void configure() throws Exception {
                        booleanPredicate.setValue(nonNull(childes));
                        Expression exp = new SimpleExpression(environment.getProperty(parentName + "." + BLOBPATH));

                        onException(Exception.class)
                                .handled(true)
                                .markRollbackOnly()
                                .process(new Processor() {
                                    public void process(Exchange exchange) throws Exception {
                                        Exception exception = (Exception) exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
                                        log.info("#####Parent" + exception.getMessage());
                                    }
                                }).end();

                        from(environment.getProperty(parentName + "." + TIMER))
                                .id(parentRouteName)
                                //.onException(Exception.class).rollback("Parent rolled back").end()
                                .transacted()
                                .setProperty(BLOBPATH, exp)
                                .process(fileReadProcessor).unmarshal().bindy(BindyType.Csv,
                                ctx.getBean(uncapitalize(environment.getProperty(parentName + "." + CSVBINDER))).getClass())
                                //.split().body()
                                /*.aggregate(constant(true), new ListAggregationStrategy()).completionSize(completionSize)
                                .completionTimeout(completionTimeout)*/
                                .process((Processor) ctx.getBean(uncapitalize(environment.getProperty(parentName + "." + PROCESSOR))))
                                .split().body()
                                //.streaming()
                                .bean(ctx.getBean(uncapitalize(environment.getProperty(parentName + "." + MAPPER)), MAPPING_METHOD))
                                .to("sql:" + environment.getProperty(parentName + "." + SQL));

                        from("timer://judicial_office_appointment?repeatCount=1&delay=5000")
                                //.onException(Exception.class).throwException(Exception.class, "").end()
                                .transacted("PROPAGATION_REQUIRED")
                                .id("judicial_office_appointment")
                                .setProperty(BLOBPATH, new SimpleExpression("azure-blob://rddemo/jrdtest/Appointments.csv?credentials=#credsreg&operation=updateBlockBlob"))
                                .process(fileReadProcessor).unmarshal().bindy(BindyType.Csv, JudicialOfficeAppointment.class)
                                //.split().body()
                                //.setProperty(TRUNCATE_SQL, constant(String.valueOf(environment.containsProperty(childRouteName + "." + TRUNCATE_SQL))))
                                .to("sql:" + "truncate judicial_office_appointment?dataSource=dataSource") // to do validate null check for truncate sql
                                //.aggregate(constant(true), new ListAggregationStrategy()).completionSize(completionSize)
                                //  .completionTimeout(completionTimeout)
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
