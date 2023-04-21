package org.mapleir.dot4j.systems.module.impl.movement;


import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

@Module.Info(name = "JetPack", description = "Launch yourself by holding jump", category = Category.MOVEMENT)
public class JetPack extends Module {


    @Override
    public void onTick() {
        if (mc.options.jumpKey.isPressed())
            mc.player.jump();
    }
}
