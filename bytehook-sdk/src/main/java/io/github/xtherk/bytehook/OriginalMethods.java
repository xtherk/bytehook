package io.github.xtherk.bytehook;

/**
 * An identifier or placeholder to mark that the original method needs to be called.
 * <p>
 * Overview: <br>
 * The method being hooked is called the "target method" or the "original method",
 * The method to replace the target method is called "fake method".
 * <p>
 * The class in which the target method resides is called the "target class",
 * The class where the fake method is located is called fake class.
 *
 * <p>
 * The framework will not check whether the target method has a return value
 *
 * @author xtherk
 */
public final class OriginalMethods {

    private OriginalMethods() {
    }

    /**
     * If you need to call the target method and the target method has no return value, please call this method
     */
    public static void callWithNoReturnType() {
    }

    /**
     * If you need to call the target method and the target method has a return value, please call this method
     *
     * @param <T> Type of original target method
     * @return The return value of the original target method
     */
    public static <T> T callWithReturnType() {
        return null;
    }

}
