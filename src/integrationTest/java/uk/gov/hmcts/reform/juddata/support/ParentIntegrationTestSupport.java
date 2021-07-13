package uk.gov.hmcts.reform.juddata.support;

import static uk.gov.hmcts.reform.juddata.support.IntegrationTestSupport.setSourcePath;

public interface ParentIntegrationTestSupport extends IntegrationTestSupport {

    String[] file = {"classpath:sourceFiles/parent/judicial_userprofile.csv",
        "classpath:sourceFiles/parent/judicial_appointments.csv",
        "classpath:sourceFiles/parent/judicial_office_authorisation.csv"};

    String[] fileWithInvalidJsr = {"classpath:sourceFiles/judicial_userprofile_jsr.csv",
        "classpath:sourceFiles/judicial_appointments_jsr.csv",
        "classpath:sourceFiles/judicial_office_authorisation_jsr_partial_success.csv"};


    static void setSourceData(String... files) throws Exception {
        System.setProperty("parent.file.name", files[0]);
        System.setProperty("child.file.name", files[1]);
        System.setProperty("child1.file.name", files[2]);
        setSourcePath(files[0],
            "parent.file.path");
        setSourcePath(files[1],
            "child.file.path");
        setSourcePath(files[2],
            "child1.file.path");
    }
}

