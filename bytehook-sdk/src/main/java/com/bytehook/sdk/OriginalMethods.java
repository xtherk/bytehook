package com.bytehook.sdk;

/**
 * 先描述几个概念:
 * 被Hook的方法称为目标方法或原始方法，替换目标方法的方法称为fake method
 * 目标方法所在的类称为目标类，fake method所在的类称为fake class
 * <p>
 * 用于表示需要调用原始方法的标识或占位符
 * <p>
 * 框架不会去校验目标方法是否有返回值，请开发人员判断选择调用什么方法
 *
 * @author xtherk
 */
public final class OriginalMethods {

    private OriginalMethods() {
    }

    /**
     * 如果你需要调用目标方法且目标方法无返回值，请调用此方法
     */
    public static void callWithNoReturnType() {
    }

    /**
     * 如果你需要调用目标方法且目标方法有返回值，请调用此方法
     */
    public static <T> T callWithReturnType() {
        return null;
    }

}
