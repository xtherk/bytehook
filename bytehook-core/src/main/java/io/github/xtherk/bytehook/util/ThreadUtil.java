package io.github.xtherk.bytehook.util;

/**
 * @author xtherk
 */
public class ThreadUtil {

    /**
     * @param millis sleep time
     */
    public static void quietSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new IllegalStateException("Illegal interrupt!", e);
        }
    }
}
