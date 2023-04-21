package org.mapleir.dot4j.systems.module.impl.movement;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.mapleir.dot4j.event.EventTarget;
import org.mapleir.dot4j.event.impl.EventUpdate;
import org.mapleir.dot4j.gui.setting.BooleanSetting;
import org.mapleir.dot4j.gui.setting.ModeSetting;
import org.mapleir.dot4j.gui.setting.NumberSetting;
import org.mapleir.dot4j.helper.utils.MoveHelper;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

@Module.Info(name = "Fly", description = "Makes you fly!", category = Category.MOVEMENT)

public class Fly extends Module {

    ModeSetting modeSetting = new ModeSetting("Mode", "Vanilla", "Vanilla", "Hypixel", "Vulcan", "Elytra", "Jetpack");
    NumberSetting speed = new NumberSetting("Speed", 0.01, 10, 0.2, 0.01);
    BooleanSetting warn = new BooleanSetting("Warn", true);
    NumberSetting clip = new NumberSetting("Clip", 1, 100, 10, 1);
    BooleanSetting Clip = new BooleanSetting("Clipping", true);
    double startHeight;

    public Fly() {
        addSettings(modeSetting, clip, Clip, speed, warn);
    }

    public static boolean wearingElytra(PlayerInventory inventory) {
        int chestSlot = 2;
        return (inventory.armor.get(chestSlot).getCount() == 1 &&
                inventory.armor.get(chestSlot).getItem().asItem().equals(Items.ELYTRA));
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (warn.isEnabled()) {
            sendMsg("&7Warning: You can still take fall damage!");
        }
        if (mc.player == null) return;

        //System.out.println(startHeight);
        startHeight = mc.player.getY();
        //mc.player.getAbilities().allowFlying = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (mc.player == null) return;
        if (!mc.player.getAbilities().creativeMode) {
            mc.player.getAbilities().allowFlying = false;
            mc.player.getAbilities().flying = false;
        }
        mc.player.getAbilities().setFlySpeed(0.1f);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (mc.player == null) return;
        if (mc.player.isDead()) return;
        if (modeSetting.isMode("Vanilla")) {
            mc.player.getAbilities().flying = true;
            mc.player.getAbilities().setFlySpeed(speed.getFloatValue());
        } else if (modeSetting.isMode("Elytra") && wearingElytra(mc.player.getInventory())) {

            if (mc.player.isFallFlying()) {
                mc.player.setVelocity(0, 0, 0);
                return;
            }
            Vec3d antiKickVel = Vec3d.ZERO;

            if (mc.player.age % 20 == 0
                    && mc.world.getBlockState(new BlockPos(new BlockPos(mc.player.getPos().add(0, -0.069, 0)))).getMaterial().isReplaceable()) {
                antiKickVel = antiKickVel.add(0, -0.069, 0);
            }

            mc.player.setVelocity(antiKickVel);

            Vec3d forward = new Vec3d(0, 0, (speed.getFloatValue() * 5)).rotateY(-(float) Math.toRadians(mc.player.getYaw()));
            Vec3d strafe = forward.rotateY((float) Math.toRadians(90));

            if (mc.options.jumpKey.isPressed())
                mc.player.setVelocity(mc.player.getVelocity().add(0, (speed.getFloatValue() * 5), 0));
            if (mc.options.sneakKey.isPressed())
                mc.player.setVelocity(mc.player.getVelocity().add(0, -(speed.getFloatValue() * 5), 0));
            if (mc.options.backKey.isPressed())
                mc.player.setVelocity(mc.player.getVelocity().add(-forward.x, 0, -forward.z));
            if (mc.options.forwardKey.isPressed())
                mc.player.setVelocity(mc.player.getVelocity().add(forward.x, 0, forward.z));
            if (mc.options.leftKey.isPressed())
                mc.player.setVelocity(mc.player.getVelocity().add(strafe.x, 0, strafe.z));
            if (mc.options.rightKey.isPressed())
                mc.player.setVelocity(mc.player.getVelocity().add(-strafe.x, 0, -strafe.z));
        }

        if (modeSetting.isMode("Vulcan")) {

            double clipHeight = startHeight - clip.getValue();
            //System.out.println("The Player Height is " + mc.player.getY() + "\n And the clip height is " + mc.player.getY());

            if (mc.player.fallDistance > 2) {
                mc.player.setOnGround(true);
                mc.player.fallDistance = 0f;
            }
            if (mc.player.age % 3 == 0) {
                MoveHelper.motionYPlus(0.026);
            } else {
                MoveHelper.motionY(-0.0991);
            }
            if (Clip.isEnabled() && clipHeight == mc.player.getY()) {
                mc.player.updatePosition(mc.player.getX(), mc.player.getY() + clip.getValue(), mc.player.getZ());
            }
        } else if (modeSetting.isMode("Jetpack")) {
            if (mc.options.jumpKey.isPressed()) {
                mc.player.jump();
            }
        }
    }
}
