package uk.gov.hmcts.reform.juddata.camel.helper;

import com.google.common.collect.ImmutableMap;
import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.RouteProperties;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialBaseLocationType;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAuthorisation;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialRegionType;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserRoleType;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.DATE_FORMAT;

public class JrdTestSupport {

    public static final String PERID_1 = "perid_1";
    public static final String PERID_2 = "perid_2";
    public static final String PERID_3 = "perid_3";
    public static final String PERID_4 = "perid_4";

    public static final Map<String, String> baseLocations = ImmutableMap.of("perid_1", "baseLocationId_1",
        "perid_2", "baseLocationId_2", "perid_3", "baseLocationId_3");

    public static final Map<String, String> regions = ImmutableMap.of("perid_1", "regionId_1",
        "perid_2", "regionId_2", "perid_3", "regionId_3");

    public static final Map<String, String> roles = ImmutableMap.of("elinksid_1", "roleId_1",
            "elinksid_2", "roleId_2", "elinksid_3", "roleId_3");


    public static final String PERSONAL_CODE = "111";

    private JrdTestSupport() {

    }

    public static JudicialUserProfile createJudicialUserProfileMock(Date currentDate, LocalDateTime dateTime,
                                                                    String perId) {

        JudicialUserProfile judicialUserProfileMock = new JudicialUserProfile();
        judicialUserProfileMock.setPerId(perId);
        judicialUserProfileMock.setPersonalCode("personalCode_1");
        judicialUserProfileMock.setKnownAs("knownAs");
        judicialUserProfileMock.setSurName("surname");
        judicialUserProfileMock.setFullName("fullName");
        judicialUserProfileMock.setPostNominals("postNominals");
        judicialUserProfileMock.setWorkPattern("workpatterns");
        judicialUserProfileMock.setEjudiciaryEmail("some@hmcts.net");
        judicialUserProfileMock.setJoiningDate(getDateWithFormat(currentDate, DATE_FORMAT));
        judicialUserProfileMock.setLastWorkingDate(getDateWithFormat(currentDate, DATE_FORMAT));
        judicialUserProfileMock.setActiveFlag(true);
        judicialUserProfileMock.setExtractedDate(getDateTimeWithFormat(dateTime));
        judicialUserProfileMock.setObjectId("779321b3-3170-44a0-bc7d-b4decc2aea10");
        judicialUserProfileMock.setIsJudge("True");
        judicialUserProfileMock.setIsPanelMember("True");
        judicialUserProfileMock.setIsMagistrate("False");
        judicialUserProfileMock.setMrdCreatedTime("2008-07-18 00:00:00");
        judicialUserProfileMock.setMrdUpdatedTime("2008-07-19 00:00:00");
        judicialUserProfileMock.setMrdDeletedTime("2008-07-20 00:00:00");
        return judicialUserProfileMock;
    }

    public static JudicialBaseLocationType createJudicialOfficeAppointmentMock() {
        JudicialBaseLocationType judicialBaseLocationType = new JudicialBaseLocationType();

        judicialBaseLocationType.setArea("area");
        judicialBaseLocationType.setBaseLocationId("baseLocationId");
        judicialBaseLocationType.setCircuit("circuit");
        judicialBaseLocationType.setCourtName("courtName");
        judicialBaseLocationType.setCourtType("courtType");
        return judicialBaseLocationType;
    }

    public static JudicialOfficeAppointment createJudicialOfficeAppointmentMock(Date currentDate,
                                                                                LocalDateTime dateTime,
                                                                                String perId) {

        JudicialOfficeAppointment judicialOfficeAppointmentMock = new JudicialOfficeAppointment();
        judicialOfficeAppointmentMock.setPerId(perId);
        judicialOfficeAppointmentMock.setBaseLocationId(baseLocations.get(perId));
        judicialOfficeAppointmentMock.setRegionId(regions.get(perId));
        judicialOfficeAppointmentMock.setIsPrincipalAppointment(true);
        judicialOfficeAppointmentMock.setStartDate(getDateWithFormat(currentDate, DATE_FORMAT));
        judicialOfficeAppointmentMock.setEndDate(getDateWithFormat(currentDate, DATE_FORMAT));
        judicialOfficeAppointmentMock.setActiveFlag(true);
        judicialOfficeAppointmentMock.setPersonalCode(PERSONAL_CODE);
        judicialOfficeAppointmentMock.setExtractedDate(getDateTimeWithFormat(dateTime));
        judicialOfficeAppointmentMock.setObjectId("779321b3-3170-44a0-bc7d-b4decc2aea10");
        judicialOfficeAppointmentMock.setAppointment("Magistrate");
        judicialOfficeAppointmentMock.setAppointmentType("1");

        return judicialOfficeAppointmentMock;
    }

    public static JudicialOfficeAuthorisation createJudicialOfficeAuthorisation(String date) {

        JudicialOfficeAuthorisation judicialOfficeAuthorisation = new JudicialOfficeAuthorisation();
        judicialOfficeAuthorisation.setPerId("1");
        judicialOfficeAuthorisation.setJurisdiction("jurisdiction");
        judicialOfficeAuthorisation.setStartDate(date);
        judicialOfficeAuthorisation.setEndDate(date);
        judicialOfficeAuthorisation.setLowerLevel("lowerLevel");
        judicialOfficeAuthorisation.setPersonalCode(PERSONAL_CODE);
        judicialOfficeAuthorisation.setTicketId(12345L);
        judicialOfficeAuthorisation.setObjectId("779321b3-3170-44a0-bc7d-b4decc2aea10");
        return judicialOfficeAuthorisation;
    }


    public static RouteProperties createRoutePropertiesMock() {

        RouteProperties routeProperties = new RouteProperties();
        routeProperties.setBinder("Binder");
        routeProperties.setBlobPath("Blobpath");
        routeProperties.setChildNames("childNames");
        routeProperties.setMapper("mapper");
        routeProperties.setProcessor("processor");
        routeProperties.setRouteName("routeName");
        routeProperties.setSql("sql");
        routeProperties.setTruncateSql("truncateSql");
        return routeProperties;
    }

    public static String createCurrentLocalDate() {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = getDateFormatter();
        return date.format(formatter);
    }

    public static DateTimeFormatter getDateFormatter() {
        return DateTimeFormatter.ofPattern(DATE_FORMAT);
    }

    public static JudicialRegionType createJudicialRegionType() {
        JudicialRegionType judicialRegionType = new JudicialRegionType();

        judicialRegionType.setRegionDescCy("region_desc_cy");
        judicialRegionType.setRegionDescEn("region_desc_en");
        judicialRegionType.setRegionId("regionId");
        return judicialRegionType;
    }

    public static String getDateWithFormat(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static String getDateTimeWithFormat(LocalDateTime dateTime) {
        String datTime = dateTime.toString().replace("T", " ");
        String tail = datTime.substring(datTime.lastIndexOf(".")).concat("000");
        return datTime.substring(0, datTime.lastIndexOf(".")) + tail;
    }

    public static JudicialUserRoleType createJudicialUserRoleType() {
        JudicialUserRoleType judicialUserRoleType = new JudicialUserRoleType();
        judicialUserRoleType.setPerId("46804");
        judicialUserRoleType.setTitle("Family Course Tutor (JC)");
        judicialUserRoleType.setLocation("Nationwide");
        judicialUserRoleType.setStartDate("2018-05-02 00:00:00.000");
        judicialUserRoleType.setEndDate("2022-05-01 00:00:00");
        return judicialUserRoleType;
    }
}
