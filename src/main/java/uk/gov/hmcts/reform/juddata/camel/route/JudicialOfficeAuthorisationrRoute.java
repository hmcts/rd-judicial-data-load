package uk.gov.hmcts.reform.juddata.camel.route;


import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialOfficeAuthorisation;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialOfficeAuthorisationRowMapper;
import uk.gov.hmcts.reform.juddata.camel.processor.JudicialOfficeAuthorisationProcessor;

@Component
public class JudicialOfficeAuthorisationrRoute extends RouteBuilder {

    @Autowired
    JudicialOfficeAuthorisationRowMapper judicialOfficeAuthorisationRowMapper;
    @Override
    public void configure() throws Exception {

              /* from("timer:hello?repeatCount=1")
                 .to("sql:TRUNCATE judicial_user,judicial_office_appointment,judicial_office_authorization?dataSource=dataSource")
                .to("log:test?showAll=true").end();


        from("azure-blob://rddemo/jrdtest/judicial_userprofile.csv?credentials=#credsreg&operation=updateBlockBlob")
                .id("judicial-user-route")
            .to("file://blobdirectory?noop=true&fileExist=Override").end();

        from("file://blobdirectory?noop=true&fileExist=Override")
                .onCompletion()
                .log("CSV data  processing finished").end()
                .transacted()
                .split(body().tokenize("\n", 1, true)).streaming()
                .unmarshal().csv()
                .split(body())
                .log("Processing CSV data judicial-user-route---1 ---- ${body}")
                .to("sql:insert into judicial_user (sno,firstName,LastName,Circuit,Area) values(#, #, #, #, #)?dataSource=dataSource")
                .to("log:test?showAll=true").end();

        //==================================2===================

        from("azure-blob://rddemo/jrdtest/judicial_office_appointment.csv?credentials=#credsreg&operation=updateBlockBlob")
                .id("judicial-office-appointment")
                .to("file://blobdirectory2?noop=true&fileExist=Override").end();


        from("file://blobdirectory2?noop=true&fileExist=Override")
                .onCompletion().log("CSV data  processing finished for route 2").end()
                .transacted()
                .split(body().tokenize("\n", 1, true)).streaming()
                .unmarshal().csv()
                .split(body())
                .log("Processing CSV data ---2 ---- ${body}")
                .to("sql:insert into judicial_office_appointment(sno,firstName,LastName,Circuit,Area) values(#, #, #, #, #)?dataSource=dataSource")

                .to("log:test?showAll=true").end();

        //==================================3===================

        from("azure-blob://rddemo/jrdtest/judicial_office_authorisation.csv?credentials=#credsreg&operation=updateBlockBlob")
                .id("jud-office-auth-route")
                .to("file://blobdirectory3?noop=true").end();


        from("file://blobdirectory3?noop=true")
                .onCompletion().log("CSV data  processing finished for route 3").end()
                .transacted()
                .split(body().tokenize("\n", 1, true)).streaming()
                .unmarshal().csv()
                .split(body())
                .log("Processing CSV data ---3 ---- ${body}")

                .to("sql:insert into judicial_office_authorization(sno,firstName,LastName,Circuit,Area) values(#, #, #, #, #)?dataSource=dataSource")

                .to("log:test?showAll=true").end();*/
                /*from("file:src/data?noop=true")
                .doTry()
                .to("direct:split")


                .doCatch(Exception.class)
                .to("file:target/messages?fileName=deadLetters.xml&fileExist=Append")
                        .rollback()
                .end();*/

        from("azure-blob://rddemo/jrdtest/judicial_office_authorisation.csv?credentials=#credsreg&operation=updateBlockBlob")
                .id("role-route")
                .to("file://blobdirectory7?noop=true&fileExist=Override").end();

        from("file://blobdirectory7?noop=true&fileExist=Override")
                .unmarshal() .bindy(BindyType.Csv, JudicialOfficeAuthorisation.class)
                .process(new JudicialOfficeAuthorisationProcessor())
                .split().body()
                .bean(judicialOfficeAuthorisationRowMapper , "getMap")
                .to("sql:insert into judicial_office_authorisation (judicial_office_auth_id,elinks_id,authorisation_id,jurisdiction_id,authorisation_date,extracted_date,created_date,last_loaded_date) " +
                        "values(:#judicial_office_auth_id,:#elinks_id,:#authorisation_id, :#jurisdiction_id,:#authorisation_date,:#extracted_date,:#created_date,:#last_loaded_date)?dataSource=dataSource")
                .to("log:test?showAll=true")
                .end();

    }

}
