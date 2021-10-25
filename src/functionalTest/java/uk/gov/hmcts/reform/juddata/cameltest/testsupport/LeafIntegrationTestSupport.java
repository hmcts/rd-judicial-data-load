package uk.gov.hmcts.reform.juddata.cameltest.testsupport;

public interface LeafIntegrationTestSupport {

    String[] file = {"classpath:sourceFiles/leaf/Locations.csv", "classpath:sourceFiles/leaf/BaseLocations.csv"};

    String[] file_jsr_error = {"classpath:sourceFiles/leaf/Locations_jsr.csv",
        "classpath:sourceFiles/leaf/BaseLocations_jsr.csv"
    };

    String[] file_error = {"classpath:sourceFiles/leaf/Locations.csv", "classpath:sourceFiles/leaf/BaseLocations.csv"};
}
