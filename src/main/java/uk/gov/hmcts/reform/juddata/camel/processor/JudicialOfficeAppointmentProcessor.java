package uk.gov.hmcts.reform.juddata.camel.processor;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialOfficeAppointment;

@Slf4j
@Component
public class JudicialOfficeAppointmentProcessor implements Processor {

    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) {

        List<JudicialOfficeAppointment> users = new ArrayList<>();
        List<JudicialOfficeAppointment> judicialOfficeAppointments;

        if (exchange.getIn().getBody() instanceof List) {
            judicialOfficeAppointments = (List<JudicialOfficeAppointment>) exchange.getIn().getBody();
        } else {
            JudicialOfficeAppointment judicialOfficeAppointment = (JudicialOfficeAppointment) exchange.getIn().getBody();
            judicialOfficeAppointments = new ArrayList<>();
            judicialOfficeAppointments.add(judicialOfficeAppointment);
        }

        for (JudicialOfficeAppointment officeAppointment : judicialOfficeAppointments) {

            JudicialOfficeAppointment validUser = fetch(officeAppointment);
            if (null != validUser) {

                users.add(validUser);
            } else {

                log.info(" Invalid JudicialOfficeAppointment record ");
            }

            exchange.getIn().setBody(users);

        }

        log.info("::JudicialOfficeAppointment Records count::" + users.size());
    }


    private JudicialOfficeAppointment fetch(JudicialOfficeAppointment officeAppointment) {

        JudicialOfficeAppointment offAppAfterValidation = null;
        if (null != officeAppointment.getElinksId()) {

            offAppAfterValidation = officeAppointment;

        }
        return offAppAfterValidation;

    }
}
