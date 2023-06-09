package net.fabricmc.fabric.systems.module.impl.Cpvp;

import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
@Module.Info(name = "Crystal Pop", description = "Crystal Breaker", category = Category.CPVP)
public class CrystalPop extends Module{
    public NumberSetting delay = new NumberSetting("Delay", 0, 10, 0, 1);

    private double tickTimer;
    public CrystalPop() {
        addSettings(delay);
        tickTimer = 0;
    }

    @Override
    public void onTick() {
        HitResult hit = mc.crosshairTarget;
        if (hit.getType() != HitResult.Type.ENTITY)
            return;
        Entity target = ((EntityHitResult) hit).getEntity();
        if (target instanceof EndCrystalEntity endCrystalEntity) {
            if (tickTimer > -1) {
                tickTimer--;
                return;
            }
            mc.interactionManager.attackEntity(mc.player, endCrystalEntity);
            tickTimer = (int) delay.getValue();
        }
    }
}
