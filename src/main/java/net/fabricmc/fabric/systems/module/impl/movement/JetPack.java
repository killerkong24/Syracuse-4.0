package net.fabricmc.fabric.systems.module.impl.movement;


import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;

@Module.Info(name = "JetPack", description = "Launch yourself by holding jump", category = Category.MOVEMENT)
public class JetPack extends Module {


    @Override
    public void onTick() {
        if (mc.options.jumpKey.isPressed())
            mc.player.jump();
    }
}
