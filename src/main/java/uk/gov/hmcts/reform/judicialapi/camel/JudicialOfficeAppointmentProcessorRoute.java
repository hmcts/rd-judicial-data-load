package uk.gov.hmcts.reform.judicialapi.camel;


import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class JudicialOfficeAppointmentProcessorRoute extends RouteBuilder {



    @Override
    public void configure() throws Exception {

                /*from("azure-blob://rddemo/jrdtest/judicial_office_appointment.csv?credentials=#credsreg")
                .id("judicial-office-appointment")
                .to("file://blobdirectory2").end();

                from("file://blobdirectory2?noop=true")
                .onCompletion().log("CSV data  processing finished").end()
                . split(body().tokenize("\n",1,true)).streaming()
                .unmarshal().csv()
                .log("Processing CSV data -- 2 ${body}")
                //  .to("bean:myCsvHandler?method=doHandleCsvData");
                .split(body())
                .log("Processing CSV data ---3 ---- ${body}")
                .to("sql:insert into judicial_office_appointment(sno,firstName,LastName,Circuit,Area) values(#, #, #, #, #)?dataSource=dataSource")
                .to ("log:test?showAll=true")
                .end();*/


    }

}
