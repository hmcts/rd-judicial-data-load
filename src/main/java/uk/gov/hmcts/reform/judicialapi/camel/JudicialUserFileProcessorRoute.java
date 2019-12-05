package uk.gov.hmcts.reform.judicialapi.camel;


import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileConverter;
import org.apache.camel.routepolicy.quartz.CronScheduledRoutePolicy;
import org.springframework.stereotype.Component;

@Component
public class JudicialUserFileProcessorRoute extends RouteBuilder
{


    public static final String ROUTE_NAME = "MYROUTE";


    @Override
    public void configure() {

        //  from("azure-blob://rddemo/jrdtest/judicial_userprofile.csv?credentials=#credsreg")
        // from("timer:hello?repeatCount=1")
        // from("azure-blob://rddemo/jrdtest/jrd1.csv?credentials=#credsreg&operation=deleteBlob")
        // .to("azure-blob://rddemo/jrd-archive/judicial_userprofile.csv?credentials=#credsreg&operation=updateBlockBlob")
             /*    from("file://blobdirectory?noop=true")
                         .to("log:test?showAll=true")
              //  .convertBodyTo(java.lang.class)
                         .process(new Processor() {
                             @Override
                             public void process(Exchange exchange) throws Exception {
                                 Object file = exchange.getIn().getMandatoryBody();

                                 exchange.getOut().setBody(
                                         GenericFileConverter.genericFileToInputStream(
                                                 (GenericFile<?>) file, exchange));
                             }
                         })
                .to("azure-blob://rddemo/jrdtest/blob1?credentials=#credsreg")
                .to("log:test?showAll=true");*//*

         */

        from("timer:hello?repeatCount=1")
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

                .to("log:test?showAll=true").end();
                /*from("file:src/data?noop=true")
                .doTry()
                .to("direct:split")


                .doCatch(Exception.class)
                .to("file:target/messages?fileName=deadLetters.xml&fileExist=Append")
                        .rollback()
                .end();*/


    }
}
