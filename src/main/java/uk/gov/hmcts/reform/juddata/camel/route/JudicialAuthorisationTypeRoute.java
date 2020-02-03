package uk.gov.hmcts.reform.juddata.camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialAuthorisationTypeRowMapper;

@Component
public class JudicialAuthorisationTypeRoute extends RouteBuilder {

    @Autowired
    JudicialAuthorisationTypeRowMapper judicialAuthorizationTypeRowMapper;

    @Override
    public void configure() throws Exception {

       /* from("timer:hello?repeatCount=1")
                .to("sql:TRUNCATE base_location_type,judicial_office_appointment,contract_type,region_type,judicial_role_type?dataSource=dataSource")
                .to("log:test?showAll=true").end();*/

       /* from("azure-blob://rddemo/jrdtest/authorisation_type.csv?credentials=#credsreg&operation=updateBlockBlob")
                .id("role-route")
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
                .end();*/
    }

}
