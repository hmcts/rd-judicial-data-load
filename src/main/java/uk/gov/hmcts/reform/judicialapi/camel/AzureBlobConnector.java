
package uk.gov.hmcts.reform.judicialapi.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.routepolicy.quartz.CronScheduledRoutePolicy;
import org.springframework.stereotype.Component;


@Component
public class AzureBlobConnector extends RouteBuilder {

        @Override
        public void configure()
        {
             //from ( "quartz://loop?cron=0+*/10+*+*+*+?")
             // from ("timer:hello?repeatCount=1")
              /*from("azure-blob://rddemo/jrdtest/simple.csv?credentials=#credsreg")
                    .to("file://blobdirectory");*/

             // from("direct:route1").recipientList().;

             //  from("direct:route1").recipientList("");

               /*   CronScheduledRoutePolicy startPolicy = new CronScheduledRoutePolicy();
                startPolicy.setRouteStartTime("0 0/3 * * * ?");
              from("file:C:/inputFolder?noop=true").routePolicy(startPolicy).noAutoStartup().process(new MyProcessor())
                        .to("file:C:/outputFolder");*/
        }

}


