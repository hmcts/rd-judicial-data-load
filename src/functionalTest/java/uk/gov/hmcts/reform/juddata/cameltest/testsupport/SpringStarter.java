package uk.gov.hmcts.reform.juddata.cameltest.testsupport;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static java.util.Objects.isNull;

public class SpringStarter {

    private static SpringStarter INSTANCE = null;

    private TestContextManager testContextManager;

    public static SpringStarter getInstance() {
        if (isNull(INSTANCE)) {
            INSTANCE = new SpringStarter();
        }
        return INSTANCE;
    }

    public void init(TestContextManager testContextManager) {
        this.testContextManager = testContextManager;
    }

    public void restart() {
        testContextManager.getTestContext().markApplicationContextDirty(DirtiesContext.HierarchyMode.CURRENT_LEVEL);
        reInjectDependencies();
    }

    private void reInjectDependencies() {
        testContextManager
            .getTestExecutionListeners()
            .stream()
            .filter(listener -> listener instanceof DependencyInjectionTestExecutionListener)
            .findFirst()
            .ifPresent(listener -> {
                try {
                    listener.prepareTestInstance(testContextManager.getTestContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }

}
