package uk.gov.hmcts.reform.judicialapi.camel.routes;

import com.microsoft.azure.storage.blob.BlobInputStream;
import com.microsoft.azure.storage.blob.BlobOutputStream;
import com.microsoft.azure.storage.file.FileOutputStream;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.*;

@Component
public class SimpleRouteBuilder //extends RouteBuilder

{

    /*@Override
    public void configure() throws Exception {

        //from("file:C:/inbox?noop=true").split().tokenize("\n").dynamicRouter(method(DynamicRouterBean.class, "route"));
        from("azure-blob://rddemo/jrdtest/judicial_userprofile.csv?credentials=#credsreg&operation=updateBlockBlob")
                .dynamicRouter(method(DynamicRouterBean1.class, "route"));

          //.to("log:test?showAll=true");

        from("direct:route1").process(new Processor() {
            public void process(Exchange exchange) {
                String body = exchange.getIn().getBody().toString();
                BlobInputStream blob = (BlobInputStream)exchange.getIn().getBody();
                try {
                    FileInputStream fis = new  FileInputStream(blob.toString());
                    //IOUtils.toString(fis, StandardCharsets.UTF_8);
                    System.out.println("blob:: " + blob);
                    System.out.println("String:: " + body);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); // assuming buffer is a byte[] with your data
                System.out.println(blob);
            }
        });*/

        /*from("direct:route2").process(new Processor() {
            public void process(Exchange exchange) {
                String body = exchange.getIn().getBody().toString();
                body = body + " in route 2";
                System.out.println(body);
                exchange.getIn().setBody(body);
            }
        });

        from("direct:route3").process(new Processor() {
            public void process(Exchange exchange) {
                String body = exchange.getIn().getBody().toString();
                body = body + " in route 3";
                exchange.getIn().setBody(body);
                System.out.println(body);
            }
        });*/
   // }
}
