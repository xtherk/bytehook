package io.github.xtherk.bytehook.util;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xtherk
 */
public class FileUtil {

    public static List<File> listFiles(File basedir) {
        return listFiles(basedir, null, false);
    }

    public static List<File> listFiles(File basedir, String[] extensions, boolean recursive) {
        File[] files = basedir.listFiles(pathname -> fileFilter(pathname, extensions));
        if (null == files) return
                Collections.emptyList();
        List<File> result = Stream.of(files).filter(File::isFile).collect(Collectors.toList());
        if (recursive) {
            for (File file : files) {
                if (file.isDirectory()) {
                    result.addAll(listFiles(file, extensions, recursive));
                }
            }
        }
        return result;
    }

    private static boolean fileFilter(File file, String... extensions) {
        if (file.isDirectory())
            return true;
        if (null == extensions || extensions.length == 0) {
            return true;
        }
        for (String extension : extensions) {
            if (file.getName().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}
