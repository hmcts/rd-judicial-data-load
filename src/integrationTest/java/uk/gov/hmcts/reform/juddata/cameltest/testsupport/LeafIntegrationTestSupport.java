package uk.gov.hmcts.reform.juddata.cameltest.testsupport;

import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.IntegrationTestSupport.setSourcePath;

public interface LeafIntegrationTestSupport extends IntegrationTestSupport {

    String[] file = {"classpath:sourceFiles/leaf/Roles.csv", "classpath:sourceFiles/leaf/Contracts.csv", "classpath:sourceFiles/leaf/Locations.csv", "classpath:sourceFiles/leaf/BaseLocations.csv"
    };

    String[] file_error = {"classpath:sourceFiles/leaf/Roles.csv", "classpath:sourceFiles/leaf/Contracts-invalid-header.csv", "classpath:sourceFiles/leaf/Locations.csv", "classpath:sourceFiles/leaf/BaseLocations.csv"
    };

    String[] file_jsr_error = {"classpath:sourceFiles/leaf/Roles_jsr.csv", "classpath:sourceFiles/leaf/Contracts_jsr.csv", "classpath:sourceFiles/leaf/Locations_jsr.csv", "classpath:sourceFiles/leaf/BaseLocations_jsr.csv"
    };

    static void setSourceData(String... files) throws Exception {
        System.setProperty("role.file.name", files[0]);
        System.setProperty("contract.file.name", files[1]);
        System.setProperty("region.file.name", files[2]);
        System.setProperty("base.location.file.name", files[3]);
        setSourcePath(files[0],
                "role.file.path");
        setSourcePath(files[1],
                "contract.file.path");
        setSourcePath(files[2],
                "region.file.path");
        setSourcePath(files[3],
                "base.location.file.path");
    }
}
