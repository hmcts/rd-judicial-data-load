package uk.gov.hmcts.reform.juddata.camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialUserRowMapper;
import uk.gov.hmcts.reform.juddata.camel.processor.RecordProcessor;

@Component
public class JudicialOfficeAppointmentProcessorRoute extends RouteBuilder {

    /*@Autowired
    JudicialUserRowMapper rowMapper ;*/

    public void configure() throws Exception {

        /*from("timer:hello?repeatCount=1")
                .to("sql:TRUNCATE judicial_user?dataSource=dataSource")
                .to("log:test?showAll=true").end();

        from("azure-blob://rddemo/jrdtest/judicial_userprofile.csv?credentials=#credsreg&operation=updateBlockBlob")
                .id("judicial-office")
                .to("file://blobdirectory?noop=true&fileExist=Override").end();
               // .to("log:foo").end();

        from("file://blobdirectory?noop=true&fileExist=Override")
                        //.onCompletion()
                        .unmarshal() .bindy(BindyType.Csv, JudicialUserProfile.class)
                      //  .split().body()
                        .process(new RecordProcessor())
                        .split().body()
                        .bean(rowMapper , "getMap")
                        .to("sql:insert into judicial_user(sno,firstName,LastName,Circuit,Area) values(:#sno,:#firstName,:#lastName, :#circuit,:#area)?dataSource=dataSource")
                        .to("log:test?showAll=true")
                        .end();
*/
    }

}
