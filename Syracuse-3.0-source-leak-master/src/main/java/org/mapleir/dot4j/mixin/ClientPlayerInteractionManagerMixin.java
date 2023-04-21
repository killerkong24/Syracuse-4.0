package org.mapleir.dot4j.mixin;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.mapleir.dot4j.systems.module.core.ModuleManager;
import org.mapleir.dot4j.systems.module.impl.combat.Reach;
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