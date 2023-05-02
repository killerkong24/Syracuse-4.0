package org.mapleir.dot4j.helper.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static org.mapleir.dot4j.helper.utils.PacketHelper.mc;

public class MathUtil {
    public static int compareDist(Entity entityA, Entity entityB) {
        return Float.compare(entityA.distanceTo(mc.player), entityB.distanceTo(mc.player));
    }

    public static Rot getDir(Entity entity, Vec3d vec) {
        double dx = vec.x - entity.getX(),
                dy = vec.y - entity.getY(),
                dz = vec.z - entity.getZ(),
                dist = MathHelper.sqrt((float) (dx * dx + dz * dz));

        return new Rot(MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(dz, dx)) - 90.0), -MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(dy, dist))));
    }

    public static boolean isInFOV(final Entity entity, double angle) {
        angle *= 0.5;

        if(entity != null) {
            final double angleDiff = getAngleDifference(mc.player.getYaw(), getRotations(entity)[0]);

            return angleDiff > 0 && angleDiff < angle || -angle < angleDiff && angleDiff < 0 && entity != null;
        }
        return false;
    }

    public static float getAngleDifference(final float dir, final float yaw) {
        final float f = Math.abs(yaw - dir) % 360;
        float dist = f;

        if(f > 180.0F) dist = 360.0F - f;

        return dist;
    }

    public static float[] getRotations(final Entity ent) {
        final double x = ent.getPos().getX();
        final double y = ent.getPos().getY() + ent.getEyeHeight(ent.getPose());
        final double z = ent.getPos().getZ();
        return getRotationFromPosition(x, y, z);
    }

    public static float[] getRotationFromPosition(final double x, final double y, final double z) {
        final double xDiff = x - mc.player.getPos().getX();
        final double yDiff = y - (mc.player.getPos().getY() + mc.player.getEyeHeight(mc.player.getPose()));
        final double zDiff = z - mc.player.getPos().getZ();

        final double dist = MathHelper.hypot(xDiff, zDiff);
        final float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / Math.PI) - 90.0f;
        final float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / Math.PI);

        return new float[]
                { yaw, pitch };
    }

    public static float[] getNeededRotations(final Vec3d vec) {
        final Vec3d eyesPos = mc.player.getEyePos();

        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;

        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        final float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
        final float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

        return new float[]
                { MathHelper.wrapDegrees(yaw), MathHelper.wrapDegrees(pitch) };
    }

    public static double getAngleToLookVec(final Vec3d vec) {
        final float[] needed = getNeededRotations(vec);

        final float currentYaw = MathHelper.wrapDegrees(mc.player.getYaw());
        final float currentPitch = MathHelper.wrapDegrees(mc.player.getPitch());

        final float diffYaw = currentYaw - needed[0];
        final float diffPitch = currentPitch - needed[1];

        return Math.hypot(diffYaw, diffPitch);
    }
}