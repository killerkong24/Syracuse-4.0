package org.mapleir.dot4j.systems.module.impl.movement;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.mapleir.dot4j.event.EventTarget;
import org.mapleir.dot4j.event.impl.EventUpdate;
import org.mapleir.dot4j.gui.setting.ModeSetting;
import org.mapleir.dot4j.helper.utils.MovementUtils;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

@Module.Info(name = "Speed", description = "Walk, but faster", category = Category.MOVEMENT)

public class Speed extends Module {

    ModeSetting modeSetting = new ModeSetting("Mode", "Vanilla", "Vanilla", "Vulcan", "Vulcan2", "SlowHop");
    private int jumpTicks = 0;
    private int ticks, offGroundTicks;

    public Speed() {
        addSettings(modeSetting);
    }

    public void onEnable() {
        super.onEnable();
        jumpTicks = 0;
        ticks = 0;
        ticks = 0;
        offGroundTicks = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        jumpTicks = 0;
        ticks = 0;
    }


    @EventTarget
    public void onUpdate(EventUpdate e) {

        if (modeSetting.isMode("Vanilla")) {

            jumpTicks += 1;

            if (isMoving(mc.player)) {
                if (mc.player.isOnGround()) {
                    mc.player.jump();

                    jumpTicks = 0;
                } else {
                    if (jumpTicks > 3)
                        mc.player.setVelocity(mc.player.getVelocity().x, (mc.player.getVelocity().y - 0.08) * 0.98, mc.player.getVelocity().z);

                    strafe(mc.player, getSpeed() * (1.01f - ((float) Math.random() / 500)));
                }
            }
        } else if (modeSetting.isMode("Vulcan2")) {
            if (mc.player.isOnGround() && mc.player.getVelocity().y > -0.2) {
                Vec3d pos = mc.player.getPos();
                Vec3d lastTickPos = new Vec3d(mc.player.prevX, mc.player.prevY, mc.player.prevZ);
                Vec3d newPos = pos.add(lastTickPos).multiply(0.5);
                newPos = newPos.subtract(0, 0.0784000015258789, 0);

                mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(newPos.x, newPos.y, newPos.z, false));
                mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(mc.player.getYaw(), mc.player.getPitch(), true));
                mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y, pos.z, false));
                mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y - 0.0784000015258789, pos.z, false));
                mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(mc.player.getYaw(), mc.player.getPitch(), true));

                MovementUtils.strafe((float) (MovementUtils.getSpeed() * 1.25 * 2));
            } else if (offGroundTicks == 1) {
                MovementUtils.strafe(MovementUtils.getSpeed() * 0.91f);
            }
        }
    }

    @Override
    public void onTick() {
        if (modeSetting.isMode("Vulcan")) {

            mc.options.jumpKey.setPressed(false);

            if (mc.player.isOnGround() && MovementUtils.isMoving()) {
                ticks = 0;
                mc.player.jump();

                MovementUtils.strafe();
                if (MovementUtils.getSpeed() < 0.5f) {
                    MovementUtils.strafe(0.484f);
                }
            }

            if (!mc.player.isOnGround()) {
                ticks++;
            }

            if (ticks == 4) {
                mc.player.setVelocity(mc.player.getVelocity().getX(), mc.player.getVelocity().getY() - 0.17, mc.player.getVelocity().getZ());
            }

            if (ticks == 1) {
                MovementUtils.strafe(0.33f);
            }
        } else if (modeSetting.isMode("Vulcan2")) {
//            if (p instanceof PlayerPositionLookS2CPacket && mc.player.age > 20) {
//                PlayerPositionLookS2CPacket s08 = ((PlayerPositionLookS2CPacket) p);
//                Vec3d packetPos = new Vec3d(s08.getX(), s08.getY(), s08.getZ());
//                if (mc.player.squaredDistanceTo(packetPos) < 25 * 4) {
//                    event.setCancelled(true);
//                }
//            }
        } else if (modeSetting.isMode("SlowHop")) {
            if (mc.player.isSubmergedInWater()) return;
            if (MovementUtils.isMoving()) {
                if (mc.player.isOnGround()) mc.player.jump();
                else mc.player.airStrafingSpeed = 0.05f;
            }
        }
    }

    private boolean isMoving(PlayerEntity player) {
        return player.forwardSpeed != 0 || player.sidewaysSpeed != 0;
    }

    private float getSpeed() {
        return mc.player.getMovementSpeed() * 0.225f;
    }

    private void strafe(PlayerEntity player, float speed) {
        if (player.forwardSpeed != 0.0f || player.sidewaysSpeed != 0.0f) {
            player.setVelocity((float) (Math.cos(Math.toRadians(player.getYaw() + 90.0f)) * player.forwardSpeed + Math.cos(Math.toRadians(player.getYaw())) * player.sidewaysSpeed) * speed, player.getVelocity().y, (float) (Math.sin(Math.toRadians(player.getYaw() + 90.0f)) * player.forwardSpeed + Math.sin(Math.toRadians(player.getYaw())) * player.sidewaysSpeed) * speed);
        }
    }
}
