package uk.gov.hmcts.reform.juddata.camel.binder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdTestSupport.createJudicialRegionType;

class JudicialRegionTypeTest {

    @Test
    void test_objects_JudicialOfficeAppointment_correctly() {
        JudicialRegionType judicialRegionType = createJudicialRegionType();

        assertEquals("regionId", judicialRegionType.getRegionId());
        assertEquals("region_desc_en", judicialRegionType.getRegionDescEn());
        assertEquals("region_desc_cy", judicialRegionType.getRegionDescCy());
        assertEquals("28-04-2022 00:00:00", judicialRegionType.getMrdCreatedTime());
        assertEquals("28-05-2022 00:00:00", judicialRegionType.getMrdUpdatedTime());
        assertEquals("28-06-2022 00:00:00", judicialRegionType.getMrdDeletedTime());
    }
}
