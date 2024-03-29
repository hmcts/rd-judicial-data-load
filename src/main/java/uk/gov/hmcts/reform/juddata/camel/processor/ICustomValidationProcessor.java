package uk.gov.hmcts.reform.juddata.camel.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.ApplicationContext;
import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.FileStatus;
import uk.gov.hmcts.reform.data.ingestion.camel.route.beans.RouteProperties;
import uk.gov.hmcts.reform.data.ingestion.camel.validator.JsrValidatorInitializer;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAppointment;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialOfficeAuthorisation;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserProfile;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserRoleType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import static org.apache.camel.util.ObjectHelper.isNotEmpty;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.DataLoadUtil.getFileDetails;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.DataLoadUtil.registerFileStatusBean;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.ROUTE_DETAILS;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.DATE_PATTERN;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.INVALID_JSR_PARENT_ROW;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.BASE_LOCATION_ID;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.LOCATION_ID;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.PER_ID;

public interface ICustomValidationProcessor<T> {

    @Slf4j
    final class LogHolder {
    }

    default void filterInvalidUserProfileRecords(List<T> filteredChildren,
                                                 List<JudicialUserProfile> invalidJudicialUserProfileRecords,
                                                 JsrValidatorInitializer<T> jsrValidatorInitializer,
                                                 Exchange exchange, String logComponentName) {
        Type mySuperclass = getType();
        List<Pair<String, Long>> invalidPerIds = new ArrayList<>();
        if (nonNull(invalidJudicialUserProfileRecords)) {

            invalidJudicialUserProfileRecords.forEach(invalidRecords -> {
                if (((Class) mySuperclass).getCanonicalName().equals(JudicialOfficeAppointment
                    .class.getCanonicalName())) {
                    filteredChildren.stream()
                            .map(JudicialOfficeAppointment.class::cast)
                            .filter(app -> app.getPerId().equalsIgnoreCase(invalidRecords.getPerId()))
                            .forEach(filteredApp ->
                                    invalidPerIds.add(Pair.of(filteredApp.getPerId(), filteredApp.getRowId())));

                    filteredChildren.removeIf(filterInvalidUserProfAppointment ->
                        ((JudicialOfficeAppointment) filterInvalidUserProfAppointment).getPerId()
                            .equalsIgnoreCase(invalidRecords.getPerId()));
                } else if (((Class) mySuperclass).getCanonicalName().equals(JudicialOfficeAuthorisation
                    .class.getCanonicalName())) {
                    filteredChildren.stream()
                            .map(JudicialOfficeAuthorisation.class::cast)
                            .filter(auth -> auth.getPerId().equalsIgnoreCase(invalidRecords.getPerId()))
                            .forEach(filteredAuth ->
                                    invalidPerIds.add(Pair.of(filteredAuth.getPerId(), filteredAuth.getRowId())));

                    filteredChildren.removeIf(filterInvalidUserProfAuthorization ->
                            ((JudicialOfficeAuthorisation) filterInvalidUserProfAuthorization).getPerId()
                                    .equalsIgnoreCase(invalidRecords.getPerId()));
                } else if (((Class) mySuperclass).getCanonicalName().equals(JudicialUserRoleType
                        .class.getCanonicalName())) {

                    filteredChildren.stream()
                            .map(JudicialUserRoleType.class::cast)
                            .filter(roleType -> roleType.getPerId().equalsIgnoreCase(invalidRecords.getPerId()))
                            .forEach(filteredRoleType ->
                                    invalidPerIds.add(Pair.of(filteredRoleType.getPerId(),
                                            filteredRoleType.getRowId())));

                    filteredChildren.removeIf(filterInvalidUserProfRoleType ->
                            ((JudicialUserRoleType) filterInvalidUserProfRoleType).getPerId()
                                    .equalsIgnoreCase(invalidRecords.getPerId()));
                }
            });

            if (isNotEmpty(invalidPerIds)) {
                //Auditing JSR skipped rows of user profile for Appointment/Authorization/JudicialUserRoleType
                jsrValidatorInitializer.auditJsrExceptions(invalidPerIds, PER_ID, INVALID_JSR_PARENT_ROW, exchange);
                LogHolder.log.info("{}:: Skipped invalid user profile per in {} {} & total skipped count {}",
                    logComponentName,
                    mySuperclass.getTypeName(),
                    invalidPerIds
                            .stream()
                            .map(Pair::getLeft)
                            .collect(joining(",")),
                    invalidPerIds.size());
            }
        }
    }

