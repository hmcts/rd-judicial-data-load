package uk.gov.hmcts.reform.juddata.camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialUserRoleType;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialRoleTypeRowMapper;
import uk.gov.hmcts.reform.juddata.camel.processor.JudicialUserRoleTypeProcessor;

@Component
public class JudicialUserRoleTypeRoute extends RouteBuilder {

    @Autowired
    JudicialRoleTypeRowMapper judicialRoleTypeRowMapper;

    @Override
    public void configure() throws Exception {

      /* from("azure-blob://rddemo/jrdtest/Roles.csv?credentials=#credsreg&operation=updateBlockBlob")
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

}
