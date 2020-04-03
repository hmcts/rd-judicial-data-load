package uk.gov.hmcts.reform.juddata.camel.processor;

import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.HEADER_EXCEPTION;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.ROUTE_DETAILS;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SCHEDULER_START_TIME;

import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import uk.gov.hmcts.reform.juddata.camel.exception.RouteFailedException;
import uk.gov.hmcts.reform.juddata.camel.route.beans.RouteProperties;

@Component
public class HeaderValidationProcessor implements Processor {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    CamelContext camelContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${invalid-header-sql}")
    String invalidHeaderSql;


    @Autowired
    @Qualifier("txManager1")
    PlatformTransactionManager platformTransactionManager;

    @Override
    public void process(Exchange exchange) throws Exception {

        RouteProperties routeProperties = (RouteProperties) exchange.getIn().getHeader(ROUTE_DETAILS);
        InputStream csv  = exchange.getIn().getBody(InputStream.class);
        CSVReader reader = new CSVReader(new InputStreamReader(csv));
        String[] header = reader.readNext();
        Field[] allFields = applicationContext.getBean(routeProperties.getBinder())
                .getClass().getDeclaredFields();
        List<Field> csvFields = new ArrayList<>();

        for (Field field : allFields) {
            if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                csvFields.add(field);
            }
        }

        //Auditing in database if headers are missing
        if(header.length < csvFields.size()) {
            exchange.getIn().setHeader(HEADER_EXCEPTION, HEADER_EXCEPTION);
            //separate transaction manager required for auditing as it is independent form route
            //Transaction
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setName("header exception logs");
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            String schedulerTime = camelContext.getGlobalOptions().get(SCHEDULER_START_TIME);
            Object[] params = new Object[]{routeProperties.getFileName(), Timestamp.valueOf(schedulerTime),
                    "Mismatch headers in csv for ::" + routeProperties.getBinder(), new Timestamp(new Date().getTime())};
            jdbcTemplate.update(invalidHeaderSql, params);
            TransactionStatus status = platformTransactionManager.getTransaction(def);
            platformTransactionManager.commit(status);
            throw new RouteFailedException("Mismatch headers in csv for ::" + routeProperties.getBinder());
        }
    }
}
