package org.mapleir.dot4j.systems.module.impl.movement;

import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;


@Module.Info(name = "LegitScaffold", description = "Sneaks at the edge of blocks", category = Category.MOVEMENT)
public class LegitScaffold extends Module {


    boolean turn = true;


    @Override
    public void onTick() {
        if (mc.world.getBlockState(mc.player.getSteppingPos()).isAir()) {
            if (!mc.player.isOnGround()) return;
            turn = true;
            mc.options.sneakKey.setPressed(true);
        } else if (turn) {
            turn = false;
            mc.options.sneakKey.setPressed(false);
        }
    }
}
