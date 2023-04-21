package org.mapleir.dot4j.systems.module.impl.combat;

import net.minecraft.block.Material;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import org.lwjgl.glfw.GLFW;
import org.mapleir.dot4j.event.EventTarget;
import org.mapleir.dot4j.event.impl.EventUpdate;
import org.mapleir.dot4j.gui.setting.BooleanSetting;
import org.mapleir.dot4j.gui.setting.ModeSetting;
import org.mapleir.dot4j.gui.setting.NumberSetting;
import org.mapleir.dot4j.helper.utils.EntityUtils;
import org.mapleir.dot4j.helper.utils.MathUtil;
import org.mapleir.dot4j.helper.utils.MovementUtils;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

import java.util.*;

@Module.Info(name = "KillAura", description = "hit player", category = Category.COMBAT)
public class KillAura extends Module {

    public ModeSetting items = new ModeSetting("items", "Sword", "Sword", "Axe", "Pickaxe", "Shovel", "Any", "All");
    public ModeSetting priority = new ModeSetting("priority", "Health", "Angle", "Distance", "Health", "Armor");
    public NumberSetting range = new NumberSetting("range", 3, 6, 3.8, 0.1);
    public NumberSetting fov = new NumberSetting("fov", 0, 360, 180, 1);
    public NumberSetting cooldown = new NumberSetting("Cooldown", 0.86, 0.99, 0.9, 0.01);
    public NumberSetting critDistance = new NumberSetting("Critical Distance", 0.02, 0.5, 025, 0.01);
    public BooleanSetting stopSprint = new BooleanSetting("Stop Sprinting", true);
    public BooleanSetting noSwing = new BooleanSetting("noSwing", true);
    public BooleanSetting players = new BooleanSetting("players", true);
    public BooleanSetting animals = new BooleanSetting("animals", true);
    public BooleanSetting monsters = new BooleanSetting("monsters", true);
    public BooleanSetting villagers = new BooleanSetting("villagers", true);
    public BooleanSetting invisibles = new BooleanSetting("invisibles", true);

    public static List<LivingEntity> targets = new LinkedList<>();
    public static Entity target = null;

    public KillAura() {
        addSettings(items, priority, range, fov, cooldown, critDistance, stopSprint, noSwing, players, animals, monsters, villagers, invisibles);
    }

    public void send(final Packet<?> packetIn) {
        if (packetIn == null) {
            return;
        }
        Objects.requireNonNull(mc.getNetworkHandler()).getConnection().send(packetIn);
    }
    @Override
    public void onDisable() {
        if (targets != null) {
            targets.clear();
        }
        target = null;

        super.onDisable();
    }

    @EventTarget
    public void onUpdate(final EventUpdate e) {

        if (isNull()) {
            return;
        }

        //final Entity entity = target;

        if (!mc.player.isAlive() || mc.player.getHealth() == 0 || !itemInHand() || !canHit()) {
            return;
        }

        final List<LivingEntity> list = getTargets();

        if (list == null) {
            return;
        }

        targets = list;
        if (targets.isEmpty()) {
            return;
        }

        final int size = Math.min(targets.size(), 1);

        for (int i = 0; i < size; ++i)
        {
            final LivingEntity en = targets.get(i);

            if (isTarget(en)) {
                attackTarget(en);
            }
        }
        targets.clear();

    }


    //TODO rotations (volcan or someone else!!!)



