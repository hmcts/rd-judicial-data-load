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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.PARTIAL_SUCCESS;
import static uk.gov.hmcts.reform.data.ingestion.camel.util.MappingConstants.ROUTE_DETAILS;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.INVALID_JSR_PARENT_ROW;
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
                //Remove invalid appointment for user profile and add to invalidPerIds List
                boolean filteredRecord = false;
                if (((Class) mySuperclass).getCanonicalName().equals(JudicialOfficeAppointment
                    .class.getCanonicalName())) {
                    filteredRecord = filteredChildren.removeIf(filterInvalidUserProfAppointment ->
                        ((JudicialOfficeAppointment) filterInvalidUserProfAppointment).getPerId()
                            .equalsIgnoreCase(invalidRecords.getPerId()));
                } else if (((Class) mySuperclass).getCanonicalName().equals(JudicialOfficeAuthorisation
                    .class.getCanonicalName())) {
                    filteredRecord = filteredChildren.removeIf(filterInvalidUserProfAuthorization ->
                        ((JudicialOfficeAuthorisation) filterInvalidUserProfAuthorization).getPerId()
                            .equalsIgnoreCase(invalidRecords.getPerId()));
                }
                if (filteredRecord) {
                    invalidPerIds.add(Pair.of(invalidRecords.getPerId(), invalidRecords.getRowId()));
                }
            });

            if (isNotEmpty(invalidPerIds)) {
                //Auditing JSR skipped rows of user profile for Appointment/Authorization
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
                        .map(i -> ((JudicialOfficeAppointment) i))
                        .forEach(j -> pair.add(Pair.of(j.getPerId(), j.getRowId())));
                //Auditing foreign key skipped rows of user profile for Appointment
                jsrValidatorInitializer.auditJsrExceptions(pair,
                    fieldInError, errorMessage, exchange);
            } else if (((Class) mySuperclass).getCanonicalName().equals(JudicialOfficeAuthorisation
                .class.getCanonicalName())) {
                List<Pair<String, Long>> pair = new ArrayList<>();
                missingForeignKeyRecords.stream()
                        .map(i -> ((JudicialOfficeAuthorisation) i))
                        .forEach(j -> pair.add(Pair.of(j.getPerId(), j.getRowId())));
                //Auditing foreign key skipped rows of user profile for Authorization
                jsrValidatorInitializer.auditJsrExceptions(pair,
                    fieldInError, errorMessage, exchange);
            }
        }
    }

    private Type getType() {
        ParameterizedType p = (ParameterizedType) getClass().getGenericSuperclass();
        return p.getActualTypeArguments()[0];
    }

    default void setFileStatus(Exchange exchange, ApplicationContext applicationContext) {
        RouteProperties routeProperties = (RouteProperties) exchange.getIn().getHeader(ROUTE_DETAILS);
        FileStatus fileStatus = getFileDetails(exchange.getContext(), routeProperties.getFileName());
        fileStatus.setAuditStatus(PARTIAL_SUCCESS);
        registerFileStatusBean(applicationContext, routeProperties.getFileName(), fileStatus,
            exchange.getContext());
    }
}
