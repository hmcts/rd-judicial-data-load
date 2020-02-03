package uk.gov.hmcts.reform.juddata.camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialOfficeAppointmentRowMapper;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialOfficeAuthorisationRowMapper;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialUserRowMapper;
import uk.gov.hmcts.reform.juddata.camel.processor.JudicialUserProfileFileReadProcessor;
import uk.gov.hmcts.reform.juddata.camel.processor.JudicialUserProfileProcessor;

@Component
public class JudicialUserProfileRoute extends RouteBuilder {

    @Autowired
    JudicialUserRowMapper judUserRowMapper;

    @Autowired
    JudicialOfficeAppointmentRowMapper judicialOfficeAppointmentRowMapper;

    @Autowired
    JudicialOfficeAuthorisationRowMapper judicialOfficeAuthorisationRowMapper;

    @Override
    public void configure() {

        from("timer://JudicialUserFileProcessorTimer?period=180s")
                .id("judicial-office")
                .process(new JudicialUserProfileFileReadProcessor()).unmarshal().bindy(BindyType.Csv, JudicialUserProfile.class)
                .process(new JudicialUserProfileProcessor())
                .split().body()
                .bean(judUserRowMapper, "getMap")
                .to("sql:insert into judicial_user_profile(elinks_id,personal_code,title,known_as,surname,full_name,post_nominals,contract_type,work_pattern,email_id,joining_date,last_working_date,"
                        + "active_flag,extracted_date,created_date,last_loaded_date) "
                        + "values(:#elinks_id,:#personal_code,:#title,:#known_as,:#surname,:#full_name,:#post_nominals,"
                        + ":#contract_type,:#work_pattern, :#email_id,:#joining_date,:#last_working_date,:#active_flag, "
                        + ":#extracted_date, now() at time zone 'utc', now() at time zone 'utc') on conflict (elinks_id) do update set personal_code = :#personal_code, title = :#title,"
                        + " known_as = :#known_as, surname = :#surname, full_name = :#full_name, post_nominals =:#post_nominals, "
                        + " contract_type = :#contract_type, work_pattern = :#work_pattern, email_id = :#email_id, joining_date = :#joining_date, "
                        + "last_working_date = :#last_working_date, active_flag = :#active_flag, "
                        + "extracted_date = :#extracted_date, last_loaded_date = now() at time zone 'utc'?dataSource=dataSource")
                .to("log:test?showAll=true")
                .end();
    }

}


