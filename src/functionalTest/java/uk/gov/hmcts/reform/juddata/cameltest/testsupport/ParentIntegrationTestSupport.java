package uk.gov.hmcts.reform.juddata.cameltest.testsupport;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.util.ResourceUtils.getFile;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.hibernate.validator.internal.util.Contracts;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ResourceUtils;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAuthorisation;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public interface ParentIntegrationTestSupport {

    String[] file = {"classpath:sourceFiles/parent/judicial_userprofile.csv",
        "classpath:sourceFiles/parent/judicial_appointments.csv",
        "classpath:sourceFiles/parent/judicial_office_authorisation.csv"};

    String[] fileWithError = {"classpath:sourceFiles/parent/judicial_userprofile.csv",
        "classpath:sourceFiles/parent/judicial_appointments_error.csv",
        "classpath:sourceFiles/parent/judicial_office_authorisation.csv"};

    String[] fileWithSingleRecord = {"classpath:sourceFiles/parent/judicial_userprofile_singlerecord.csv",
        "classpath:sourceFiles/parent/judicial_appointments_singlerecord.csv",
        "classpath:sourceFiles/parent/judicial_office_authorisation_singlerecord.csv"};

    String[] fileWithInvalidHeader = {"classpath:sourceFiles/parent/judicial_userprofile.csv",
        "classpath:sourceFiles/parent/judicial_appointments_invalidheader.csv",
        "classpath:sourceFiles/parent/judicial_office_authorisation.csv"};

    String[] fileWithAuthorisationInvalidHeader = {"classpath:sourceFiles/parent/judicial_userprofile.csv",
        "classpath:sourceFiles/parent/judicial_appointments.csv",
        "classpath:sourceFiles/parent/judicial_office_authorisation_invalidheader.csv"};

    String[] fileWithInvalidJsr = {"classpath:sourceFiles/parent/judicial_userprofile_jsr.csv",
        "classpath:sourceFiles/parent/judicial_appointments_jsr.csv",
        "classpath:sourceFiles/parent/judicial_office_authorisation_jsr_partial_success.csv"};

    String[] fileWithInvalidJsrExceedsThreshold = {"classpath:sourceFiles/parent/judicial_userprofile_jsr.csv",
        "classpath:sourceFiles/parent/judicial_appointments_jsr_exccedthreshold.csv",
        "classpath:sourceFiles/parent/judicial_office_authorisation.csv"};

    String[] fileWithElinkIdMissing = {"classpath:sourceFiles/parent/judicial_userprofile.csv",
        "classpath:sourceFiles/parent/judicial_appointments_elinks_missing.csv",
        "classpath:sourceFiles/parent/judicial_office_authorisation.csv"};

    String[] fileWithAuthElinkIdMissing = {"classpath:sourceFiles/parent/judicial_userprofile.csv",
        "classpath:sourceFiles/parent/judicial_appointments.csv",
        "classpath:sourceFiles/parent/judicial_office_authorisation_elinks_missings.csv"};

    String[] fileWithElinkIdInvalidInParent = {"classpath:sourceFiles/parent/judicial_userprofile_jsr.csv",
        "classpath:sourceFiles/parent/judicial_appointments_invalid_jsr_parent_elinks.csv",
        "classpath:sourceFiles/parent/judicial_office_authorisation.csv"};

    static void uploadBlobs(JrdBlobSupport jrdBlobSupport, List<String> archivalFileNames,
                            boolean isParent, String... files) throws Exception {
        int i = isParent ? 0 : 3;
        for (String absoluteFileName: files) {
            jrdBlobSupport.uploadFile(
                    archivalFileNames.get(i),
                    new FileInputStream(getFile(absoluteFileName))
            );
            i++;
        }
    }

    static void deleteBlobs(JrdBlobSupport jrdBlobSupport, List<String> fileNames) throws Exception {
        for (String fileName: fileNames) {
            jrdBlobSupport.deleteBlob(fileName);
        }
    }

    static void validateDbRecordCountFor(JdbcTemplate jdbcTemplate, String queryName, int expectedCount) {
        assertEquals(expectedCount, jdbcTemplate.queryForList(queryName).size());
    }

    static void validateExceptionDbRecordCount(JdbcTemplate jdbcTemplate,
                                               String queryName, int expectedCount,
                                               boolean isPartialSuccessValidation) {
        List<Map<String, Object>> exceptionList = jdbcTemplate.queryForList(queryName);

        exceptionList.forEach(exception -> {
            assertNotNull(exception.get("scheduler_name"));
            assertNotNull(exception.get("scheduler_start_time"));
            assertNotNull(exception.get("error_description"));
            assertNotNull(exception.get("updated_timestamp"));
            if (isPartialSuccessValidation) {
                assertNotNull(exception.get("table_name"));
                assertNotNull(exception.get("key"));
                assertNotNull(exception.get("field_in_error"));
            }
        });
        assertEquals(expectedCount, exceptionList.size());
    }

    static void validateUserProfileFile(JdbcTemplate jdbcTemplate, String userProfileSql) {
        List<Map<String, Object>> judicialUserProfileList = jdbcTemplate.queryForList(userProfileSql);
        assertEquals(judicialUserProfileList.size(), 2);
        Contracts.assertNotNull(judicialUserProfileList.get(0));
        Contracts.assertNotNull(judicialUserProfileList.get(1));
        assertEquals(judicialUserProfileList.get(0).get("elinks_id"), "1");
        assertEquals(judicialUserProfileList.get(1).get("elinks_id"), "2");
        assertEquals(judicialUserProfileList.get(0).get("email_id"), "joe.bloggs@ejudiciary.net");
        assertEquals(judicialUserProfileList.get(1).get("email_id"), "jo1e.bloggs@ejudiciary.net");
    }

    static void validateAppointmentFile(JdbcTemplate jdbcTemplate, String sqlChild1) {
        List<Map<String, Object>> judicialAppointmentList = jdbcTemplate.queryForList(sqlChild1);
        assertEquals(judicialAppointmentList.size(), 2);
        Contracts.assertNotNull(judicialAppointmentList.get(0));
        Contracts.assertNotNull(judicialAppointmentList.get(1));
        Contracts.assertNotNull(judicialAppointmentList.get(0).get("judicial_office_appointment_id"));
        Contracts.assertNotNull(judicialAppointmentList.get(0).get("judicial_office_appointment_id"));
        assertEquals("1", judicialAppointmentList.get(0).get("elinks_id"));
        assertEquals("2", judicialAppointmentList.get(1).get("elinks_id"));
    }

    static void validateAuthorisationFile(JdbcTemplate jdbcTemplate, String sqlChild2) {
        List<Map<String, Object>> judicialAuthorisationList = jdbcTemplate.queryForList(sqlChild2);
        List<JudicialOfficeAuthorisation> actualAuthorisations =
                judicialAuthorisationList.stream().map(authorisationMap -> {
                    JudicialOfficeAuthorisation judicialOfficeAuthorisation = new JudicialOfficeAuthorisation();
                    judicialOfficeAuthorisation.setElinksId((String)authorisationMap.get("elinks_id"));
                    judicialOfficeAuthorisation.setJurisdiction((String)authorisationMap.get("jurisdiction"));
                    judicialOfficeAuthorisation.setTicketId((Long)authorisationMap.get("ticket_id"));
                    judicialOfficeAuthorisation.setStartDate(handleNull((Timestamp)authorisationMap.get("start_date")));
                    judicialOfficeAuthorisation.setEndDate(handleNull((Timestamp)authorisationMap.get("end_date")));
                    judicialOfficeAuthorisation.setCreatedDate(handleNull((Timestamp)authorisationMap
                            .get("created_date")));
                    judicialOfficeAuthorisation.setLastUpdated(handleNull((Timestamp)authorisationMap
                            .get("last_updated")));
                    judicialOfficeAuthorisation.setLowerLevel((String)authorisationMap.get("lower_level"));
                    return judicialOfficeAuthorisation;
                }).collect(Collectors.toList());

        //size check
        List<JudicialOfficeAuthorisation> expectedAuthorisations = getFileAuthorisationObjectsFromCsv(file[2]);
        assertEquals(judicialAuthorisationList.size(), expectedAuthorisations.size());
        //exact field checks
        Assertions.assertThat(actualAuthorisations).usingFieldByFieldElementComparator()
                .containsAll(expectedAuthorisations);
    }

    static List<JudicialOfficeAuthorisation> getFileAuthorisationObjectsFromCsv(String inputFilePath)  {
        List<JudicialOfficeAuthorisation> authorisations = new LinkedList<>();
        try {
            File file = ResourceUtils.getFile(inputFilePath);
            InputStream inputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            authorisations = bufferedReader.lines().skip(1).map(line -> mapJudicialOfficeAuthorisation(line))
                    .collect(Collectors.toList());
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return authorisations;
    }

    static JudicialOfficeAuthorisation mapJudicialOfficeAuthorisation(String line) {
        List<String> columns = Arrays.asList(line.split("\\,", -1));
        JudicialOfficeAuthorisation judicialOfficeAuthorisation = new JudicialOfficeAuthorisation();
        judicialOfficeAuthorisation.setElinksId(handleNull(columns.get(0), false));
        judicialOfficeAuthorisation.setJurisdiction(handleNull(columns.get(1), false));
        judicialOfficeAuthorisation.setTicketId(isBlank(columns.get(2)) ? null : Long.parseLong(columns.get(2)));
        judicialOfficeAuthorisation.setStartDate(handleNull(columns.get(3), true));
        judicialOfficeAuthorisation.setEndDate(handleNull(columns.get(4), true));
        judicialOfficeAuthorisation.setCreatedDate(handleNull(columns.get(5), true));
        judicialOfficeAuthorisation.setLastUpdated(handleNull(columns.get(6), true));
        judicialOfficeAuthorisation.setLowerLevel(handleNull(columns.get(7), false));
        return judicialOfficeAuthorisation;
    }

    static String handleNull(String fieldValue, boolean timeStampField) {
        if (isBlank(fieldValue) && !timeStampField) {
            return StringUtils.EMPTY;
        } else if (timeStampField && isBlank(fieldValue)) {
            return null;
        } else if (timeStampField && !isBlank(fieldValue)) {
            return Timestamp.valueOf(fieldValue).toString();
        } else {
            return fieldValue;
        }
    }

    static String handleNull(Timestamp timestamp) {
        if (Objects.isNull(timestamp)) {
            return null;
        } else {
            return String.valueOf(timestamp);
        }
    }
}
