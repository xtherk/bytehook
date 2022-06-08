package com.bytehook.sdk;

import java.lang.annotation.*;


/**
 * @author xtherk
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface HookEndpoint {

    /**
     * Class that needs to be hooked.
     *
     * <p>To obtain an inner class, use "$" instead of "." for separating
     * the enclosing class name and the inner class name.</p>
     *
     * @return a fully-qualified class name
     */
    String clazz();

    /**
     * It is used to locate the method that requires hook.
     *
     * @return method descriptor
     */
    String desc() default "";


    /**
     * @return Hook target method behavior.
     */
    Behavior behavior() default Behavior.SET;
}
