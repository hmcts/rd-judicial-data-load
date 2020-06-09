package uk.gov.hmcts.reform.juddata.cameltest.testsupport;

import org.apache.camel.test.spring.CamelSpringRunner;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.TestContextManager;

public class RestartingSpringJUnit4ClassRunner extends CamelSpringRunner {

    public RestartingSpringJUnit4ClassRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    protected TestContextManager testContextManager = getTestContextManager();


    @Override
    protected Object createTest() throws Exception {
        final Object testInstance = super.createTest();

        SpringRestarter.getInstance().init(getTestContextManager());

        return testInstance;
    }
}
