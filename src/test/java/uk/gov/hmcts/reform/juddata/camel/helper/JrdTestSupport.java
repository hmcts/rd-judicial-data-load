package uk.gov.hmcts.reform.juddata.camel.helper;

import com.google.common.collect.ImmutableMap;
import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.RouteProperties;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialBaseLocationType;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialContractType;
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

    public static final String ELINKSID_1 = "elinksid_1";
    public static final String ELINKSID_2 = "elinksid_2";
    public static final String ELINKSID_3 = "elinksid_3";
    public static final String ELINKSID_4 = "elinksid_4";

    public static final Map<String, String> roles = ImmutableMap.of("elinksid_1", "roleId_1",
        "elinksid_2", "roleId_2", "elinksid_3", "roleId_3");

    public static final Map<String, String> contracts = ImmutableMap.of("elinksid_1", "contractTypeId_1",
        "elinksid_2", "contractTypeId_2", "elinksid_3", "contractTypeId_3");

    public static final Map<String, String> baseLocations = ImmutableMap.of("elinksid_1", "baseLocationId_1",
        "elinksid_2", "baseLocationId_2", "elinksid_3", "baseLocationId_3");

    public static final Map<String, String> regions = ImmutableMap.of("elinksid_1", "regionId_1",
        "elinksid_2", "regionId_2", "elinksid_3", "regionId_3");


    private JrdTestSupport() {

    }

    public static JudicialUserProfile createJudicialUserProfileMock(Date currentDate, LocalDateTime dateTime,
                                                                    String elinksId) {

        JudicialUserProfile judicialUserProfileMock = new JudicialUserProfile();
        judicialUserProfileMock.setElinksId(elinksId);
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
                                                                                String elinksId) {

        JudicialOfficeAppointment judicialOfficeAppointmentMock = new JudicialOfficeAppointment();
        judicialOfficeAppointmentMock.setElinksId(elinksId);
        judicialOfficeAppointmentMock.setRoleId(roles.get(elinksId));
        judicialOfficeAppointmentMock.setContractType(contracts.get(elinksId));
        judicialOfficeAppointmentMock.setBaseLocationId(baseLocations.get(elinksId));
        judicialOfficeAppointmentMock.setRegionId(regions.get(elinksId));
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
