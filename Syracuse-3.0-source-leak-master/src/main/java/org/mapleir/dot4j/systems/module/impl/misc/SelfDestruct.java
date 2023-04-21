package org.mapleir.dot4j.systems.module.impl.misc;

import org.mapleir.dot4j.ClientMain;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;
import org.mapleir.dot4j.systems.module.core.ModuleManager;

import java.util.Properties;

@Module.Info(name = "SelfDestruct", description = "Removes Syracuse from your game", category = Category.MISC)

public class SelfDestruct extends Module {

    @Override
    public void onEnable() {
        ClientMain.getINSTANCE().isSelfDestucted = true;
        mc.setScreen(null);
        for (Module m : ModuleManager.INSTANCE.getModules()) {
            m.setEnabled(false);
            m.setDescription(null);
            m.setName(null);
            m.setDisplayName(null);
        }
        try {
            System.gc();
            System.runFinalization();
            System.gc();
            Thread.sleep(100L);
            System.gc();
            System.runFinalization();
            Thread.sleep(200L);
            System.gc();
            System.runFinalization();
            Thread.sleep(300L);
            System.gc();
            System.runFinalization();
        } catch (InterruptedException e) {
            sendMsg("Self Destruct Failed.");
            throw new RuntimeException(e);
        }

        System.gc();
    }
}

