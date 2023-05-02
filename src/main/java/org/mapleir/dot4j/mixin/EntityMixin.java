package org.mapleir.dot4j.mixin;

import net.minecraft.entity.Entity;
import org.mapleir.dot4j.systems.module.core.ModuleManager;
import org.mapleir.dot4j.systems.module.impl.combat.Hitboxes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "getTargetingMargin", at = @At("HEAD"), cancellable = true)
    private void onGetTargetingMargin(CallbackInfoReturnable<Float> info) {
        double v = ((Hitboxes) ModuleManager.INSTANCE.getModuleByClass(Hitboxes.class)).size.getValue();
        if (!ModuleManager.INSTANCE.getModuleByClass(Hitboxes.class).isEnabled()) return;
        info.setReturnValue((float) v);
    }
}