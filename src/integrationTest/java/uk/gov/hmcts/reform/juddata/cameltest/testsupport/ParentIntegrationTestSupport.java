package uk.gov.hmcts.reform.juddata.cameltest.testsupport;

import static uk.gov.hmcts.reform.juddata.cameltest.testsupport.IntegrationTestSupport.setSourcePath;

public interface ParentIntegrationTestSupport extends IntegrationTestSupport {

    String[] file = {"classpath:sourceFiles/parent/judicial_userprofile.csv", "classpath:sourceFiles/parent/judicial_appointments.csv", "classpath:sourceFiles/parent/judicial_office_authorisation.csv"};

    String[] fileWithError = {"classpath:sourceFiles/parent/judicial_userprofile.csv", "classpath:sourceFiles/parent/judicial_appointments_error.csv", "classpath:sourceFiles/parent/judicial_office_authorisation.csv"};

    String[] fileWithSingleRecord = {"classpath:sourceFiles/parent/judicial_userprofile_singlerecord.csv", "classpath:sourceFiles/parent/judicial_appointments_singlerecord.csv", "classpath:sourceFiles/parent/judicial_office_authorisation_singlerecord.csv"};

    String[] fileWithInvalidHeader = {"classpath:sourceFiles/parent/judicial_userprofile.csv", "classpath:sourceFiles/parent/judicial_appointments_invalidheader.csv", "classpath:sourceFiles/parent/judicial_office_authorisation.csv"};

    String[] fileWithAuthorisationInvalidHeader = {"classpath:sourceFiles/parent/judicial_userprofile.csv", "classpath:sourceFiles/parent/judicial_appointments.csv", "classpath:sourceFiles/parent/judicial_office_authorisation_invalidheader.csv"};

    String[] fileWithInvalidJsr = {"classpath:sourceFiles/parent/judicial_userprofile_jsr.csv", "classpath:sourceFiles/parent/judicial_appointments_jsr.csv", "classpath:sourceFiles/parent/judicial_office_authorisation_jsr_partial_success.csv"};

    String[] fileWithInvalidJsrExceedsThreshold = {"classpath:sourceFiles/parent/judicial_userprofile_jsr.csv", "classpath:sourceFiles/parent/judicial_appointments_jsr_exccedthreshold.csv", "classpath:sourceFiles/parent/judicial_office_authorisation.csv"};

    String[] fileWithElinkIdMissing = {"classpath:sourceFiles/parent/judicial_userprofile.csv", "classpath:sourceFiles/parent/judicial_appointments_elinks_missing.csv", "classpath:sourceFiles/parent/judicial_office_authorisation.csv"};

    String[] fileWithAuthElinkIdMissing = {"classpath:sourceFiles/parent/judicial_userprofile.csv", "classpath:sourceFiles/parent/judicial_appointments.csv", "classpath:sourceFiles/parent/judicial_office_authorisation_elinks_missings.csv"};

    String[] fileWithElinkIdInvalidInParent = {"classpath:sourceFiles/parent/judicial_userprofile_jsr.csv", "classpath:sourceFiles/parent/judicial_appointments_invalid_jsr_parent_elinks.csv"};

    static void setSourceData(String... files) throws Exception {
        System.setProperty("parent.file.name", files[0]);
        System.setProperty("child1.file.name", files[1]);
        System.setProperty("child2.file.name", files[2]);
        setSourcePath(files[0],
                "parent.file.path");
        setSourcePath(files[1],
                "child1.file.path");
        setSourcePath(files[2],
                "child2.file.path");
    }
}
