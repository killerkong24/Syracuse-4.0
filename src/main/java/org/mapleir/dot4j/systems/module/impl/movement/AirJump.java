package org.mapleir.dot4j.systems.module.impl.movement;

import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

@Module.Info(name = "AirJump", description = "Jump midair", category = Category.MOVEMENT)
public class AirJump extends Module {

    @Override
    public void onTick() {

        if (mc.player == null) {
            return;
        }
        if (mc.options.jumpKey.isPressed()) {
            mc.player.setOnGround(true);
            mc.player.fallDistance = 0f;
        }
        super.onTick();
    }
}
