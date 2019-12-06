package uk.gov.hmcts.reform.juddata.camel;


import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class JudicialOfficeAuthorisationrRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {

             /*  from("azure-blob://rddemo/jrdtest/judicial_office_authorisation.csv?credentials=#credsreg")
                .id("jud-office-auth-route")
                .to("file://blobdirectory3").end();

                from("file://blobdirectory3?noop=true")
                .onCompletion().log("CSV data  processing finished").end()

                . split(body().tokenize("\n",1,true)).streaming()
                .unmarshal().csv()

               // .unmarshal().bindy(BindyType.Csv, JrdCsvDataMapper.class)
               // .log("Processing CSV data -- 2 ${body}")
                //  .to("bean:myCsvHandler?method=doHandleCsvData");
                //.to("mock:daltons")
                .split(body())
                // .process(new GetRecordsProcess())
                .log("Processing CSV data jud-office-auth-route ---3 ---- ${body}")
               // .to //("log:test?level=DEBUG")
                .to("sql:insert into judicial_office_authorization(sno,firstName,LastName,Circuit,Area) values(#, #, #, #, #)?dataSource=dataSource")
                .to ("log:test?showAll=true")
                .end();*/

    }

}
