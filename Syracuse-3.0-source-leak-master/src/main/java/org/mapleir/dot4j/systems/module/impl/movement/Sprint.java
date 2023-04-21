package org.mapleir.dot4j.systems.module.impl.movement;

import org.mapleir.dot4j.gui.setting.BooleanSetting;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

@Module.Info(name = "Sprint", description = "Automatically sprints for you", category = Category.MOVEMENT)
public class Sprint extends Module {

    BooleanSetting smart = new BooleanSetting("Smart", false);

    public Sprint() {
        addSettings(smart);
    }

    @Override
    public void onTick() {
        if (this.isEnabled()) {
            if (smart.isEnabled()) {
                if (mc.player.forwardSpeed > 0) mc.player.setSprinting(true);
            } else mc.player.setSprinting(true);
        }
        super.onTick();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
