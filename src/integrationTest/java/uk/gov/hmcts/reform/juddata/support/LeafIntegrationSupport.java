package uk.gov.hmcts.reform.juddata.support;

import static uk.gov.hmcts.reform.juddata.support.IntegrationTestSupport.setSourcePath;

public interface LeafIntegrationSupport extends IntegrationTestSupport {
    String[] file = {
        "classpath:sourceFiles/leaf/Locations.csv", "classpath:sourceFiles/leaf/BaseLocations.csv"
    };

    String[] file_jsr_error = {"classpath:sourceFiles/leaf/Locations_jsr.csv",
        "classpath:sourceFiles/leaf/BaseLocations_jsr.csv"
    };

    static void setSourceData(String... files) throws Exception {

        System.setProperty("region.file.name", files[0]);
        System.setProperty("base.location.file.name", files[1]);
        setSourcePath(files[0],
            "region.file.path");
        setSourcePath(files[1],
            "base.location.file.path");
    }
}
