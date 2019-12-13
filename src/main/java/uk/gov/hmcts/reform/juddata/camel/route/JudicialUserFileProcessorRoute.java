package uk.gov.hmcts.reform.juddata.camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialUserRowMapper;
import uk.gov.hmcts.reform.juddata.camel.processor.RecordProcessor;

@Component
public class JudicialUserFileProcessorRoute extends RouteBuilder
{

    @Autowired
    JudicialUserRowMapper judUserRowMapper ;

    public static final String ROUTE_NAME = "MYROUTE";


    @Override
    public void configure() {



        /*from("timer:hello?repeatCount=1")
                .to("sql:TRUNCATE judicial_user_profile?dataSource=dataSource")
                .to("log:test?showAll=true").end();*/

        from("azure-blob://rddemo/jrdtest/Personal.csv?credentials=#credsreg&operation=updateBlockBlob")
                .id("judicial-office")
                .to("file://blobdirectory?noop=true&fileExist=Override").end();
        // .to("log:foo").end();

        from("file://blobdirectory?noop=true&fileExist=Override")
                // .onCompletion()
                .unmarshal() .bindy(BindyType.Csv, JudicialUserProfile.class)
                //  .split().body()
                .process(new RecordProcessor())
                .split().body()
                .bean(judUserRowMapper , "getMap")
                .to("sql:insert into judicial_user_profile(elinks_id,personal_code,title,known_as,surname,full_name,post_nominals,contract_type,work_pattern,email_id,joining_date,last_working_date,active_flag,extracted_date) " +
                        "values(:#elinks_id,:#personal_code,:#title, :#known_as,:#surname, :#full_name,:#post_nominals,:#contract_type,:#work_pattern, :#email_id,:#joining_date, :#last_working_date, :#active_flag, :#extracted_date)?dataSource=dataSource")
                .to("log:test?showAll=true")
                .end();



    }
}
