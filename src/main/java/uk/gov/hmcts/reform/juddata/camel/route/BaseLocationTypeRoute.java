package uk.gov.hmcts.reform.juddata.camel.route;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.mapper.BaseLocationRowTypeMapper;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialAuthorisationTypeRowMapper;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialContractTypeRowMapper;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialRegionTypeRowMapper;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialRoleTypeRowMapper;

@Component
@Slf4j
public class BaseLocationTypeRoute extends RouteBuilder {

    @Autowired
    BaseLocationRowTypeMapper baseLocationRowTypeMapper;

    @Autowired
    JudicialAuthorisationTypeRowMapper judicialAuthorizationTypeRowMapper;

    @Autowired
    JudicialContractTypeRowMapper judicialContractTypeRowMapper;

    @Autowired
    JudicialRegionTypeRowMapper judicialRegionTypeRowMapper;

    @Autowired
    JudicialRoleTypeRowMapper judicialRoleTypeRowMapper;

    @Override
    public void configure() throws Exception {
        // Java
        //        CronScheduledRoutePolicy policy = new CronScheduledRoutePolicy();
        //        policy.setRouteStartTime("0 0/2 * * * ?");

        //from("scheduler=quartz2&scheduler.cron=00+*/5+*+1/1+*+?+*")
        //        .routeId("testRoute")
        //        .log("File name : ${header.CamelFileName}")
        //        .to("azure-blob://rddemo/jrdtest/BaseLocations.csv?credentials=#credsreg&operation=updateBlockBlob");

        //  from("quartz2&scheduler.cron=00+*/5+*+1/1+*+?+*")
        //                .routeId("testRoute")

        //        .to("azure-blob://rddemo/jrdtest/BaseLocations.csv?credentials=#credsreg&operation=updateBlockBlob");

        //        from("azure-blob://rddemo/jrdtest/BaseLocations.csv?credentials=#credsreg&operation=updateBlockBlob")
        //                .id("location-route")
        //               // .routePolicy(policy)
        //                .to("log:test1?showAll=true")
        //                .to("file://blobdirectory1?noop=true&fileExist=Override").stop();
        //                //.to("file:blobdirectory1/noop=true");

        /*from("file://blobdirectory1?noop=true&fileExist=Override")
                .unmarshal()
                .csv()
                .process(validateFile())
               // .to("log:my.package?multiline=true")
                .to("file://blobdirectory1?scheduler=quartz2").end();*/

        //        from("file://blobdirectory1?scheduler=quartz2&scheduler.cron=00+*/2+*+1/1+*+?+*")
        //                .unmarshal()
        //                .bindy(BindyType.Csv, BaseLocationType.class)
        //                .process(new BaseLocationRecordProcessor())
        //                .split().body()
        //                .bean(baseLocationRowTypeMapper , "getMap")
        //                .to("sql:insert into base_location_type (base_location_id,court_name,court_type,circuit,area_of_expertise) values(:#base_location_id,:#court_name, :#court_type,:#circuit, :#area_of_expertise)?dataSource=dataSource")
        //                .to("log:test?showAll=true")
        //                .end();

       /* from("azure-blob://rddemo/jrdtest/authorisation_type.csv?credentials=#credsreg&operation=updateBlockBlob")
                .id("authorization-route")
                // .startupOrder(3)
                .to("file://blobdirectory6?noop=true&fileExist=Override").end();

        from("file://blobdirectory6?noop=true&fileExist=Override")
                //  .startupOrder(4)
                .unmarshal() .bindy(BindyType.Csv, JudicialAuthorisationType.class)
                .process(new JudicialAuthorisationTypeProcessor())
                .split().body()
                .bean(judicialAuthorizationTypeRowMapper , "getMap")
                .to("sql:insert into authorisation_type (authorisation_id,authorisation_desc_en,authorisation_desc_cy,jurisdiction_id,jurisdiction_desc_en,jurisdiction_desc_cy) " +
                        "values(:#authorisation_id,:#authorisation_desc_en,:#authorisation_desc_cy, :#jurisdiction_id,:#jurisdiction_desc_en,:#jurisdiction_desc_cy)?dataSource=dataSource")
                .to("log:test?showAll=true")
                .end();

        from("azure-blob://rddemo/jrdtest/Contracts.csv?credentials=#credsreg&operation=updateBlockBlob")
                .id("contract-route")
                //.startupOrder(5)
                .to("file://blobdirectory4?noop=true&fileExist=Override").end();

        from("file://blobdirectory4?noop=true&fileExist=Override")
                // .startupOrder(6)
                .unmarshal() .bindy(BindyType.Csv, JudicialContractType.class)
                .process(new JudicialContractTypeProcessor())
                .split().body()
                .bean(judicialContractTypeRowMapper , "getMap")
                .to("sql:insert into contract_type (contract_type_id,contract_type_desc_en,contract_type_desc_cy) values(:#contract_type_id,:#contract_type_desc_en,:#contract_type_desc_en)?dataSource=dataSource")
                .to("log:test?showAll=true")
                .end();

        from("azure-blob://rddemo/jrdtest/region_type.csv?credentials=#credsreg&operation=updateBlockBlob")
                .id("region-route")
                //.startupOrder(7)
                .to("file://blobdirectory5?noop=true&fileExist=Override").end();

        from("file://blobdirectory5?noop=true&fileExist=Override")
                // .startupOrder(8)
                .unmarshal() .bindy(BindyType.Csv, JudicialRegionType.class)
                .process(new JudicialRegionTypeProcessor())
                .split().body()
                .bean(judicialRegionTypeRowMapper , "getMap")
                .to("sql:insert into region_type (region_id,region_desc_en,region_desc_cy) values(:#region_id,:#region_desc_en,:#region_desc_cy)?dataSource=dataSource")
                .to("log:test?showAll=true")
                .end();

        from("azure-blob://rddemo/jrdtest/Roles.csv?credentials=#credsreg&operation=updateBlockBlob")
                .id("role-route")
                //.startupOrder(9)
                .to("file://blobdirectory2?noop=true&fileExist=Override").end();

        from("file://blobdirectory2?noop=true&fileExist=Override")
                //.startupOrder(10)
                .unmarshal() .bindy(BindyType.Csv, JudicialUserRoleType.class)
                .process(new JudicialUserRoleTypeProcessor())
                .split().body()
                .bean(judicialRoleTypeRowMapper , "getMap")
                .to("sql:insert into judicial_role_type (role_id,role_desc_en,role_desc_cy) values(:#role_id,:#role_desc_en,:#role_desc_cy)?dataSource=dataSource")
                .to("log:test?showAll=true")
                .end();*/
    }

    public Processor validateFile() {
        return new Processor() {
            @SuppressWarnings("unchecked")
            @Override
            public void process(Exchange exchange) throws Exception {
                List<List<String>> data = (List<List<String>>) exchange.getIn().getBody();
                Map<String, Object> headers = exchange.getIn().getHeaders();
                // iterate over each line
                for (String line : data.get(0)) {
                    log.info("Total columns: " + data.get(0).size());
                    log.info("Column::Name::" + line); // first column
                }
            }
        };
    }

}
