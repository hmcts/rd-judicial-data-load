package uk.gov.hmcts.reform.juddata.cameltest.testsupport;

import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.IntegrationTestSupport.setSourcePath;

public interface ParentIntegrationTestSupport extends IntegrationTestSupport {

    String[] file = {"classpath:sourceFiles/parent/judicial_userprofile.csv", "classpath:sourceFiles/parent/judicial_appointments.csv"};

    String[] fileWithError = {"classpath:sourceFiles/parent/judicial_userprofile.csv", "classpath:sourceFiles/parent/judicial_appointments_error.csv"};

    String[] fileWithSingleRecord = {"classpath:sourceFiles/parent/judicial_userprofile_singlerecord.csv", "classpath:sourceFiles/parent/judicial_appointments_singlerecord.csv"};

    String[] fileWithInvalidHeader = {"classpath:sourceFiles/parent/judicial_userprofile.csv", "classpath:sourceFiles/parent/judicial_appointments_invalidheader.csv"};

    String[] fileWithInvalidJsr = {"classpath:sourceFiles/parent/judicial_userprofile_jsr.csv", "classpath:sourceFiles/parent/judicial_appointments_jsr.csv"};

    String[] fileWithInvalidJsrExceedsThreshold = {"classpath:sourceFiles/parent/judicial_userprofile_jsr.csv", "classpath:sourceFiles/parent/judicial_appointments_jsr_exccedthreshold.csv"};

    String[] fileWithUniqueViolation = {"classpath:sourceFiles/parent/judicial_userprofile_unique_violation.csv", "classpath:sourceFiles/parent/judicial_appointments.csv"};

    static void setSourceData(String... files) throws Exception {
        System.setProperty("parent.file.name", files[0]);
        System.setProperty("child.file.name", files[1]);
        setSourcePath(files[0],
                "parent.file.path");
        setSourcePath(files[1],
                "child1.file.path");
    }
}
