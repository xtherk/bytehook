package io.github.xtherk.bytehook;

import io.github.xtherk.bytehook.util.ClassLoaderInternals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xtherk
 */
public class Modules {

    public static volatile boolean initialized = false;
    private static final Path DEFAULT_MODULE_DIR = Paths.get(System.getProperty("user.dir"), "modules");
    /**
     * Scan all resources in the module directory into {@link #moduleResourcePaths}
     */
    private static final List<Path> MODULE_DIRECTORIES = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(Modules.class);
    private static final Map<String, List<Endpoint>> ENDPOINT_MAPPING = new HashMap<>();
    /**
     * The final module resource to process
     */
    private static final List<Path> moduleResourcePaths = new ArrayList<>();

    static {
        MODULE_DIRECTORIES.add(DEFAULT_MODULE_DIR);
    }

    /**
     * Init endpoints
     */
    public static void initialize() {
        if (!initialized) {
            logger.info("default module directory is {}", DEFAULT_MODULE_DIR);
            try {
                scanModules();
                loadModules();
                release();
                initialized = true;
            } catch (IOException e) {
                logger.error("Please check the module directory", e);
            }
        }
    }

    /**
     * All modules in the load module directory
     */
    public static void loadModules() {
        if (moduleResourcePaths.isEmpty()) return;
        for (Path modulePath : moduleResourcePaths) {
            addToClasspath(modulePath);
            load(modulePath);
        }
    }

    /**
     * Load the module of the specified path
     *
     * @param path module path
     */
    public static void load(Path path) {
        for (ModuleLoader loader : Loaders.getLoaders()) {
            if (loader.support(path)) {
                try {
                    List<Endpoint> endpoints = loader.load(path);
                    Map<String, List<Endpoint>> endpointListMap =
                            endpoints.stream().collect(Collectors.groupingBy(Endpoint::getTargetClass));
                    ENDPOINT_MAPPING.putAll(endpointListMap);
                    return;
                } catch (IOException e) {
                    logger.error("Reading module failed", e);
                }
            }
        }
        logger.warn("Unsupported module format with {}", path.toString());
    }

    /**
     * Inserts the given jars into the classpath.
     *
     * @param modulePath module path
     */
    public static void addToClasspath(Path modulePath) {
        try {
            URL url = modulePath.toUri().toURL();
            Object ucp = ClassLoaderInternals.getUcp();
            ClassLoaderInternals.appendToUcpPath(ucp, url);
        } catch (ReflectiveOperationException e) {
            // This should only occur if a JRE has some customizations to the way core classloaders are handled.
            // Or if they update something in a newer version of Java.
            logger.error("Failed to add missing module paths to classpath", e);
        } catch (MalformedURLException e) {
            logger.error("Failed to resolve local dependency jar to URL", e);
        }
    }

    /**
     * Scan all modules in the module directory
     *
     * @throws IOException if an I/O error occurs
     */
    public static void scanModules() throws IOException {
        for (Path modulePath : MODULE_DIRECTORIES) {
            if (!Files.isDirectory(modulePath)) return;
            // Close the directory handle
            try (DirectoryStream<Path> modulePaths = Files.newDirectoryStream(modulePath)) {
                modulePaths.forEach(moduleResourcePaths::add);
            }
        }
    }

    private static void release() {
        moduleResourcePaths.clear();
    }

    public static List<Endpoint> getEndpoints(String className) {
        return ENDPOINT_MAPPING.get(className);
    }

    public static Set<String> endpointKeySets() {
        return ENDPOINT_MAPPING.keySet();
    }

    public static void addModuleDirectory(Path directory) {
        if (Files.isDirectory(directory)) {
            MODULE_DIRECTORIES.add(directory);
        }
    }

    public static void addModuleResource(Path resource) {
        moduleResourcePaths.add(resource);
    }

}
