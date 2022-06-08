package com.bytehook.core;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * The module loader.
 * This interface is responsible for loading modules
 * from different sources.
 *
 * @author xtherk
 */
public interface ModuleLoader {

    List<Endpoint> load(Path path) throws IOException;

    boolean support(Path path);

}
