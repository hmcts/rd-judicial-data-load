package uk.gov.hmcts.reform.juddata.camel.processor;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialUserProfile;

@Slf4j
public class RecordProcessor implements Processor {

    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) throws Exception {

        List<JudicialUserProfile> users = new ArrayList<>();
        List<JudicialUserProfile> userRecords = (List<JudicialUserProfile>) exchange.getIn().getBody();

       // JudicialUser user = (JudicialUser)exchange.getIn().getBody();
        log.info(" JudicialUser Records count before validation::" + userRecords.size());

        for (JudicialUserProfile user : userRecords) {

            JudicialUserProfile validUser = fetch(user);
             if (null != validUser) {

                 users.add(user);
             } else {

                 log.info(" Invalid JudicialUser record ");
             }

            exchange.getIn().setBody(users);

        }

        log.info(" JudicialUser Records count After Validation::" + users.size());
    }


    private JudicialUserProfile fetch(JudicialUserProfile user) {

        JudicialUserProfile userAfterValidation = null;
        if (null != user.getSno()) {

            userAfterValidation = user;

        }

        return userAfterValidation;

    }
}
