package org.mapleir.dot4j.helper.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SnowGolemEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mapleir.dot4j.helper.utils.PacketHelper.mc;

public class EntityUtils {

    @SuppressWarnings("unchecked")
    public static <T extends Entity> List<T> findEntities(Class<T> entityClass) {
        List<T> entities = new ArrayList<>();
        for (Entity entity : mc.world.getEntities()) {
            if (entity.equals(mc.player)) continue;
            if (entityClass.isAssignableFrom(entity.getClass())) {
                entities.add((T) entity);
            }
        }
        return entities;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> T findClosest(Class<T> entityClass, float range) {
        for (Entity entity : mc.world.getEntities()) {
            if (entityClass.isAssignableFrom(entity.getClass()) && !entity.equals(mc.player) && entity.distanceTo(mc.player) <= range) {
                return (T) entity;
            }
        }
        return null;
    }


    public static boolean isAnimal(Entity e) {
        return e instanceof PassiveEntity
                || e instanceof AmbientEntity
                || e instanceof WaterCreatureEntity
                || e instanceof IronGolemEntity
                || e instanceof SnowGolemEntity;
    }
}