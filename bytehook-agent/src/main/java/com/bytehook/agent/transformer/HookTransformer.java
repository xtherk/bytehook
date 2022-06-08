package com.bytehook.agent.transformer;


import com.bytehook.core.Endpoint;
import com.bytehook.core.Modules;
import com.bytehook.core.util.TransformUtil;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.List;

/**
 * @author xtherk
 */
public class HookTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        List<Endpoint> endpoints = Modules.getEndpoints(className);
        if (null == endpoints)
            return null;
        return TransformUtil.transform(className, classfileBuffer);
    }
}
