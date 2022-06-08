package io.github.xtherk.bytehook.loader;

import io.github.xtherk.bytehook.core.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

/**
 * @author xtherk
 */
public class DirectoriesModuleLoaderTests extends TestUtils {

    private static final Path testDir = sourcesDir.resolve("license");


    @Test
    public void testDirectoriesModuleLoader() throws IOException {
        DirectoriesModuleLoader loader = DirectoriesModuleLoader.INSTANCE;
        Map<String, byte[]> result = loader.parse(testDir);
        Assertions.assertEquals(1, result.size());
    }
}
