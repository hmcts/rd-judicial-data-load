package uk.gov.hmcts.reform.juddata.camel.route;

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

import static org.apache.commons.lang.WordUtils.uncapitalize;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.BLOBPATH;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.CSVBINDER;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.MAPPER;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.PARENT_ROUTE_NAME;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.PROCESSOR;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SQL;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.TIMER;

/**
 * This class is Judicial User Profile Router Triggers Orchestrated data loading.
 *
 */
@Component
public class ParentRoute  {

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

    public void startRoute() throws Exception {

        CamelContext camelContext = ctx.getBean(CamelContext.class);
        camelContext.addRoutes(
            new SpringRouteBuilder() {
                @Override
                public void configure() {
                    String parentRouteName = camelContext.getGlobalOptions().get(PARENT_ROUTE_NAME);
                    String parentName = "parent-route" + "." + parentRouteName;
                    Expression exp = new SimpleExpression(environment.getProperty(parentName + "." +  BLOBPATH));
                    //User Profile Route Insertion based on aggregate timeout(router:user-profile-aggregation-strategy-timeout)
                    //or aggregate batch size (user-profile-aggregation-strategy-completion-size)
                    from(environment.getProperty(parentName + "." + TIMER))
                            .id(parentRouteName)
                            .setProperty("blobFilePath", exp)
                            .process(fileReadProcessor).unmarshal().bindy(BindyType.Csv,
                            ctx.getBean(uncapitalize(environment.getProperty(parentName + "." + CSVBINDER))).getClass())
                            .split().body()
                            .aggregate(constant(true), new ListAggregationStrategy()).completionSize(completionSize)
                            .completionTimeout(completionTimeout)
                            .process((Processor) ctx.getBean(uncapitalize(environment.getProperty(parentName + "." + PROCESSOR))))
                            .split().body()
                            .streaming()
                            .bean(ctx.getBean(uncapitalize(environment.getProperty(parentName + "." + MAPPER)), "getMap"))
                            //.loop(sqls.size())
                            //    .to("sql:" + sqls.get(Integer.valueOf((Exchange.LOOP_INDEX))))
                            .to("sql:" + environment.getProperty(parentName + "." + SQL))
                            .end();
                }
            }
        );
    }
}


