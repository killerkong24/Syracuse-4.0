package net.fabricmc.fabric.systems.module.impl.movement;

import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;

@Module.Info(name = "Step", description = "Makes you step higher", category = Category.MOVEMENT)
public class Step extends Module {

    NumberSetting height = new NumberSetting("Height", 1, 6, 2, 1.0);

    public Step() {
        addSettings(height);
    }

    @Override
    public void onTick() {
        mc.player.setStepHeight(height.getFloatValue());
        super.onTick();
    }

    @Override
    public void onDisable() {
        mc.player.setStepHeight(0.6f);
        super.onDisable();
    }
}
