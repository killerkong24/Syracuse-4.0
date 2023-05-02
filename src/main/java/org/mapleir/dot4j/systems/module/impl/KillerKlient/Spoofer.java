package org.mapleir.dot4j.systems.module.impl.KillerKlient;

import org.mapleir.dot4j.gui.setting.BooleanSetting;
import org.mapleir.dot4j.gui.setting.ModeSetting;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

@Module.Info(name = "Spoofer", description = "REQUIRES RELOGGING", category = Category.KILLERKLIENT)
public class Spoofer extends Module {

    ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Fabric", "Lunar", "Feather Fabric", "Forge", "Geyser");
    public final BooleanSetting resource = new BooleanSetting("Resource Pack", false);

    public Spoofer() {
        addSettings(mode, resource);
    }
    // Done in ClientBrandRetrieverMixin, ResourcePackSendS2CPacketMixin, ServerResourcePackProviderMixin
}
