package uk.gov.hmcts.reform.juddata.camel.helper;

import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.DATE_FORMAT;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SCHEDULER_NAME;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.SCHEDULER_START_TIME;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.yaml.snakeyaml.Yaml;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialBaseLocationType;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialContractType;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialRegionType;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserRoleType;
import uk.gov.hmcts.reform.juddata.camel.route.beans.RouteProperties;

@TestPropertySource(properties = {"spring.config.location=classpath:application-integration.yml"})
public class JrdTestSupport {

    @Value("${route.judicial-user-profile-orchestration.route-header}")
    public static String fileHeader;

    public static Map readYmlAsMap = readYmlAsMap("test-header.yaml");


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
        routeProperties.setFileName("Test.csv");
        routeProperties.setRouteHeader((String) ((Map) readYmlAsMap.get("judicialUserProfile")).get("header"));
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
        String tail = datTime.substring(datTime.lastIndexOf(".")).concat("000000");
        return datTime.substring(0, datTime.lastIndexOf(".")) + tail;
    }

    public static Map<String, String> createMockGlobalOptions(Exchange exchange) {
        Map<String, String> globalOptions = new HashMap<>();
        globalOptions.put(SCHEDULER_NAME, "judicial_leaf_scheduler");
        globalOptions.put(SCHEDULER_START_TIME, String.valueOf(new Date().getTime()));
        exchange.getContext().setGlobalOptions(globalOptions);
        return globalOptions;
    }

    public static InputStream getInputStreamOfFile(String fileName) throws FileNotFoundException {
        File initialFile = new File(fileName);
        return new FileInputStream(initialFile);
    }

    public static Map<String, Object> readYmlAsMap(String yamlFile) {
        Yaml yaml = new Yaml();
        InputStream inputStream = JrdTestSupport.class.getClassLoader().getResourceAsStream(yamlFile);
        return yaml.load(inputStream);
    }
}
