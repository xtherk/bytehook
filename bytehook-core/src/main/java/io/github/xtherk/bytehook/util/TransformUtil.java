package io.github.xtherk.bytehook.util;

import io.github.xtherk.bytehook.Behavior;
import io.github.xtherk.bytehook.Endpoint;
import io.github.xtherk.bytehook.Modules;
import io.github.xtherk.bytehook.ex.IllegalEndpointException;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.List;

/**
 * @author xtherk
 */
public class TransformUtil {
    private static final int api = Opcodes.ASM8;
    private static final Logger logger = LoggerFactory.getLogger(TransformUtil.class);

    public static byte[] transform(String className, byte[] classfileBuffer) {
        List<Endpoint> endpoints = Modules.getEndpoints(className);
        if (null == endpoints)
            return null;

        logger.info("The [{}] class that needs to be replaced is detected.", className);

        ClassNode node = new ClassNode(api);
        ClassReader reader = new ClassReader(classfileBuffer);
        reader.accept(node, 0);

        for (Endpoint endpoint : endpoints) {
            for (MethodNode method : node.methods) {
                if (method.name.equals(endpoint.getTargetMethod())
                        && method.desc.equals(endpoint.getTargetMethodDesc())) {

                    proxyTargetMethod(endpoint, method);
                    break;
                }
            }
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }

    private static void proxyTargetMethod(Endpoint endpoint, MethodNode method) {

        if (endpoint.getBehavior() == Behavior.SET) {
            method.tryCatchBlocks.clear();
            method.instructions.clear();
        }

        InsnList instNodes = new InsnList();
        Type[] argumentTypes = Type.getArgumentTypes(method.desc);
        Type returnType = Type.getReturnType(method.desc);

        boolean isStatic = Modifier.isStatic(method.access);
        if (endpoint.isRequireInstance()) {
            if (isStatic) {
                throw new IllegalEndpointException("The method of being hook is a static method, which cannot provide instance.");
            }
            instNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
        }

        if (argumentTypes.length > 0) {
            int slot = isStatic ? 0 : 1;
            for (Type argumentType : argumentTypes) {
                int opcode = OpcodeUtil.getLoadOpcodeByType(argumentType);
                instNodes.add(new VarInsnNode(opcode, slot));
                if (argumentType == Type.LONG_TYPE || argumentType == Type.DOUBLE_TYPE)
                    slot += 2;
                else
                    slot++;
            }
        }

        String fakeClass = endpoint.getFakeClass();
        String fakeMethod = endpoint.getFakeMethod();
        String fakeMethodDesc = endpoint.getFakeMethodDesc();

        // Call fake method
        instNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, fakeClass, fakeMethod, fakeMethodDesc, false));
        instNodes.add(new InsnNode(OpcodeUtil.getReturnOpcodeByType(returnType)));
        method.instructions.insert(instNodes);
    }

}
