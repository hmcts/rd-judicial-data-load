package uk.gov.hmcts.reform.elinks.util;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import uk.gov.hmcts.reform.elinks.service.FeatureToggleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

@Component
@AllArgsConstructor
@SuppressWarnings("all")
public class FeatureConditionEvaluation implements HandlerInterceptor {

    public static final String SERVICE_AUTHORIZATION = "ServiceAuthorization";

    public static final String BEARER = "Bearer ";

    public static final String FORBIDDEN_EXCEPTION_LD = "feature flag is not released";

    @Autowired
    private final FeatureToggleService featureToggleService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {

//        Map<String, String> launchDarklyUrlMap = featureToggleService.getLaunchDarklyMap();
//
//        String restMethod = ((HandlerMethod) handler).getMethod().getName();
//        String clazz = ((HandlerMethod) handler).getMethod().getDeclaringClass().getSimpleName();
//
//        String flagName = launchDarklyUrlMap.get(clazz + "." + restMethod);
//
//        if (isNotTrue(launchDarklyUrlMap.isEmpty()) && nonNull(flagName)) {
//
//            boolean flagStatus = featureToggleService
//                    .isFlagEnabled(launchDarklyUrlMap.get(clazz + "." + restMethod));
//
//            if (!flagStatus) {
//                throw new ForbiddenException(flagName.concat(SPACE).concat(FORBIDDEN_EXCEPTION_LD));
//            }
//        }
        return true;
    }

}
