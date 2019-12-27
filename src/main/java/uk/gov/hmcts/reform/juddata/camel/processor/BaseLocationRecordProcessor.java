package uk.gov.hmcts.reform.juddata.camel.processor;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import uk.gov.hmcts.reform.juddata.camel.beans.BaseLocation;


@Slf4j
public class BaseLocationRecordProcessor implements Processor {

    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) throws Exception {

        List<BaseLocation> locations = new ArrayList<>();
        List<BaseLocation> locationsRecords = (List<BaseLocation>) exchange.getIn().getBody();

        log.info("Location Records count before validation::" + locationsRecords.size());

        for (BaseLocation user : locationsRecords) {

            BaseLocation validLocation = fetch(user);
             if (null != validLocation) {

                 locations.add(validLocation);
             } else {

                 log.info("Invalid Location record ");
             }

            exchange.getIn().setBody(locations);

        }

        log.info("Location Records count After Validation::" + locations.size());
    }


    private BaseLocation fetch(BaseLocation location) {

        BaseLocation locationAfterValidation = null;
        if (null != location.getBase_location_Id()) {
            locationAfterValidation = location;
        }
        return locationAfterValidation;
    }
}
