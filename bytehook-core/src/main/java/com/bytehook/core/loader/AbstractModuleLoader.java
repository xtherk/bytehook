package com.bytehook.core.loader;

import com.bytehook.sdk.*;
import com.bytehook.core.ModuleLoader;
import com.bytehook.core.ex.IllegalEndpointException;
import com.bytehook.core.util.CancelSignal;
import com.bytehook.core.util.MethodTuple;
import com.bytehook.core.Endpoint;
import org.objectweb.asm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Abstract implementation of module loader
 *
 * @author xtherk
 */
public abstract class AbstractModuleLoader implements ModuleLoader {

    private static final Logger logger = LoggerFactory.getLogger(AbstractModuleLoader.class);
    private static final String HOOK_CLASS_DESCRIPTOR = Type.getDescriptor(HookClass.class);

    /**
     * @param path module file or directory
     * @return key: class name, value: class bytecode
     * @throws IOException Io errors
     */
    public abstract Map<String, byte[]> parse(Path path) throws IOException;

    @Override
    public List<Endpoint> load(Path path) throws IOException {
        Map<String, byte[]> content = parse(path);
        List<String> endpointClasses = getHookClasses(content);
        if (endpointClasses.isEmpty()) {
            return Collections.emptyList();
        }
        List<Endpoint> allEndpoint = new ArrayList<>();
        for (String endpointClass : endpointClasses) {
            try {
                // Class<?> entrypoint = classLoader.lookupClass(endpointClass);
                Class<?> entrypoint = Class.forName(endpointClass);
                List<Endpoint> endpoints = readEndpoints(entrypoint);
                allEndpoint.addAll(endpoints);
                // classLoader.register();
            } catch (ClassNotFoundException e) {
                logger.error("Class endpoint was not found: ", e);
            }
        }
        return allEndpoint;
    }

    /**
     * Locates module endpoint class.
     *
     * @param content {@link Map} containing archive data.
     * @return located plugin entrypoint or {@code null},
     * if not found.
     */
    private static List<String> getHookClasses(Map<String, byte[]> content) {
        List<String> classes = new ArrayList<>();
        ClassVisitor visitor = new HookClassAnnotationVisitor();
        for (Map.Entry<String, byte[]> entry : content.entrySet()) {
            String className = entry.getKey();
            ClassReader cr = new ClassReader(entry.getValue());
            try {
                cr.accept(visitor, ClassReader.SKIP_FRAMES | ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG);
            } catch (CancelSignal signal) {
                classes.add(className);
            }
        }
        return classes;
    }

    /**
     * Reads module endpoint from the class.
     *
     * @param fakeClass endpoint class.
     * @return endpoints
     */
    private static List<Endpoint> readEndpoints(Class<?> fakeClass) {
        List<Endpoint> endpoints = new ArrayList<>();
        HookClass targetClass = fakeClass.getAnnotation(HookClass.class);
        if (targetClass == null) {
            throw new IllegalStateException("@HookClass is missing on " + fakeClass);
        }
        for (Method fakeMethod : fakeClass.getDeclaredMethods()) {
            HookMethod endpoint = fakeMethod.getAnnotation(HookMethod.class);
            if (null != endpoint) {
                if (!Modifier.isStatic(fakeMethod.getModifiers())) {
                    throw new IllegalEndpointException("The endpoint must have static modifier.");
                }
                Endpoint ep = createEndpoint(targetClass, endpoint, fakeClass, fakeMethod);
                endpoints.add(ep);
                if (endpoint.original()) {
                    Endpoint sourceMethodEndpoint = new Endpoint(ep.getFakeClass(), ep.getFakeMethod(), ep.getFakeMethodDesc(),
                            ep.getTargetClass(), ep.getTargetMethod(), ep.getTargetMethodDesc(), ep.isRequireInstance(), false
                            , true, Behavior.SET);
                    endpoints.add(sourceMethodEndpoint);
                }
            }
        }
        return endpoints;
    }

    private static void earlyLoadClass(String className) {
        className = className.replace("/", ".");
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static Endpoint createEndpoint(HookClass targetClass, HookMethod endpoint, Class<?> fakeClass, Method fakeMethod) {
        String targetClassName = targetClass.value().replace(".", "/");
        if (endpoint.original()) {
            earlyLoadClass(targetClassName);
        }
        String fakeMethodDesc = Type.getMethodDescriptor(fakeMethod);
        MethodTuple tuple = getMethodTuple(fakeMethod, endpoint);
        String fakeMethodName = fakeMethod.getName();
        String targetMethod = getHookMethodName(endpoint, fakeMethod);
        String fakeClassName = Type.getInternalName(fakeClass);
        return new Endpoint(targetClassName, targetMethod, tuple.targetMethodDesc,
                fakeClassName, fakeMethodName, fakeMethodDesc, tuple.requireInstance,
                endpoint.original(), false, endpoint.behavior());
    }

    private static MethodTuple getMethodTuple(Method method, HookMethod endpoint) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length > 0) {
            Parameter first = parameters[0];
            TargetClass targetClass = first.getAnnotation(TargetClass.class);
            if (null != targetClass) {
                // Exclude target class instance param
                if (parameters.length - 1 == 0) {
                    String desc = getDescOrDefault(endpoint, Type.getMethodDescriptor(Type.getType(method.getReturnType())));
                    return MethodTuple.of(desc, true);
                }
                Type[] argumentTypes = new Type[parameters.length - 1];
                for (int i = 1; i < parameters.length; i++) {
                    argumentTypes[i - 1] = Type.getType(parameters[i].getType());
                }
                String desc = getDescOrDefault(endpoint, Type.getMethodDescriptor(Type.getType(method.getReturnType()), argumentTypes));
                return MethodTuple.of(desc, true);
            }
        }
        String desc = getDescOrDefault(endpoint, Type.getMethodDescriptor(method));
        return MethodTuple.of(desc, false);
    }

    private static String getDescOrDefault(HookMethod endpoint, String defaultDesc) {
        // Preferential use of annotations
        return endpoint.desc().isEmpty() ? defaultDesc : endpoint.desc();
    }

    private static String getHookMethodName(HookMethod targetMethod, Method method) {
        String methodName = targetMethod.methodName();
        return methodName.isEmpty() ? method.getName() : methodName;
    }

    private static final class HookClassAnnotationVisitor extends ClassVisitor {
        HookClassAnnotationVisitor() {
            super(Opcodes.ASM8);
        }

        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            if (HOOK_CLASS_DESCRIPTOR.equals(descriptor)) {
                throw CancelSignal.get();
            }
            return null;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
    }
}
