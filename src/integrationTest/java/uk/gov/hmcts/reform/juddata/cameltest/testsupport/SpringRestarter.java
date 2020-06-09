package uk.gov.hmcts.reform.juddata.cameltest.testsupport;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

public class SpringRestarter {

    private static SpringRestarter INSTANCE = null;

    private TestContextManager testContextManager;

    public static SpringRestarter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SpringRestarter();
        }

        return INSTANCE;
    }

    public void init(TestContextManager testContextManager) {
        this.testContextManager = testContextManager;
    }

    public void restart() {
        testContextManager.getTestContext().markApplicationContextDirty(DirtiesContext.HierarchyMode.EXHAUSTIVE);
        testContextManager.getTestContext().getApplicationContext();
        reInjectDependencies();
    }

    private void reInjectDependencies()  {
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
