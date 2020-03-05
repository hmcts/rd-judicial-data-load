package uk.gov.hmcts.reform.juddata.camel.processor;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialBaseLocationType;

@Slf4j
@Component
public class JudicialBaseLocationProcessor implements Processor{
    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) throws Exception {

        List<JudicialBaseLocationType> locations = new ArrayList<>();
        List<JudicialBaseLocationType> locationsRecords = (List<JudicialBaseLocationType>) exchange.getIn().getBody();

        for (JudicialBaseLocationType user : locationsRecords) {

            JudicialBaseLocationType validLocation = fetch(user);
            if (null != validLocation) {

                locations.add(validLocation);
            } else {

                log.info("Invalid Location record ");
            }

            exchange.getIn().setBody(locations);

        }

        log.info("Location Records count After Validation::" + locations.size());
    }


    private JudicialBaseLocationType fetch(JudicialBaseLocationType location) {

        JudicialBaseLocationType locationAfterValidation = null;
        if (null != location.getBase_location_id()) {
            locationAfterValidation = location;
        }
        return locationAfterValidation;
    }
}
