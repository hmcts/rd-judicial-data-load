package uk.gov.hmcts.reform.juddata.camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.mapper.JudicialOfficeAppointmentRowMapper;
import uk.gov.hmcts.reform.juddata.camel.processor.JudicialOfficeAppointmentProcessor;

@Component
public class JudicialOfficeAppointmentProcessorRoute extends RouteBuilder {

    @Autowired
    JudicialOfficeAppointmentRowMapper judicialOfficeAppointmentRowMapper;

    @Override
    public void configure() throws Exception {

        from("azure-blob://rddemo/jrdtest/Appointments.csv?credentials=#credsreg&operation=updateBlockBlob")
                .id("office-appointment")
                .to("file://blobdirectory3?noop=true&fileExist=Override").end();

        from("file://blobdirectory3?noop=true&fileExist=Override")
                .unmarshal() .bindy(BindyType.Csv, JudicialOfficeAppointment.class)
                .process(new JudicialOfficeAppointmentProcessor())
                .split().body()
                .bean(judicialOfficeAppointmentRowMapper , "getMap")
                .to("sql:insert into judicial_office_appointment (judicial_office_appointment_Id,elinks_id,role_id,contract_type_Id,base_location_Id,region_Id,is_prinicple_appointment,start_date,end_date,active_flag,extracted_date) " +
                        "values(:#judicial_office_appointment_Id, :#elinks_id,:#role_id, :#contract_type_Id,:#base_location_Id, :#region_Id, " +
                        ":#is_prinicple_appointment, :#start_date, :#end_date, :#active_flag,:#extracted_date)?dataSource=dataSource")
                .to("log:test?showAll=true")
                .end();

    }

}
