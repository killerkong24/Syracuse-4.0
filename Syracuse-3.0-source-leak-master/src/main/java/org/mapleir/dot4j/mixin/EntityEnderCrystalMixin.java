package org.mapleir.dot4j.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
import org.mapleir.dot4j.event.impl.EndCrystalExplosionMcPlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndCrystalEntity.class)
public class EntityEnderCrystalMixin {

    @Inject(method = "crystalDestroyed", at = @At("HEAD"))
    private void onCrystalDestroyed(DamageSource source, CallbackInfo ci) {
        if (source.getAttacker() instanceof ClientPlayerEntity) {
            new EndCrystalExplosionMcPlayerEvent();
        }
    }
}