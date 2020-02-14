package uk.gov.hmcts.reform.juddata.camel.route;

import static org.apache.commons.lang.WordUtils.uncapitalize;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.BLOBPATH;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.CHILD_ROUTE_NAME;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.CSVBINDER;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.MAPPER;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.MAPPING_METHOD;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.PROCESSOR;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SQL;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.TIMER;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.TRUNCATE_SQL;

import org.apache.camel.CamelContext;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.model.dataformat.BindyType;
import org.apache.camel.model.language.SimpleExpression;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.aggregate.ListAggregationStrategy;
import uk.gov.hmcts.reform.juddata.camel.processor.FileReadProcessor;
import uk.gov.hmcts.reform.juddata.predicate.BooleanPredicate;

@Component
public class ChildRoute {

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
    BooleanPredicate booleanPredicate;


    public void startRoute() throws Exception {

        CamelContext camelContext = ctx.getBean(CamelContext.class);
        String childRouteName = CHILD_ROUTE_NAME + "." + camelContext.getGlobalOptions().get(CHILD_ROUTE_NAME);
        Expression exp = new SimpleExpression(environment.getProperty(childRouteName + "." + BLOBPATH));
        String timer = environment.getProperty(childRouteName + "." + TIMER);
        String sql = environment.getProperty(childRouteName + "." + SQL);
        String truncateSql = environment.getProperty(childRouteName + "." + TRUNCATE_SQL);
        booleanPredicate.setValue(truncateSql != null && !truncateSql.isEmpty());

        camelContext.addRoutes(
                new SpringRouteBuilder() {
                    @Override
                    public void configure() {
                        from(timer)
                                .id(childRouteName)
                                .setProperty(BLOBPATH, exp)
                                .process(fileReadProcessor).unmarshal().bindy(BindyType.Csv,
                                 ctx.getBean(uncapitalize(environment.getProperty(childRouteName + "." + CSVBINDER))).getClass())
                                .split().body()
                                //.setProperty(TRUNCATE_SQL, constant(String.valueOf(environment.containsProperty(childRouteName + "." + TRUNCATE_SQL))))
                                .to("sql:" + truncateSql) // to do validate null check for truncate sql
                                .aggregate(constant(true), new ListAggregationStrategy()).completionSize(completionSize)
                                .completionTimeout(completionTimeout)
                                .process((Processor) ctx.getBean(uncapitalize(environment.getProperty(childRouteName + "." + PROCESSOR))))
                                .split().body()
                                .streaming()
                                .bean(ctx.getBean(uncapitalize(environment.getProperty(childRouteName + "." + MAPPER)), MAPPING_METHOD))
                                .to("sql:" + sql)
                                .end();
                    }
                }
        );
    }
}
