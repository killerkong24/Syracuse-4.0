package net.fabricmc.fabric.systems.module.impl.KillerKlient;

import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;

@Module.Info(name = "Theme", description = "Gui Theme", category = Category.KILLERKLIENT)
public class Theme extends Module{
    ModeSetting theme = new ModeSetting("theme","dark","darkclear","dark");
    public Theme(){addSettings(theme);}

    @Override
    public void onTick() {
        if (theme.isMode("dark")){
            net.fabricmc.fabric.helper.utils.Theme.dark();
        }
        if (theme.isMode("darkclear")){
            net.fabricmc.fabric.helper.utils.Theme.darkclear();
        }
    }
}
