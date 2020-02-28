package uk.gov.hmcts.reform.juddata.camel.processor;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.JudicialUserRoleType;

@Slf4j
@Component
public class JudicialUserRoleTypeProcessor implements Processor {

    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) {

        List<JudicialUserRoleType> userRoles = new ArrayList<>();
        List<JudicialUserRoleType> judicialUserRoleTypes;

        if (exchange.getIn().getBody() instanceof List) {
            judicialUserRoleTypes = (List<JudicialUserRoleType>) exchange.getIn().getBody();
        } else {
            JudicialUserRoleType judicialUserRoleType = (JudicialUserRoleType) exchange.getIn().getBody();
            judicialUserRoleTypes = new ArrayList<>();
            judicialUserRoleTypes.add(judicialUserRoleType);
        }

        log.info("JudicialUserRole Records count before validation::" + judicialUserRoleTypes.size());

        for (JudicialUserRoleType user : judicialUserRoleTypes) {

            JudicialUserRoleType validUserRole = fetch(user);
            if (null != validUserRole) {

                userRoles.add(validUserRole);
            } else {

                log.info("Invalid JudicialUserRole record ");
            }

            exchange.getIn().setBody(userRoles);

        }

        log.info("JudicialUserRole Records count After Validation::" + userRoles.size());
    }


    private JudicialUserRoleType fetch(JudicialUserRoleType userRole) {

        JudicialUserRoleType roleAfterValidation = null;
        if (null != userRole.getRoleId()) {
            roleAfterValidation = userRole;
        }
        return roleAfterValidation;
    }
}
