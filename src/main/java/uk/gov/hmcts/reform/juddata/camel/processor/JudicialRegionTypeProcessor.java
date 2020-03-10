package uk.gov.hmcts.reform.juddata.camel.processor;

import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialRegionType;

@Slf4j
@Component
public class JudicialRegionTypeProcessor implements Processor {

    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) throws Exception {

        List<JudicialRegionType> regionTypes = new ArrayList<>();
        List<JudicialRegionType> judicialRegionTypes = (List<JudicialRegionType>) exchange.getIn().getBody();
        for (JudicialRegionType regionType : judicialRegionTypes) {

            JudicialRegionType validRegionType = fetch(regionType);
            if (nonNull(validRegionType)) {

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
        if (nonNull(regionType.getRegionId())) {
            regionTypeAfterValidation = regionType;
        }
        return regionTypeAfterValidation;
    }
}
