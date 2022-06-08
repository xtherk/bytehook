package com.bytehook.core.util;

/**
 * @author xtherk
 */
public class MethodTuple {
    public final String targetMethodDesc;
    public final boolean requireInstance;

    public MethodTuple(String targetMethodDesc, boolean requireInstance) {
        this.targetMethodDesc = targetMethodDesc;
        this.requireInstance = requireInstance;
    }

    public static MethodTuple of(String desc, boolean requireInstance) {
        return new MethodTuple(desc, requireInstance);
    }
}
