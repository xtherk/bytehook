package com.bytehook.sdk;

import java.lang.annotation.*;

/**
 * @author xtherk
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface HookMethod {

    /**
     * Method that need to be hooked.
     *
     * @return name of method.
     */
    String methodName() default "";

    /**
     * It is used to locate the method that requires hook.
     *
     * <p>If you do not specify the <code>desc()</code>,
     * We will find the descriptor of the same method as you define. </p>
     *
     * @return method descriptor.
     */
    String desc() default "";

    /**
     * Whether to call the original method, similar to the around in AOP
     * @return if you need call the original method then return true, else return false
     */
    boolean original() default false;

    /**
     * @return Hook target method behavior.
     */
    Behavior behavior() default Behavior.SET;
}
