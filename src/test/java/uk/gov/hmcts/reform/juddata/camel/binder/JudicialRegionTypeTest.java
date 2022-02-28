package uk.gov.hmcts.reform.juddata.camel.binder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialRegionType;

class JudicialRegionTypeTest {

    @Test
    void test_objects_JudicialOfficeAppointment_correctly() {
        JudicialRegionType judicialRegionType = createJudicialRegionType();

        Assertions.assertEquals("regionId", judicialRegionType.getRegionId());
        Assertions.assertEquals("region_desc_en", judicialRegionType.getRegionDescEn());
        Assertions.assertEquals("region_desc_cy", judicialRegionType.getRegionDescCy());
    }
}
