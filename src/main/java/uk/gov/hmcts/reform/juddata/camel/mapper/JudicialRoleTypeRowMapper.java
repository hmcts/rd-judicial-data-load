package uk.gov.hmcts.reform.juddata.camel.mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.data.ingestion.camel.mapper.IMapper;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserRoleType;
import uk.gov.hmcts.reform.juddata.camel.util.CommonUtils;

import static uk.gov.hmcts.reform.juddata.camel.util.CommonUtils.getDateTimeStamp;

@Component
public class JudicialRoleTypeRowMapper implements IMapper {

    public Map<String, Object> getMap(Object roleObject) {

        JudicialUserRoleType role = (JudicialUserRoleType) roleObject;

        Map<String, Object> roleRow = new HashMap<>();
        roleRow.put("per_Id", role.getPerId());
        roleRow.put("title", role.getTitle());
        roleRow.put("location", role.getLocation());
        roleRow.put("start_date", getDateTimeStamp(role.getStartDate()));
        roleRow.put("end_date", getDateTimeStamp(role.getEndDate()));


        Optional<String> mrdCreatedTimeOptional =
                Optional.ofNullable(role.getMrdCreatedTime()).filter(Predicate.not(String::isEmpty));
        roleRow.put("mrd_created_time", mrdCreatedTimeOptional.map(CommonUtils::getDateTimeStamp)
                .orElse(null));

        Optional<String> mrdUpdatedTimeOptional =
                Optional.ofNullable(role.getMrdUpdatedTime()).filter(Predicate.not(String::isEmpty));
        roleRow.put("mrd_updated_time", mrdUpdatedTimeOptional.map(CommonUtils::getDateTimeStamp)
                .orElse(null));

        Optional<String> mrdDeletedTimeOptional =
                Optional.ofNullable(role.getMrdDeletedTime()).filter(Predicate.not(String::isEmpty));
        roleRow.put("mrd_deleted_time", mrdDeletedTimeOptional.map(CommonUtils::getDateTimeStamp)
                .orElse(null));
        return  roleRow;
    }
}

