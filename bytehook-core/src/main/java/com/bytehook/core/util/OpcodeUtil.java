package com.bytehook.core.util;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Utility to bytecode operation
 *
 * @author xtherk
 */
public class OpcodeUtil implements Opcodes {

    /**
     * Returns a JVM instruction opcode adapted to this {@link Type}. This method must not be used for
     * * method types.
     *
     * @param type A Java type.
     * @return an opcode that is similar to the given opcode, but adapted to this type. For
     * example, if this type is {@code float} and {@code opcode} is ILOAD, this method returns
     * {@link Opcodes#FLOAD}.
     */
    public static int getLoadOpcodeByType(Type type) {
        return type.getOpcode(Opcodes.ILOAD);
    }

    /**
     * Returns a JVM instruction opcode adapted to this {@link Type}. This method must not be used for
     * * method types.
     *
     * @param type A Java type.
     * @return an opcode that is similar to the given opcode, but adapted to this type. For
     * example, if this type is {@code float} and {@code opcode} is IRETURN, this method returns
     * {@link Opcodes#FRETURN}.
     */
    public static int getReturnOpcodeByType(Type type) {
        return type.getOpcode(Opcodes.IRETURN);
    }

}
