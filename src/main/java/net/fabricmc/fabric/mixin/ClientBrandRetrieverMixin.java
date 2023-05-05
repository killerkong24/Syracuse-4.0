package net.fabricmc.fabric.mixin;

import net.fabricmc.fabric.systems.module.core.ModuleManager;
import net.fabricmc.fabric.systems.module.impl.KillerKlient.Spoofer;
import net.minecraft.client.ClientBrandRetriever;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.Setting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin({ClientBrandRetriever.class})
public abstract class ClientBrandRetrieverMixin {

    @Inject(at = @At("HEAD"), method = "getClientModName", cancellable = true, remap = false)
    private static void getClientModName(CallbackInfoReturnable<String> callback) {
        if (ModuleManager.INSTANCE.getModuleByClass(Spoofer.class).isEnabled()) {
            List<Setting> settings = ModuleManager.INSTANCE.getModuleByClass(Spoofer.class).getSettings();
            if (!settings.isEmpty()) {
                Setting setting = settings.get(0);
                if (setting instanceof ModeSetting modeSetting) {
                    switch (modeSetting.getMode()) {
                        case "Vanilla":
                        case "Fabric":
                        case "Feather Fabric":
                        case "Forge":
                            callback.setReturnValue(modeSetting.getMode());
                            break;
                        case "Lunar":
                            callback.setReturnValue("Lunarclient:71aa13d");
                            break;
                        case "Geyser":
                            callback.setReturnValue("Geyser");
                            break;
                    }
                }
            }
        }
    }
}
