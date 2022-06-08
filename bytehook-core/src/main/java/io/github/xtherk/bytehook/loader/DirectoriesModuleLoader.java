package io.github.xtherk.bytehook.loader;

import io.github.xtherk.bytehook.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Module loader that is capable of loading modules from Directories.
 *
 * @author xtherk
 */
public class DirectoriesModuleLoader extends AbstractModuleLoader {

    public static DirectoriesModuleLoader INSTANCE = new DirectoriesModuleLoader();

    private static final String[] ext = {"class"};

    private DirectoriesModuleLoader() {
    }

    @Override
    public Map<String, byte[]> parse(Path basedir) throws IOException {
        Map<String, byte[]> content = new HashMap<>();
        Collection<File> classFiles = FileUtil.listFiles(basedir.toFile(), ext, true);
        for (File classfile : classFiles) {
            content.put(toClassName(basedir, classfile.toPath()), readAllBytes(classfile));
        }
        return content;
    }

    private static String toClassName(Path basedir, Path classpath) {
        Path path = classpath.subpath(basedir.getNameCount(), classpath.getNameCount());
        String name = path.toString();
        return name.substring(0, name.length() - 6).replace(File.separatorChar, '.');
    }

    private static byte[] readAllBytes(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    @Override
    public boolean support(Path path) {
        return Files.isDirectory(path);
    }
}
