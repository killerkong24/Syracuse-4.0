package net.fabricmc.fabric.systems.module.impl.combat;

import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;

@Module.Info(name = "Reach", description = "Extends your reach", category = Category.COMBAT)

public class Reach extends Module {
    public final NumberSetting areac = new NumberSetting("Reach", 1, 6, 4, 0.1); // this is called size cuz i messed smth up in the settings n stuff :skull emoji x7:

    public Reach() {
        addSettings(areac);
    }

    // handled in ClientPlayerInteractionManagerMixin
    //here too, should make additional checks
}
