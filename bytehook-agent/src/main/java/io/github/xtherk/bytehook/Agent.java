package io.github.xtherk.bytehook;


import io.github.xtherk.bytehook.transformer.HookTransformer;
import io.github.xtherk.bytehook.util.Reloader;

import java.lang.instrument.Instrumentation;

/**
 * @author xtherk
 */
public class Agent {

    public static void agentmain(String args, Instrumentation inst) {
        Modules.initialize();
        inst.addTransformer(new HookTransformer(), true);
        new Thread(new Reloader(inst)).start();
    }

    public static void premain(String args, Instrumentation inst) {
        Modules.initialize();
        inst.addTransformer(new HookTransformer(), true);
        new Thread(new Reloader(inst)).start();
    }
}
