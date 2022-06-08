package io.github.xtherk.bytehook.util;

import io.github.xtherk.bytehook.Modules;
import io.github.xtherk.bytehook.ex.ReloadClassException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xtherk
 */
public class Reloader implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Reloader.class);
    private final Instrumentation instrumentation;

    public Reloader(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    @Override
    public void run() {
        while (!Modules.initialized) {
            ThreadUtil.quietSleep(10);
        }
        Class<?>[] loadedClasses = instrumentation.getAllLoadedClasses();
        Map<String, Class<?>> classMap = Stream.of(loadedClasses).collect(Collectors.toMap(Class::getName, clz -> clz));
        for (String className : Modules.endpointKeySets()) {
            Class<?> klass = classMap.get(className.replace("/", "."));
            if (null != klass) {
                try {
                    if (instrumentation.isRetransformClassesSupported()) {
                        logger.debug("loading -> {}", className);
                        instrumentation.retransformClasses(klass);
                    }
                } catch (UnmodifiableClassException e) {
                    throw new ReloadClassException("Can't modify the specified class " + className, e);
                }
            }
        }
    }
}
