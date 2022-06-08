package io.github.xtherk.bytehook;

import io.github.xtherk.bytehook.loader.DirectoriesModuleLoader;
import io.github.xtherk.bytehook.loader.ZipModuleLoader;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Module loaders
 *
 * @author xtherk
 */
public class Loaders {
    private static final List<ModuleLoader> LOADERS = new ArrayList<>();

    static {
        registry(ZipModuleLoader.INSTANCE);
        registry(DirectoriesModuleLoader.INSTANCE);
    }

    public static void registry(ModuleLoader loader) {
        LOADERS.add(loader);
    }

    public static List<ModuleLoader> getLoaders() {
        return Collections.unmodifiableList(LOADERS);
    }

    public static Optional<ModuleLoader> getLoader(Path path) {
        for (ModuleLoader loader : LOADERS) {
            if (loader.support(path))
                return Optional.of(loader);
        }
        return Optional.empty();
    }
}
