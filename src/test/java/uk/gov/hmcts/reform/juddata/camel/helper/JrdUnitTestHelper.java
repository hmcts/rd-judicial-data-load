package uk.gov.hmcts.reform.juddata.camel.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialBaseLocationType;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialContractType;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialRegionType;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserRoleType;
import uk.gov.hmcts.reform.juddata.camel.route.beans.RouteProperties;

public class JrdUnitTestHelper {
    private JrdUnitTestHelper() {

    }

    public static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    public static JudicialUserProfile createJudicialUserProfileMock(LocalDate currentDate) {

        JudicialUserProfile judicialUserProfileMock = new JudicialUserProfile();
        judicialUserProfileMock.setElinksId("elinksid_1");
        judicialUserProfileMock.setPersonalCode("personalCode_1");
        judicialUserProfileMock.setTitle("title");
        judicialUserProfileMock.setKnownAs("knownAs");
        judicialUserProfileMock.setSurName("surname");
        judicialUserProfileMock.setFullName("fullName");
        judicialUserProfileMock.setPostNominals("postNominals");
        judicialUserProfileMock.setContractTypeId("contractTypeId");
        judicialUserProfileMock.setWorkPattern("workpatterns");
        judicialUserProfileMock.setEmailId("some@hmcts.net");
        judicialUserProfileMock.setJoiningDate(currentDate);
        judicialUserProfileMock.setLastWorkingDate(currentDate);
        judicialUserProfileMock.setActiveFlag(true);
        judicialUserProfileMock.setExtractedDate(currentDate.toString());

        return judicialUserProfileMock;
    }

    public static JudicialOfficeAppointment createJudicialOfficeAppointmentMockMock(String currentDateString) {

        LocalDate currentDate = LocalDate.parse(currentDateString, getDateFormatter());
        JudicialOfficeAppointment judicialOfficeAppointmentMock = new JudicialOfficeAppointment();
        judicialOfficeAppointmentMock.setElinksId("elinksid_1");
        judicialOfficeAppointmentMock.setRoleId("roleId_1");
        judicialOfficeAppointmentMock.setContractType("contractTypeId_1");
        judicialOfficeAppointmentMock.setBaseLocationId("baseLocationId_1");
        judicialOfficeAppointmentMock.setRegionId("regionId_1");
        judicialOfficeAppointmentMock.setIsPrincipalAppointment(true);
        judicialOfficeAppointmentMock.setStartDate(currentDate);
        judicialOfficeAppointmentMock.setEndDate(currentDate);
        judicialOfficeAppointmentMock.setActiveFlag(true);
        judicialOfficeAppointmentMock.setExtractedDate(currentDateString);
        return judicialOfficeAppointmentMock;
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

    public static JudicialUserRoleType createJudicialUserRoleType() {
        JudicialUserRoleType judicialUserRoleType = new JudicialUserRoleType();
        judicialUserRoleType.setRoleDescCy("roleDescCy");
        judicialUserRoleType.setRoleDescEn("roleDescEn");
        judicialUserRoleType.setRoleId("roleId");
        return  judicialUserRoleType;
    }

    public static JudicialRegionType createJudicialRegionType() {
        JudicialRegionType judicialRegionType = new JudicialRegionType();

        judicialRegionType.setRegionDescCy("region_desc_cy");
        judicialRegionType.setRegionDescEn("region_desc_en");
        judicialRegionType.setRegionId("regionId");
        return judicialRegionType;
    }

    public static JudicialBaseLocationType createJudicialOfficeAppointmentMock() {
        JudicialBaseLocationType judicialBaseLocationType = new JudicialBaseLocationType();

        judicialBaseLocationType.setArea("area");
        judicialBaseLocationType.setBaseLocationId("baseLocationId");
        judicialBaseLocationType.setCircuit("circuit");
        judicialBaseLocationType.setCourtName("courtName");
        judicialBaseLocationType.setCourtType("courtType");
        return  judicialBaseLocationType;
    }

    public static JudicialContractType createJudicialContractType() {
        JudicialContractType contractType = new JudicialContractType();
        contractType.setContractTypeDescCy("contractTypeDescCy");
        contractType.setContractTypeDescEn("contractTypeDescEn");
        contractType.setContractTypeId("contractTypeId");
        return  contractType;
    }
}
