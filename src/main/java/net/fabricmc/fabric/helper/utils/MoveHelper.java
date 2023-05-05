package net.fabricmc.fabric.helper.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class MoveHelper {

    public static boolean hasMovement() {
        final Vec3d playerMovement = PacketHelper.mc.player.getVelocity();
        return playerMovement.getX() != 0 || playerMovement.getY() != 0 || playerMovement.getZ() != 0;
    }

    public static double motionY(final double motionY) {
        final Vec3d vec3d = PacketHelper.mc.player.getVelocity();
        PacketHelper.mc.player.setVelocity(vec3d.x, motionY, vec3d.z);
        return motionY;
    }

    public static double motionYPlus(final double motionY) {
        final Vec3d vec3d = PacketHelper.mc.player.getVelocity();
        PacketHelper.mc.player.setVelocity(vec3d.x, vec3d.y + motionY, vec3d.z);
        return motionY;
    }

    public static double getDistanceToGround(Entity entity) {
        final double playerX = PacketHelper.mc.player.getX();
        final int playerHeight = (int) Math.floor(PacketHelper.mc.player.getY());
        final double playerZ = PacketHelper.mc.player.getZ();

        for (int height = playerHeight; height > 0; height--) {
            final BlockPos checkPosition = new BlockPos((int) playerX, height, (int) playerZ);

            // Check if the block is solid
            if (!PacketHelper.mc.world.isAir(checkPosition)) {
                return playerHeight - height;
            }
        }
        return 0;
    }
}
