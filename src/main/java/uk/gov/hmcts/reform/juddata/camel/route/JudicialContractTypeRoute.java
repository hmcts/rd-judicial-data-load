package uk.gov.hmcts.reform.juddata.camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialContractType;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialContractTypeRowMapper;
import uk.gov.hmcts.reform.juddata.camel.processor.JudicialContractTypeProcessor;


@Component
public class JudicialContractTypeRoute extends RouteBuilder {

    @Autowired
    JudicialContractTypeRowMapper judicialContractTypeRowMapper;

    @Override
    public void configure() throws Exception {

      /* from("azure-blob://rddemo/jrdtest/Contracts.csv?credentials=#credsreg&operation=updateBlockBlob")
                .id("role-route")
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
*/
    }

}
