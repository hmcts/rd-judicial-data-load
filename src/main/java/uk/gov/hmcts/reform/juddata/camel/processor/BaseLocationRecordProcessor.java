package uk.gov.hmcts.reform.juddata.camel.processor;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import uk.gov.hmcts.reform.juddata.camel.beans.BaseLocationType;


@Slf4j
public class BaseLocationRecordProcessor implements Processor {


    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) throws Exception {

        List<BaseLocationType> locations = new ArrayList<>();
        List<BaseLocationType> locationsRecords = (List<BaseLocationType>) exchange.getIn().getBody();

        log.info("Location Records count before validation::" + locationsRecords.size());

        for (BaseLocationType user : locationsRecords) {
            BaseLocationType validLocation = fetch(user);
            if (null != validLocation) {

                locations.add(validLocation);
            } else {

                log.info("Invalid Location record ");
            }

            exchange.getIn().setBody(locations);
        }

        log.info("Location Records count After Validation::" + locations.size());
    }


    private BaseLocationType fetch(BaseLocationType location) {
        BaseLocationType locationAfterValidation = null;
        if (null != location.getBaseLocationId()) {
            locationAfterValidation = location;
        }
        return locationAfterValidation;
    }
}
