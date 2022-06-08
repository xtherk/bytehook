package com.bytehook.agent;


import com.bytehook.agent.transformer.HookTransformer;
import com.bytehook.core.Modules;
import com.bytehook.core.util.Reloader;

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
