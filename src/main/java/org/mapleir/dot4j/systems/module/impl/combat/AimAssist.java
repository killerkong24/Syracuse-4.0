package org.mapleir.dot4j.systems.module.impl.combat;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import org.mapleir.dot4j.event.interfaces.impl.ISubWorldRenderEnd;
import org.mapleir.dot4j.gui.setting.BooleanSetting;
import org.mapleir.dot4j.gui.setting.NumberSetting;
import org.mapleir.dot4j.helper.utils.EntityUtils;
import org.mapleir.dot4j.helper.utils.MathUtil;
import org.mapleir.dot4j.helper.utils.Rot;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

@Module.Info(name = "AimAssist", description = "swag", category = Category.COMBAT)

public class AimAssist extends Module implements ISubWorldRenderEnd {
    public NumberSetting distance = new NumberSetting("Distance", 3, 10, 6, 0.1);
    public NumberSetting strength = new NumberSetting("Smoothness", 0, 10, 6, 0.1);
    public BooleanSetting seeOnly = new BooleanSetting("SeeOnly", true);

    public BooleanSetting yawAssist = new BooleanSetting("Vertical", true);
    public BooleanSetting pitchAssist = new BooleanSetting("Horizontal", false);


    public AimAssist() {
        addSettings(distance, strength, seeOnly, yawAssist, pitchAssist);
    }

    public static boolean isOverEntity() {
        HitResult hitResult = mc.crosshairTarget;
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            Entity entity = ((EntityHitResult) hitResult).getEntity();
            return true;
        } else {
            return false;
        }
    }

    public boolean isHoldingFirework() {
        PlayerInventory inventory = mc.player.getInventory();
        ItemStack heldItem = inventory.getMainHandStack();

        return heldItem.getItem() instanceof FireworkRocketItem;
    }

    @Override
    public void onWorldRenderEnd(WorldRenderContext ctx) {
        if (!this.isEnabled()) return;
        if (isHoldingFirework()) return;
        if (isOverEntity()) return;
        if (mc.currentScreen != null) return;

        PlayerEntity targetPlayer = EntityUtils.findClosest(PlayerEntity.class, distance.getFloatValue());

        if (targetPlayer == null || (seeOnly.isEnabled() && !mc.player.canSee(targetPlayer))) {
            return;
        }

        Rot targetRot = MathUtil.getDir(mc.player, targetPlayer.getPos());

        float yawDist = MathHelper.subtractAngles((float) targetRot.yaw(), mc.player.getYaw());
        float pitchDist = MathHelper.subtractAngles((float) targetRot.pitch(), mc.player.getPitch());

        float yaw;
        float pitch;

        float stren = strength.getFloatValue() / 10;

        yaw = mc.player.getYaw();
        if (Math.abs(yawDist) > stren) {
            yaw = mc.player.getYaw();
            if (yawDist < 0) {
                yaw += stren;
            } else if (yawDist > 0) {
                yaw -= stren;
            }
        } else {
            // aw = (float) targetRot.yaw();
        }

        pitch = mc.player.getPitch();
        if (Math.abs(pitchDist) > stren) {
            pitch = mc.player.getPitch();
            if (pitchDist < 0) {
                pitch += stren;
            } else if (pitchDist > 0) {
                pitch -= stren;
            }
        } else {
            // pitch = (float) targetRot.pitch();
        }

        float stren2 = strength.getFloatValue() / 50;
        yaw = MathHelper.lerpAngleDegrees(stren2, mc.player.getYaw(), (float) targetRot.yaw());
        pitch = MathHelper.lerpAngleDegrees(stren2, mc.player.getPitch(), (float) targetRot.pitch());
        if (yawAssist.isEnabled()) mc.player.setYaw(yaw);
        if (pitchAssist.isEnabled()) mc.player.setPitch(pitch);
    }
}