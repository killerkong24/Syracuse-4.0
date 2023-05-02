package org.mapleir.dot4j.systems.module.impl.movement;


import org.mapleir.dot4j.gui.setting.ModeSetting;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

@Module.Info(name = "FastFall", description = "Increases Fall Speed", category = Category.MOVEMENT)
public class FastFall extends Module {

    ModeSetting modeSetting = new ModeSetting("Mode", "Vanilla", "Vanilla", "Matrix");

    public FastFall() {
        addSettings(modeSetting);
    }


    @Override
    public void onTick() {
        if (mc.player.fallDistance > 0.4 && modeSetting.isMode("Vanilla")) {
            mc.player.setVelocity(mc.player.getVelocity().x, -1.6, mc.player.getVelocity().z);
        }
        if (mc.player.fallDistance > 0.8f && modeSetting.isMode("Matrix")) {
            mc.player.setVelocity(0, -0.54, 0);
        }
    }
}
