package uk.gov.hmcts.reform.juddata.camel.processor;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialOfficeAppointment;

@Slf4j
public class JudicialOfficeAppointmentProcessor implements Processor {

    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) throws Exception {

        List<JudicialOfficeAppointment> users = new ArrayList<>();
        List<JudicialOfficeAppointment> judicialOfficeAppointment = (List<JudicialOfficeAppointment>) exchange.getIn().getBody();

        log.info("JudicialOfficeAppointment Records count before validation::" + judicialOfficeAppointment.size());

        for (JudicialOfficeAppointment officeAppointment : judicialOfficeAppointment) {

            JudicialOfficeAppointment validUser = fetch(officeAppointment);
            if (null != validUser) {

                users.add(validUser);
            } else {

                log.info(" Invalid JudicialOfficeAppointment record ");
            }

            exchange.getIn().setBody(users);

        }

        log.info(" JudicialOfficeAppointment Records count After Validation::" + users.size());
    }


    private JudicialOfficeAppointment fetch(JudicialOfficeAppointment officeAppointment) {

        JudicialOfficeAppointment offAppAfterValidation = null;
        if (null != officeAppointment.getElinksId()) {

            offAppAfterValidation = officeAppointment;

        }
        return offAppAfterValidation;

    }
}
