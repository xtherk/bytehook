package com.bytehook.core;


import com.bytehook.sdk.Behavior;
import lombok.Getter;
import lombok.Setter;

/**
 * Used to represent a hook point.
 * Each Endpoint instance means that there is a method that needs will be hooked.
 *
 * @author xtherk
 */

@Getter
public class Endpoint {

    /**
     * Class that needs to be hooked.
     */
    private final String targetClass;

    /**
     * Method that needs to be hooked.
     */
    private final String targetMethod;

    /**
     * Method descriptor that needs to be hooked.
     */
    private final String targetMethodDesc;

    /**
     * Method access that needs to be hooked.
     */
    @Setter
    private int targetAccess = -1;

    /**
     * Replace the located class of the method of the target method
     * {@link #fakeMethod} method class
     */
    private final String fakeClass;

    /**
     * Replace the method name of the target method
     */
    private final String fakeMethod;

    /**
     * Descriptor of fake method
     */
    private final String fakeMethodDesc;

    /**
     * {@link #targetClass} instance
     * When the target method is a static method, the value is false.
     * The first parameter of {@link #fakeMethod} is marked by {@link com.bytehook.sdk.TargetClass}, the value is true.
     */
    private final boolean requireInstance;

    /**
     * Whether the target method needs to be copied
     */
    private final boolean copyTargetMethod;

    /**
     * Flag to replace method call
     */
    private final boolean replaceFlag;

    /**
     * Hook target method behavior
     */
    private final Behavior behavior;

    public Endpoint(String targetClass, String targetMethod, String targetMethodDesc,
                    String fakeClass, String fakeMethod, String fakeMethodDesc,
                    boolean requireInstance, boolean copyTargetMethod, boolean replaceFlag, Behavior behavior) {

        this.targetClass = targetClass;
        this.targetMethod = targetMethod;
        this.targetMethodDesc = targetMethodDesc;

        this.fakeClass = fakeClass;
        this.fakeMethod = fakeMethod;
        this.fakeMethodDesc = fakeMethodDesc;

        this.requireInstance = requireInstance;
        this.copyTargetMethod = copyTargetMethod;
        this.replaceFlag = replaceFlag;
        this.behavior = behavior;
    }
}
