package org.mapleir.dot4j.systems.module.impl.KillerKlient;

import org.mapleir.dot4j.gui.setting.ModeSetting;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

@Module.Info(name = "Theme", description = "Gui Theme", category = Category.KILLERKLIENT)
public class Theme extends Module{
    ModeSetting theme = new ModeSetting("theme","dark","darkclear","dark");
    public Theme(){addSettings(theme);}

    @Override
    public void onTick() {
        if (theme.isMode("dark")){
            org.mapleir.dot4j.helper.utils.Theme.dark();
        }
        if (theme.isMode("darkclear")){
            org.mapleir.dot4j.helper.utils.Theme.darkclear();
        }
    }
}