    private List<LivingEntity> getTargets() {
        final List<LivingEntity> livings = new ArrayList<>();

        for (final Entity e : mc.world.getEntities()) {
            if (e == null || !e.isAlive()) {
                continue;
            }
            if (e instanceof ClientPlayerEntity || Math.sqrt(mc.player.squaredDistanceTo(e)) > (range.getValue() == 6 ? 6.1D : range.getValue()) && e instanceof PlayerEntity) {
                continue;
            }

            if (e instanceof PlayerEntity || EntityUtils.isAnimal(e) || e instanceof Monster || e instanceof VillagerEntity) {
                final LivingEntity living = (LivingEntity) e;

                if (!invisibles.isEnabled() && (living.getStatusEffect(StatusEffects.INVISIBILITY) != null || living.isInvisible())) {
                    continue;
                }

                if (living instanceof PlayerEntity) {
                    if (!players.isEnabled()) {
                        continue;
                    }

                }
                else if (!animals.isEnabled() && EntityUtils.isAnimal(living)) {
                    continue;
                }
                else if (!monsters.isEnabled() && living instanceof Monster || !villagers.isEnabled() && living instanceof VillagerEntity) {
                    continue;
                }
                if (MathUtil.isInFOV(living, fov.getValue())) {
                    livings.add(living);
                }

            }
        }
        if (livings.isEmpty()) {
            return null;
        }

        livings.sort(Comparator.comparingDouble(e -> priority.getMode().equalsIgnoreCase("Angle") ? MathUtil.getAngleToLookVec(e.getBoundingBox().getCenter()) : priority.getMode().equalsIgnoreCase("Health") ? e.getHealth() : priority.getMode().equalsIgnoreCase("Distance") ? e.squaredDistanceTo(mc.player) : e instanceof PlayerEntity ? ((PlayerEntity) e).getArmor() : e.getHealth()));

        return livings;
    }


    private boolean canHit() {
        return mc.player.getAttackCooldownProgress(mc.getTickDelta()) <= cooldown.getFloatValue();
    }

    private void performHit(final LivingEntity e) {

        if (stopSprint.isEnabled()) {
            if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_SEMICOLON)) {
                if (MovementUtils.isMoving()) {
                    send(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
                }
            }
            else {
                send(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
            }
        }


        if (canHit()) {
            mc.interactionManager.attackEntity(mc.player, e);
        }
        //((IClientPlayerInteractionManager) mc.interactionManager).syncSelected().syncSelectedSlot();

        //mc.player.networkHandler.sendPacket(new PlayerInteractEntityC2SPacket(target, mc.player.isSneaking()));

        if (mc.interactionManager.getCurrentGameMode() != GameMode.SPECTATOR) {
            mc.player.resetLastAttackedTicks();
        }

        if (!noSwing.isEnabled()) {
            mc.player.swingHand(Hand.MAIN_HAND);
        }

        if (stopSprint.isEnabled() && MovementUtils.isMoving() && !mc.player.horizontalCollision && mc.player.getHungerManager().getFoodLevel() > 6) {
            send(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
        }
    }

private void attackTarget(final LivingEntity e)
        {
            if (mc.player == null) {
                return;
            }

            if (mc.player.isOnGround()) {
                performHit(e);
            }
            else if (mc.player.fallDistance >= critDistance.getFloatValue() && mc.player.getVelocity().getY() < 0 || InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_SEMICOLON) || mc.player.getStatusEffect(StatusEffects.LEVITATION) != null || mc.player.isFallFlying() || mc.player.isHoldingOntoLadder() || mc.player.isRiding()
                    || mc.player.isSubmergedInWater() || mc.player.isInLava() || mc.player.getVelocity().getY() == 0) {
                performHit(e);
            }
        }
    private boolean isTarget(final LivingEntity e) {
        return e != null && Math.sqrt(mc.player.squaredDistanceTo(e)) <= range.getValue();
    }

    private static boolean onGround()
    {
        return mc.world.getBlockState(new BlockPos(mc.player.getX(), mc.player.getY() - 1E-5D, mc.player.getZ())).getMaterial() != Material.AIR || mc.player.isOnGround();
    }

    private boolean itemInHand () {
        final Item item = mc.player.getMainHandStack().getItem();

        switch (items.getMode()) {
            case "Sword":
                return item instanceof SwordItem;

            case "Axe":
                return item instanceof AxeItem;

            case "Pickaxe":
                return item instanceof PickaxeItem;

            case "All":
                return item instanceof SwordItem || item instanceof AxeItem || item instanceof PickaxeItem;

            case "Any":
                return true;

            default:
                return false;
        }
    }
}