    default void removeForeignKeyElements(List<T> filteredJudicialAppointments,
                                          Predicate<T> predicate, String fieldInError, Exchange exchange,
                                          JsrValidatorInitializer<T> jsrValidatorInitializer, String errorMessage) {

        Set<T> missingForeignKeyRecords =
            filteredJudicialAppointments.stream().filter(predicate).collect(toSet());
        filteredJudicialAppointments.removeAll(missingForeignKeyRecords);
        Type mySuperclass = getType();

        if (isNotEmpty(missingForeignKeyRecords)) {
            if (((Class) mySuperclass).getCanonicalName().equals(JudicialOfficeAppointment
                .class.getCanonicalName())) {
                List<Pair<String, Long>> pair = new ArrayList<>();
                missingForeignKeyRecords.stream()
                        .map(JudicialOfficeAppointment.class::cast)
                        .forEach(j -> pair.add(Pair.of(j.getPerId(), j.getRowId())));
                //Auditing foreign key skipped rows of user profile for Appointment
                jsrValidatorInitializer.auditJsrExceptions(pair,
                    fieldInError, errorMessage, exchange);
                if (List.of(LOCATION_ID, BASE_LOCATION_ID).contains(fieldInError)) {
                    sendEmail(missingForeignKeyRecords, fieldInError,
                            LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_PATTERN)));
                }
            } else if (((Class) mySuperclass).getCanonicalName().equals(JudicialOfficeAuthorisation
                .class.getCanonicalName())) {
                List<Pair<String, Long>> pair = new ArrayList<>();
                missingForeignKeyRecords.stream()
                        .map(JudicialOfficeAuthorisation.class::cast)
                        .forEach(j -> pair.add(Pair.of(j.getPerId(), j.getRowId())));
                //Auditing foreign key skipped rows of user profile for Authorization
                jsrValidatorInitializer.auditJsrExceptions(pair,
                    fieldInError, errorMessage, exchange);
            } else if (((Class) mySuperclass).getCanonicalName().equals(JudicialUserRoleType
                    .class.getCanonicalName())) {
                List<Pair<String, Long>> pair = new ArrayList<>();
                missingForeignKeyRecords.stream()
                        .map(JudicialUserRoleType.class::cast)
                        .forEach(j -> pair.add(Pair.of(j.getPerId(), j.getRowId())));
                //Auditing foreign key skipped rows of user profile for Authorization
                jsrValidatorInitializer.auditJsrExceptions(pair,
                        fieldInError, errorMessage, exchange);
            }
        }
    }

    /**
     * Has to be overridden when it needs.
     * @param data data to prepare email body
     * @param params data to build subject or any other dynamic data
     * @return positive when email send success
     */
    default int sendEmail(Set<T> data, String type, Object... params) {
        return -1;
    }

    private Type getType() {
        ParameterizedType p = (ParameterizedType) getClass().getGenericSuperclass();
        return p.getActualTypeArguments()[0];
    }

    default void setFileStatus(Exchange exchange, ApplicationContext applicationContext, String auditStatus) {
        RouteProperties routeProperties = (RouteProperties) exchange.getIn().getHeader(ROUTE_DETAILS);
        FileStatus fileStatus = getFileDetails(exchange.getContext(), routeProperties.getFileName());
        fileStatus.setAuditStatus(auditStatus);
        registerFileStatusBean(applicationContext, routeProperties.getFileName(), fileStatus,
            exchange.getContext());
    }
}
