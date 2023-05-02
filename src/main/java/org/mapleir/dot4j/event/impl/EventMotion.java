package org.mapleir.dot4j.event.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import org.mapleir.dot4j.event.Event;


public class EventMotion extends Event {

    public static final class Pre extends EventMotion {

        private static final Pre instance = new Pre();
        private static float staticYaw, prevYaw;
        private static float staticPitch, prevPitch;
        private float yaw;
        private float pitch;
        private double posX;
        private double posY;
        private double posZ;
        private boolean onGround;

        public static Pre get(final float yaw, final float pitch, final double posX, final double posY, final double posZ, final boolean onGround) {
            instance.setCancelled(false);
            instance.yaw = yaw;
            instance.pitch = pitch;

            instance.posX = posX;
            instance.posY = posY;
            instance.posZ = posZ;

            instance.onGround = onGround;
            return instance;
        }

        public static float getStaticYaw() {
            return staticYaw;
        }

        public static float getPrevYaw() {
            return prevYaw;
        }

        public static float getStaticPitch() {
            return staticPitch;
        }

        public static float getPrevPitch() {
            return prevPitch;
        }

        public void updateLast() {
            prevYaw = staticYaw;
            prevPitch = staticPitch;
            staticYaw = yaw;
            staticPitch = pitch;
        }

        public boolean isOnGround() {
            return onGround;
        }

        public void setOnGround(final boolean onGround) {
            this.onGround = onGround;
        }

        public double getPosX() {
            return posX;
        }

        public void setPosX(final double posX) {
            this.posX = posX;
        }

        public double getPosY() {
            return posY;
        }

        public void setPosY(final double posY) {
            this.posY = posY;
        }

        public double getPosZ() {
            return posZ;
        }

        public void setPosZ(final double posZ) {
            this.posZ = posZ;
        }

        public float getYaw() {
            return yaw;
        }

        public void setYaw(final float yaw) {
            MinecraftClient.getInstance().player.setBodyYaw(yaw);
            MinecraftClient.getInstance().player.headYaw = yaw;
            this.yaw = yaw;
        }

        public float getPitch() {
            return pitch;
        }

        public void setPitch(final float pitch) {
            this.pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
        }

        public void setPosition(final double posX, final double posY, final double posZ, final boolean onGround) {
            setPosX(posX);
            setPosY(posY);
            setPosZ(posZ);
            setOnGround(onGround);
        }
    }

}
