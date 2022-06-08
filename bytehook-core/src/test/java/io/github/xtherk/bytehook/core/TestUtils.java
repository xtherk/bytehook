package io.github.xtherk.bytehook.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Common test utilities.
 *
 * @author Matt Coley
 */
public class TestUtils {
	protected static final Path resourcesDir = getResourcesPath();
	protected static final Path sourcesDir = getResourcesPath().resolve("content-sources");
	protected static final Path jarsDir = getResourcesPath().resolve("jars");

	private static Path getResourcesPath() {
		Path currentDir = Paths.get(System.getProperty("user.dir"));
		if (Files.exists(currentDir.resolve("src"))) {
			return Paths.get("src", "test", "resources");
		} else {
			return Paths.get("recaf-core", "src", "test", "resources");
		}
	}
}
