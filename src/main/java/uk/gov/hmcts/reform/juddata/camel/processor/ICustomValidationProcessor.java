package uk.gov.hmcts.reform.juddata.camel.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
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
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.springframework.util.CollectionUtils.isEmpty;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.INVALID_JSR_PARENT_ROW;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdMappingConstants.ELINKS_ID;


public interface ICustomValidationProcessor<T> {

    @Slf4j
    final class LogHolder {
    }

    default void filterInvalidUserProfileRecords(List<T> filteredChildren,
                                                 List<JudicialUserProfile> invalidJudicialUserProfileRecords,
                                                 JsrValidatorInitializer<T> jsrValidatorInitializer,
                                                 Exchange exchange, String logComponentName) {
        Type mySuperclass = getType();
        List<String> invalidElinks = new ArrayList<>();
        if (nonNull(invalidJudicialUserProfileRecords)) {

            invalidJudicialUserProfileRecords.forEach(invalidRecords -> {
                //Remove invalid appointment for user profile and add to invalidElinks List
                boolean filteredRecord;
                if (((Class) mySuperclass).getCanonicalName().equals(JudicialOfficeAppointment
                    .class.getCanonicalName())) {
                    filteredRecord = filteredChildren.removeIf(filterInvalidUserProfAppointment ->
                        ((JudicialOfficeAppointment) filterInvalidUserProfAppointment).getElinksId()
                            .equalsIgnoreCase(invalidRecords.getElinksId()));
                } else {
                    filteredRecord = filteredChildren.removeIf(filterInvalidUserProfAppointment ->
                        ((JudicialOfficeAuthorisation) filterInvalidUserProfAppointment).getElinksId()
                            .equalsIgnoreCase(invalidRecords.getElinksId()));
                }
                if (filteredRecord) {
                    invalidElinks.add(invalidRecords.getElinksId());
                }
            });

            if (isFalse(isEmpty(invalidElinks))) {
                //Auditing JSR skipped rows of user profile for Appointment/Authorization
                jsrValidatorInitializer.auditJsrExceptions(invalidElinks, ELINKS_ID, INVALID_JSR_PARENT_ROW, exchange);
                LogHolder.log.info("{}:: Skipped invalid user profile elinks in {} {} & total skipped count {}",
                    logComponentName,
                    mySuperclass.getTypeName(),
                    invalidElinks.stream().collect(joining(",")),
                    invalidElinks.size());
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

        if (isFalse(isEmpty(missingForeignKeyRecords))) {
            if (((Class) mySuperclass).getCanonicalName().equals(JudicialOfficeAppointment
                .class.getCanonicalName())) {
                //Auditing JSR skipped rows of user profile for Appointment
                jsrValidatorInitializer.auditJsrExceptions(missingForeignKeyRecords.stream()
                        .map(s -> ((JudicialOfficeAppointment) s).getElinksId()).collect(toList()),
                    fieldInError, errorMessage, exchange);
            } else {
                jsrValidatorInitializer.auditJsrExceptions(missingForeignKeyRecords.stream()
                        .map(s -> ((JudicialOfficeAuthorisation) s).getElinksId()).collect(toList()),
                    fieldInError, errorMessage, exchange);
            }
        }
    }

    private Type getType() {
        ParameterizedType p = (ParameterizedType) getClass().getGenericSuperclass();
        return p.getActualTypeArguments()[0];
    }
}
