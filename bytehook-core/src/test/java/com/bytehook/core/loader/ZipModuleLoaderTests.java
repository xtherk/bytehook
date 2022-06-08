package com.bytehook.core.loader;

import com.bytehook.core.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Map;

/**
 * @author xtherk
 */
public class ZipModuleLoaderTests extends TestUtils {
    private static final Path testJar = jarsDir.resolve("license.jar");

    @Test
    public void testZipModuleLoader() throws Exception {
        ZipModuleLoader loader = ZipModuleLoader.INSTANCE;
        Map<String, byte[]> result = loader.parse(testJar);
        Assertions.assertEquals(1, result.size());
    }
}
