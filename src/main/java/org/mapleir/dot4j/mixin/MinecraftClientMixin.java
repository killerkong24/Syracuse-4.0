package org.mapleir.dot4j.mixin;

import net.minecraft.client.MinecraftClient;
import org.mapleir.dot4j.ClientMain;
import org.mapleir.dot4j.event.impl.EventUpdate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    public void onUpdate(CallbackInfo ci) {
        new EventUpdate().call();
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void onTick(CallbackInfo ci) {
        ClientMain.getINSTANCE().onTick();
    }
}
