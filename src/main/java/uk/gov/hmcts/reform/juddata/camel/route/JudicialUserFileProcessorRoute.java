package uk.gov.hmcts.reform.juddata.camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.apache.camel.routepolicy.quartz2.CronScheduledRoutePolicy;
import org.apache.camel.routepolicy.quartz2.SimpleScheduledRoutePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialOfficeAuthorisation;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialOfficeAppointmentRowMapper;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialOfficeAuthorisationRowMapper;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialUserRowMapper;
import uk.gov.hmcts.reform.juddata.camel.processor.JudicialOfficeAppointmentProcessor;
import uk.gov.hmcts.reform.juddata.camel.processor.JudicialOfficeAuthorisationProcessor;
import uk.gov.hmcts.reform.juddata.camel.processor.JudicialUserProfileProcessor;

import java.util.Date;

@Component
public class JudicialUserFileProcessorRoute extends RouteBuilder
{

    @Autowired
    JudicialUserRowMapper judUserRowMapper;
    @Autowired
    JudicialOfficeAppointmentRowMapper judicialOfficeAppointmentRowMapper;
    @Autowired
    JudicialOfficeAuthorisationRowMapper judicialOfficeAuthorisationRowMapper;

    public static final String ROUTE_NAME = "MYROUTE";


    @Override
    public void configure() {

       // CronScheduledRoutePolicy policy = new CronScheduledRoutePolicy();
       // policy.setRouteStartTime("* */3 * * * ?");

       /* from("azure-blob://rddemo/jrdtest/judicial_userprofile.csv?credentials=#credsreg&operation=updateBlockBlob")
                .id("judicial-office")
               // .routePolicy(policy)
                .to("file://blobdirectory?noop=true&fileExist=Override").end();


        from("file://blobdirectory?noop=true&fileExist=Override")
               // .startupOrder(1)
                // .onCompletion()
                .routePolicy(policy)
                .unmarshal() .bindy(BindyType.Csv, JudicialUserProfile.class)
                //  .split().body()
                .process(new JudicialUserProfileProcessor())
                .split().body()
                .bean(judUserRowMapper , "getMap")
                .to("sql:insert into judicial_user_profile(elinks_id,personal_code,title,known_as,surname,full_name,post_nominals,contract_type,work_pattern,email_id,joining_date,last_working_date,active_flag,extracted_date) " +
                        "values(:#elinks_id,:#personal_code,:#title, :#known_as,:#surname, :#full_name,:#post_nominals,:#contract_type,:#work_pattern, :#email_id,:#joining_date, :#last_working_date, :#active_flag, :#extracted_date)?dataSource=dataSource")
                .to("log:test?showAll=true")
                .end();*/

       /* from("azure-blob://rddemo/jrdtest/Appointments.csv?credentials=#credsreg&operation=updateBlockBlob")
                .id("office-appointment")
               // .startupOrder(15)
                .to("file://blobdirectory3?noop=true&fileExist=Override").end();

        from("file://blobdirectory3?noop=true&fileExist=Override")
                //.startupOrder(16)
                .unmarshal() .bindy(BindyType.Csv, JudicialOfficeAppointment.class)
                .process(new JudicialOfficeAppointmentProcessor())
                .split().body()
                .bean(judicialOfficeAppointmentRowMapper , "getMap")
                .to("sql:insert into judicial_office_appointment (judicial_office_appointment_id,elinks_id,role_id,contract_type_id,base_location_id,region_id,is_prinicple_appointment,start_date,end_date,active_flag,extracted_date) " +
                        "values(:#judicial_office_appointment_id, :#elinks_id,:#role_id, :#contract_type_id,:#base_location_id, :#region_id, " +
                        ":#is_prinicple_appointment, :#start_date, :#end_date, :#active_flag,:#extracted_date)?dataSource=dataSource")
                .to("log:test?showAll=true")
                .end();


        from("azure-blob://rddemo/jrdtest/judicial_office_authorisation.csv?credentials=#credsreg&operation=updateBlockBlob")
                .id("role-route")
                //.startupOrder(13)
                .to("file://blobdirectory7?noop=true&fileExist=Override").end();

        from("file://blobdirectory7?noop=true&fileExist=Override")
                //.startupOrder(14)
                .unmarshal() .bindy(BindyType.Csv, JudicialOfficeAuthorisation.class)
                .process(new JudicialOfficeAuthorisationProcessor())
                .split().body()
                .bean(judicialOfficeAuthorisationRowMapper , "getMap")
                .to("sql:insert into judicial_office_authorisation (judicial_office_auth_id,elinks_id,authorisation_id,jurisdiction_id,authorisation_date,extracted_date,created_date,last_loaded_date) " +
                        "values(:#judicial_office_auth_id,:#elinks_id,:#authorisation_id, :#jurisdiction_id,:#authorisation_date,:#extracted_date,:#created_date,:#last_loaded_date)?dataSource=dataSource")
                .to("log:test?showAll=true")
                .end();*/

    }
}
