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
                if (((Class) mySuperclass).getCanonicalName().equals(JudicialOfficeAppointment
                    .class.getCanonicalName())) {
                    filteredChildren.removeIf(filterInvalidUserProfAppointment ->
                        ((JudicialOfficeAppointment) filterInvalidUserProfAppointment).getElinksId()
                            .equalsIgnoreCase(invalidRecords.getElinksId()));
                } else {
                    filteredChildren.removeIf(filterInvalidUserProfAppointment ->
                        ((JudicialOfficeAuthorisation) filterInvalidUserProfAppointment).getElinksId()
                            .equalsIgnoreCase(invalidRecords.getElinksId()));
                }
                invalidElinks.add(invalidRecords.getElinksId());
            });

            if (isFalse(isEmpty(invalidElinks))) {
                //Auditing JSR skipped rows of user profile for Appointment/Authorization
                jsrValidatorInitializer.auditJsrExceptions(invalidElinks, ELINKS_ID, exchange);
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
                                          JsrValidatorInitializer<T> jsrValidatorInitializer) {

        Set<T> missingFkRecords =
            filteredJudicialAppointments.stream().filter(predicate).collect(toSet());
        filteredJudicialAppointments.removeAll(missingFkRecords);
        Type mySuperclass = getType();

        if (isFalse(isEmpty(missingFkRecords))) {
            if (((Class) mySuperclass).getCanonicalName().equals(JudicialOfficeAppointment
                .class.getCanonicalName())) {
                //Auditing JSR skipped rows of user profile for Appointment
                jsrValidatorInitializer.auditJsrExceptions(missingFkRecords.stream()
                        .map(s -> ((JudicialOfficeAppointment) s).getElinksId()).collect(toList()),
                    fieldInError, exchange);
            } else {
                jsrValidatorInitializer.auditJsrExceptions(missingFkRecords.stream()
                        .map(s -> ((JudicialOfficeAuthorisation) s).getElinksId()).collect(toList()),
                    fieldInError, exchange);
            }
        }
    }

    private Type getType() {
        ParameterizedType p = (ParameterizedType) getClass().getGenericSuperclass();
        Type mySuperclass = p.getActualTypeArguments()[0];
        return mySuperclass;
    }
}
