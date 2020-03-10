package uk.gov.hmcts.reform.juddata.camel.binder;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.juddata.camel.helper.JrdUnitTestHelper.createJudicialRegionType;

import org.junit.Test;

public class JudicialRegionTypeTest {

    @Test
    @SuppressWarnings("unchecked")
    public void  test_objects_JudicialOfficeAppointment_correctly() {
        JudicialRegionType judicialRegionType = createJudicialRegionType();

        assertEquals("regionId", judicialRegionType.getRegionId());
        assertEquals("region_desc_en", judicialRegionType.getRegionDescEn());
        assertEquals("region_desc_cy", judicialRegionType.getRegionDescCy());
    }
}
