package net.fabricmc.fabric.mixin;

import net.fabricmc.fabric.systems.module.core.ModuleManager;
import net.fabricmc.fabric.systems.module.impl.combat.Reach;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Inject(method = "getReachDistance", at = @At("HEAD"), cancellable = true)
    public void getReachDistance(CallbackInfoReturnable<Float> cir) {
        if (ModuleManager.INSTANCE.getModuleByClass(Reach.class).isEnabled()) {
            cir.setReturnValue(((Reach) ModuleManager.INSTANCE.getModuleByClass(Reach.class)).areac.getFloatValue());
        }
    }


    @Inject(method = "hasExtendedReach", at = @At("HEAD"), cancellable = true)
    public void hasExtReach(CallbackInfoReturnable<Boolean> cir) {
        if (ModuleManager.INSTANCE.getModuleByClass(Reach.class).isEnabled()) {
            cir.setReturnValue(true);
        }
    }
}