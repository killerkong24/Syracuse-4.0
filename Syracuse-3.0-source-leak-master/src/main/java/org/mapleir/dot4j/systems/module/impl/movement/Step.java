package org.mapleir.dot4j.systems.module.impl.movement;

import org.mapleir.dot4j.gui.setting.NumberSetting;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

@Module.Info(name = "Step", description = "Makes you step higher", category = Category.MOVEMENT)
public class Step extends Module {

    NumberSetting height = new NumberSetting("Height", 1, 6, 2, 1.0);

    public Step() {
        addSettings(height);
    }

    @Override
    public void onTick() {
        mc.player.stepHeight = height.getFloatValue();
        super.onTick();
    }

    @Override
    public void onDisable() {
        mc.player.stepHeight = 0.6f;
        super.onDisable();
    }
}
