package uk.gov.hmcts.reform.juddata.camel.helper;

import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.DATE_FORMAT;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.RouteProperties;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialBaseLocationType;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialContractType;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAuthorisation;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialRegionType;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserRoleType;

public class JrdTestSupport {

    private JrdTestSupport() {

    }

    public static JudicialUserProfile createJudicialUserProfileMock(Date currentDate, LocalDateTime dateTime) {

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
        judicialUserProfileMock.setJoiningDate(getDateWithFormat(currentDate, DATE_FORMAT));
        judicialUserProfileMock.setLastWorkingDate(getDateWithFormat(currentDate, DATE_FORMAT));
        judicialUserProfileMock.setActiveFlag(true);
        judicialUserProfileMock.setExtractedDate(getDateTimeWithFormat(dateTime));
        judicialUserProfileMock.setObjectId("779321b3-3170-44a0-bc7d-b4decc2aea10");
        return judicialUserProfileMock;
    }

    public static JudicialOfficeAppointment createJudicialOfficeAppointmentMockMock(Date currentDate,
                                                                                    LocalDateTime dateTime) {

        JudicialOfficeAppointment judicialOfficeAppointmentMock = new JudicialOfficeAppointment();
        judicialOfficeAppointmentMock.setElinksId("elinksid_1");
        judicialOfficeAppointmentMock.setRoleId("roleId_1");
        judicialOfficeAppointmentMock.setContractType("contractTypeId_1");
        judicialOfficeAppointmentMock.setBaseLocationId("baseLocationId_1");
        judicialOfficeAppointmentMock.setRegionId("regionId_1");
        judicialOfficeAppointmentMock.setIsPrincipalAppointment(true);
        judicialOfficeAppointmentMock.setStartDate(getDateWithFormat(currentDate, DATE_FORMAT));
        judicialOfficeAppointmentMock.setEndDate(getDateWithFormat(currentDate, DATE_FORMAT));
        judicialOfficeAppointmentMock.setActiveFlag(true);
        judicialOfficeAppointmentMock.setExtractedDate(getDateTimeWithFormat(dateTime));
        return judicialOfficeAppointmentMock;
    }

    public static JudicialOfficeAuthorisation createJudicialOfficeAuthorisation(String date) {

        JudicialOfficeAuthorisation judicialOfficeAuthorisation = new JudicialOfficeAuthorisation();
        judicialOfficeAuthorisation.setElinksId("1");
        judicialOfficeAuthorisation.setJurisdiction("jurisdiction");
        judicialOfficeAuthorisation.setStartDate(date);
        judicialOfficeAuthorisation.setEndDate(date);
        judicialOfficeAuthorisation.setCreatedDate(date);
        judicialOfficeAuthorisation.setLastUpdated(date);
        judicialOfficeAuthorisation.setLowerLevel("lowerLevel");
        judicialOfficeAuthorisation.setTicketId(12345L);
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

    public static JudicialUserRoleType createJudicialUserRoleType() {
        JudicialUserRoleType judicialUserRoleType = new JudicialUserRoleType();
        judicialUserRoleType.setRoleDescCy("roleDescCy");
        judicialUserRoleType.setRoleDescEn("roleDescEn");
        judicialUserRoleType.setRoleId("roleId");
        return judicialUserRoleType;
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
        return judicialBaseLocationType;
    }

    public static JudicialContractType createJudicialContractType() {
        JudicialContractType contractType = new JudicialContractType();
        contractType.setContractTypeDescCy("contractTypeDescCy");
        contractType.setContractTypeDescEn("contractTypeDescEn");
        contractType.setContractTypeId("contractTypeId");
        return contractType;
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
}
