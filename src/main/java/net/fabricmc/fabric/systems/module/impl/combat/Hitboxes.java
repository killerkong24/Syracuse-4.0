package net.fabricmc.fabric.systems.module.impl.combat;

import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;

@Module.Info(name = "Hitboxes", description = "Expands hitboxes", category = Category.COMBAT)

public class Hitboxes extends Module {

    public NumberSetting size = new NumberSetting("Size", 0.1, 2, 0.3, 0.1);

    public Hitboxes() {
        addSettings(size);
    }


    // handled in EntityMixin
    //retarded implementation, you should make some other checks and not expand the hitboxes 100% of the time
}