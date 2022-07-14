package uk.gov.hmcts.reform.juddata.camel.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JrdConstantsTest {

    @Test()
    void test_expected_exception_on_instantiation() throws NoSuchMethodException {
        Constructor<JrdConstants> constructor = JrdConstants.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        //constructor.newInstance();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        InvocationTargetException invocationTargetException =
                assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertSame("java.lang.AssertionError", invocationTargetException.getCause().toString());
    }
}