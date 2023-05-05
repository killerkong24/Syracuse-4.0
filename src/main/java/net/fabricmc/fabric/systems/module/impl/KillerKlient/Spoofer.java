package net.fabricmc.fabric.systems.module.impl.KillerKlient;

import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;

@Module.Info(name = "Spoofer", description = "REQUIRES RELOGGING", category = Category.KILLERKLIENT)
public class Spoofer extends Module {

    ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Fabric", "Lunar", "Feather Fabric", "Forge", "Geyser");
    public final BooleanSetting resource = new BooleanSetting("Resource Pack", false);

    public Spoofer() {
        addSettings(mode, resource);
    }
    // Done in ClientBrandRetrieverMixin, ResourcePackSendS2CPacketMixin, ServerResourcePackProviderMixin
}
