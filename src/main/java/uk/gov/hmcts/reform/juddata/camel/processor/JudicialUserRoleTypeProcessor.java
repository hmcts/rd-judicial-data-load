package uk.gov.hmcts.reform.juddata.camel.processor;

import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.binder.JudicialUserRoleType;

@Component
@Slf4j
public class JudicialUserRoleTypeProcessor implements Processor {

    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) {

        List<JudicialUserRoleType> userRoles = new ArrayList<>();
        List<JudicialUserRoleType> judicialUserRoleTypes = (List<JudicialUserRoleType>) exchange.getIn().getBody();

        for (JudicialUserRoleType user : judicialUserRoleTypes) {

            JudicialUserRoleType validUserRole = fetch(user);
            if (nonNull(validUserRole)) {

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
        if (nonNull(userRole.getRoleId())) {
            roleAfterValidation = userRole;
        }
        return roleAfterValidation;
    }
}
