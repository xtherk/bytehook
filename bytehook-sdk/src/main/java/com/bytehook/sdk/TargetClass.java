package com.bytehook.sdk;

import java.lang.annotation.*;

/**
 * 如果第一个参数被Hook的class
 * @author xtherk
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface TargetClass {
}
