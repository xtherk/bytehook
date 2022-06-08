package io.github.xtherk.bytehook.loader;

import io.github.xtherk.bytehook.util.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Module loader that is capable of loading modules from ZIP archives.
 *
 * @author xtherk
 */
public final class ZipModuleLoader extends AbstractModuleLoader {

    public static ZipModuleLoader INSTANCE = new ZipModuleLoader();

    private ZipModuleLoader() {

    }

    @Override
    public Map<String, byte[]> parse(Path zipFile) throws IOException {
        InputStream source = Files.newInputStream(zipFile);
        Map<String, byte[]> content = new HashMap<>();
        try (ZipInputStream zis = new ZipInputStream(source)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                // We don't care about directories.
                if (entry.isDirectory()) {
                    continue;
                }
                byte[] bytes = IOUtil.toByteArray(zis);
                String name = entry.getName();
                if (!name.endsWith(".class")) {
                    continue; //  We are only looking for classes.
                }
                String className = name.substring(0, name.length() - 6).replace('/', '.');
                content.put(className, bytes);
            }
        }
        return content;
    }

    @Override
    public boolean support(Path path) {
        return path.toString().endsWith(".jar");
    }
}
