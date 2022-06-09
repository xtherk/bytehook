package io.github.xtherk.bytehook;


import io.github.xtherk.bytehook.transformer.HookTransformer;
import io.github.xtherk.bytehook.util.Reloader;

import java.lang.instrument.Instrumentation;

/**
 * @author xtherk
 */
public class Agent {

    /**
     * When you want to debug, maybe you want to control the initialization time yourself
     */
    private static final String DISABLE_PROPERTY_NAME = "bh.agent.init.disabled";

    public static void agentmain(String args, Instrumentation inst) {
        Modules.initialize();
        inst.addTransformer(new HookTransformer(), true);
        new Thread(new Reloader(inst)).start();
    }

    public static void premain(String args, Instrumentation inst) {
        boolean disabled = Boolean.parseBoolean(System.getProperty(DISABLE_PROPERTY_NAME, "false"));
        if (!disabled) {
            Modules.initialize();
        }
        inst.addTransformer(new HookTransformer(), true);
        new Thread(new Reloader(inst)).start();
    }
}
