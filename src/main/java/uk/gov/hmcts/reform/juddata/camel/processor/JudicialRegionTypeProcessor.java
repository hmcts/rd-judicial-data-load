package uk.gov.hmcts.reform.juddata.camel.processor;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialRegionType;

@Slf4j
@Component
public class JudicialRegionTypeProcessor implements Processor {

    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) {

        List<JudicialRegionType> regionTypes = new ArrayList<>();
        List<JudicialRegionType> judicialRegionTypes;

        if (exchange.getIn().getBody() instanceof List) {
            judicialRegionTypes = (List<JudicialRegionType>) exchange.getIn().getBody();
        } else {
            JudicialRegionType judicialRegionType = (JudicialRegionType) exchange.getIn().getBody();
            judicialRegionTypes = new ArrayList<>();
            judicialRegionTypes.add(judicialRegionType);
        }

        log.info("JudicialRegionType Records count before validation::" + judicialRegionTypes.size());

        for (JudicialRegionType regionType : judicialRegionTypes) {

            JudicialRegionType validRegionType = fetch(regionType);
            if (null != validRegionType) {

                regionTypes.add(validRegionType);
            } else {

                log.info("Invalid JudicialRegionType record ");
            }

            exchange.getIn().setBody(regionTypes);

        }

        log.info("JudicialRegionType Records count After Validation::" + regionTypes.size());
    }


    private JudicialRegionType fetch(JudicialRegionType regionType) {

        JudicialRegionType regionTypeAfterValidation = null;
        if (null != regionType.getRegionId()) {
            regionTypeAfterValidation = regionType;
        }
        return regionTypeAfterValidation;
    }
}
