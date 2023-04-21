package org.mapleir.dot4j.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import org.mapleir.dot4j.event.impl.HandSwingEvent;
import org.mapleir.dot4j.gui.setting.BooleanSetting;
import org.mapleir.dot4j.systems.module.core.ModuleManager;
import org.mapleir.dot4j.systems.module.impl.player.NoSlow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerMixin {

    @Shadow
    public abstract boolean isSneaking();

    @Inject(at = @At("HEAD"), method = "swingHand")
    public void swingHand(Hand hand, CallbackInfo ci) {
        new HandSwingEvent(hand).call();
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"), require = 0)
    private boolean tickMovement(ClientPlayerEntity player) {
        if (ModuleManager.INSTANCE.getModuleByClass(NoSlow.class).isEnabled() &&
                ((BooleanSetting) ModuleManager.INSTANCE.getModuleByClass(NoSlow.class).getSettings().get(0)).isEnabled())
            return false;
        return player.isUsingItem();
    }

    @Inject(method = "shouldSlowDown", at = @At("HEAD"), cancellable = true)
    private void onShouldSlowDown(CallbackInfoReturnable<Boolean> info) {
        if (ModuleManager.INSTANCE.getModuleByClass(NoSlow.class).isEnabled()) {
            if (((BooleanSetting) ModuleManager.INSTANCE.getModuleByClass(NoSlow.class).getSettings().get(1)).isEnabled()) {
                info.setReturnValue(false);
            }
        }
    }
}
