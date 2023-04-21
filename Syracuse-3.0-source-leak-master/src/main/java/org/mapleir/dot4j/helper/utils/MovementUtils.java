package org.mapleir.dot4j.helper.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

import static java.lang.Math.sqrt;

public class MovementUtils {

    protected static final MinecraftClient mc = MinecraftClient.getInstance();


    public static float getSpeed() {
        return (float) sqrt(mc.player.getVelocity().x * mc.player.getVelocity().x + mc.player.getVelocity().z * mc.player.getVelocity().z);
    }

    public static void strafe() {
        strafe(getSpeed());
    }

    public static boolean test() {
        return mc.options.forwardKey.isPressed() || mc.options.backKey.isPressed() || mc.options.leftKey.isPressed() || mc.options.rightKey.isPressed();
    }

    public static boolean isMoving() {
        return mc.player != null && test();
    }

    public static void resetMotion(Boolean y) {
        mc.player.setVelocity(0, mc.player.getVelocity().y, 0);
        if (y) {
            mc.player.setVelocity(0, 0, 0);
        }
    }


    public static void strafe(float speed) {
        if (!isMoving()) {
            return;
        }
        double direction = getDirection();
        double x = -Math.sin(direction) * speed;
        double z = Math.cos(direction) * speed;

        Vec3d motion = new Vec3d(x, mc.player.getVelocity().y, z);
        mc.player.setVelocity(motion);
    }


    private static double getDirection() {
        double rotationYaw = MinecraftClient.getInstance().player.getYaw();
        if (MinecraftClient.getInstance().player.input.movementForward < 0) {
            rotationYaw += 180;
        }
        double forward = 1;
        if (MinecraftClient.getInstance().player.input.movementForward < 0) {
            forward = -0.5;
        } else if (MinecraftClient.getInstance().player.input.movementForward > 0) {
            forward = 0.5;
        }
        if (MinecraftClient.getInstance().player.input.movementSideways > 0) {
            rotationYaw -= 90 * forward;
        }
        if (MinecraftClient.getInstance().player.input.movementSideways < 0) {
            rotationYaw += 90 * forward;
        }
        return Math.toRadians(rotationYaw);
    }
}
