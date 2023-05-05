package net.fabricmc.fabric.mixin;

import net.fabricmc.fabric.event.impl.EndCrystalExplosionMcPlayerEvent;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
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