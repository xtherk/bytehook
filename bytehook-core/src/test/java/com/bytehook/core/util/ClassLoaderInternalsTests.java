package com.bytehook.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author xtherk
 */
public class ClassLoaderInternalsTests {

    @Test
    public void testGetUcp() throws ReflectiveOperationException {
        Assertions.assertNotNull(ClassLoaderInternals.getUcp());
    }
}
