package uk.gov.hmcts.reform.juddata.camel.processor;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialOfficeAuthorisation;

@Slf4j
@Component
public class JudicialOfficeAuthorisationProcessor implements Processor {

    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) throws Exception {

        List<JudicialOfficeAuthorisation> users = new ArrayList<>();
        List<JudicialOfficeAuthorisation> judicialOfficeAppointment = (List<JudicialOfficeAuthorisation>) exchange.getIn().getBody();

        log.info("JudicialOfficeAuthorisation Records count before validation::" + judicialOfficeAppointment.size());

        for (JudicialOfficeAuthorisation officeAppointment : judicialOfficeAppointment) {

            JudicialOfficeAuthorisation validUser = fetch(officeAppointment);
            if (null != validUser) {

                users.add(validUser);
            } else {

                log.info(" Invalid JudicialOfficeAppointment record ");
            }

            exchange.getIn().setBody(users);

        }

        log.info(" JudicialOfficeAuthorisation Records count After Validation::" + users.size());
    }


    private JudicialOfficeAuthorisation fetch(JudicialOfficeAuthorisation officeAuthorization) {

        JudicialOfficeAuthorisation officeAuthoAfterValidation = null;

        if (null != officeAuthorization.getElinksId()) {

            officeAuthoAfterValidation = officeAuthorization;

        }
        return officeAuthoAfterValidation;

    }
}
