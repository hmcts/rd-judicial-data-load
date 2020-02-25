package uk.gov.hmcts.reform.juddata.camel.route;

import static org.apache.commons.lang.WordUtils.uncapitalize;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.BLOBPATH;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.CHILD_ROUTE_NAME;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.CSVBINDER;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.DIRECT_ROUTE;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.ID;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.MAPPER;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.MAPPING_METHOD;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.PARENT_ROUTE_NAME;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.PROCESSOR;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.ROUTE;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SQL;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.TIMER;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.TRUNCATE_SQL;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;
import org.apache.camel.CamelContext;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.model.dataformat.BindyType;
import org.apache.camel.model.language.SimpleExpression;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.processor.FileReadProcessor;
import uk.gov.hmcts.reform.juddata.camel.vo.RouteProperties;
import uk.gov.hmcts.reform.juddata.predicate.BooleanPredicate;

/**
 * This class is Judicial User Profile Router Triggers Orchestrated data loading.
 */
@Component
public class ParentOrchestrationRoute {

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

    @Autowired
    SpringTransactionPolicy springTransactionPolicy;

    @SuppressWarnings("unchecked")
    @Transactional
    public void startRoute() throws Exception {

        CamelContext camelContext = ctx.getBean(CamelContext.class);
        String parentRouteName = camelContext.getGlobalOptions().get(PARENT_ROUTE_NAME);
        String parentName = ROUTE + "." + parentRouteName;
        String childNames = ROUTE + "." + parentRouteName + "." + CHILD_ROUTE_NAME;

        List<String> childes = environment.containsProperty(childNames)
                ? (List<String>) environment.getProperty(childNames, List.class) : new ArrayList<>();
        childes.add(0, parentRouteName);

        List<RouteProperties> routePropertiesList = getRouteProperties(childes);

        camelContext.addRoutes(
                new SpringRouteBuilder() {
                    @Override
                    public void configure() throws Exception {

                        String[] directChild = new String[childes.size()];
                        int index = 0;
                        for (String child : childes) {
                            directChild[index] = (DIRECT_ROUTE).concat(child);
                            index++;
                        }

                        //User Profile Route Insertion based on  timeout(router:user-profile-aggregation-strategy-timeout)
                        //or aggregate batch size (user-profile-aggregation-strategy-completion-size)
                        from(environment.getProperty(parentName + "." + TIMER))
                                .transacted()
                                .policy(springTransactionPolicy)
                                //.transacted()
                                .multicast()
                                .stopOnException().to(directChild).end();


                        for (RouteProperties route : routePropertiesList) {

                            Expression exp = new SimpleExpression(route.getBlobPath());

                            from(DIRECT_ROUTE + route.getRouteName()).id(DIRECT_ROUTE + route.getRouteName())
                                    .transacted()
                                    .policy(springTransactionPolicy)
                                    .setProperty(BLOBPATH, exp)
                                    .process(fileReadProcessor).unmarshal().bindy(BindyType.Csv,
                                    ctx.getBean(route.getBinder()).getClass())
                                    //.split().body()
                                    .to(route.getTruncateSql())
                                    //.aggregate(constant(true), new ListAggregationStrategy()) //commented code requires for aggregation
                                    //.aggregationRepository(aggregationRepository)
                                    //.completionSize(completionSize)
                                    //.completionTimeout(completionTimeout)
                                    .process((Processor) ctx.getBean(route.getProcessor()))
                                    .split().body()
                                    .streaming()
                                    .bean(ctx.getBean(route.getMapper()), MAPPING_METHOD)
                                    .to(route.getSql()).end();
                        }
                    }
                });
    }

    /**
     * Sets Route Properties.
     *
     * @param routes routes
     * @return List RouteProperties.
     */
    private List<RouteProperties> getRouteProperties(List<String> routes) {
        List<RouteProperties> routePropertiesList = new LinkedList<>();
        int index = 0;
        for (String child : routes) {
            RouteProperties properties = new RouteProperties();
            //only applicable for parent
            properties.setChildNames(environment.getProperty(
                    ROUTE + "." + child + "." + CHILD_ROUTE_NAME));
            properties.setRouteName(environment.getProperty(
                    ROUTE + "." + child + "." + ID));
            properties.setSql(environment.getProperty(
                    ROUTE + "." + child + "." + SQL));
            properties.setTruncateSql(environment.getProperty(
                    ROUTE + "." + child + "." + TRUNCATE_SQL)
                    == null ? "log:test" : environment.getProperty(
                    ROUTE + "." + child + "." + TRUNCATE_SQL));
            properties.setBlobPath(environment.getProperty(
                    ROUTE + "." + child + "." + BLOBPATH));
            properties.setMapper(uncapitalize(environment.getProperty(
                    ROUTE + "." + child + "." + MAPPER)));
            properties.setBinder(uncapitalize(environment.getProperty(ROUTE + "."
                    + child + "." + CSVBINDER)));
            properties.setProcessor(uncapitalize(environment.getProperty(ROUTE + "."
                    + child + "." + PROCESSOR)));
            routePropertiesList.add(index, properties);
            index++;
        }
        return routePropertiesList;
    }
}



