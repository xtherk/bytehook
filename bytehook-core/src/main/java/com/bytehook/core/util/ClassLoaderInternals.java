package com.bytehook.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * Hacky code to get internal classloader state.
 *
 * @author xDark - This was his idea.
 * @author Matt Coley - I actually committed the file.
 * @author xtherk - Add support for Java8
 */
public class ClassLoaderInternals {
    /**
     * @return <code>sun.misc.URLClassPath</code> or <code>jdk.internal.loader.URLClassPath</code> instance.
     * @throws ReflectiveOperationException When the internals change and the reflective look-ups fail.
     */
    public static Object getUcp() throws ReflectiveOperationException {
        int jdkVersion = JavaVersion.get();
        Class<?> ucpOwner;
        // Fetch UCP of application's ClassLoader
        Object appClassLoader;
        if (jdkVersion <= 8) {
            ucpOwner = Class.forName("java.net.URLClassLoader");
            appClassLoader = ClassLoaderInternals.class.getClassLoader();
            // In some cases, the classloader cannot be obtained,
            // so we can get from context classloader of thread
            if (null == appClassLoader) {
                appClassLoader = Thread.currentThread().getContextClassLoader();
            }
        } else {
            Class<?> clsClassLoaders = Class.forName("jdk.internal.loader.ClassLoaders");
            Java9Util.openPackage(clsClassLoaders);
            appClassLoader = clsClassLoaders.getDeclaredMethod("appClassLoader").invoke(null);
            ucpOwner = appClassLoader.getClass();
            // Field removed in 16, but still exists in parent class "BuiltinClassLoader"
            if (jdkVersion >= 16)
                ucpOwner = ucpOwner.getSuperclass();
        }
        Field fieldUCP = ReflectUtil.getDeclaredField(ucpOwner, "ucp");
        return fieldUCP.get(appClassLoader);
    }

    private static Object getAppClassLoaderForJava8(Class<?> klass) {
        ClassLoader classLoader = klass.getClassLoader();
        if (null == classLoader) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        return classLoader;
    }

    /**
     * @param ucp See {@link #getUcp()}.
     * @param url URL to add to the search path for directories and Jar files of the UCP.
     * @throws ReflectiveOperationException When the internals change and the reflective look-ups fail.
     */
    public static void appendToUcpPath(Object ucp, URL url) throws ReflectiveOperationException {
        if (ucp == null)
            return;
        Class<?> ucpClass = ucp.getClass();
        Method addURL = ReflectUtil.getDeclaredMethod(ucpClass, "addURL", URL.class);
        addURL.invoke(ucp, url);
    }
}
