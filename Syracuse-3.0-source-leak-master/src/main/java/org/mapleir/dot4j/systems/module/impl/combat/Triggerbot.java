package org.mapleir.dot4j.systems.module.impl.combat;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import org.mapleir.dot4j.event.EventTarget;
import org.mapleir.dot4j.event.impl.EventUpdate;
import org.mapleir.dot4j.gui.setting.BooleanSetting;
import org.mapleir.dot4j.gui.setting.ModeSetting;
import org.mapleir.dot4j.gui.setting.NumberSetting;
import org.mapleir.dot4j.helper.utils.EntityUtils;
import org.mapleir.dot4j.helper.utils.MoveHelper;
import org.mapleir.dot4j.helper.utils.PacketHelper;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

import java.util.Random;


@Module.Info(name = "Triggerbot", description = "umm", category = Category.COMBAT)

public class Triggerbot extends Module {

    public ModeSetting mode = new ModeSetting("Mode", "Sword", "Sword", "Axe", "Pickaxe", "Shovel", "Any", "All");
    public NumberSetting hitCooldown = new NumberSetting("Hit cooldown", 0.8, 1.0, 0.9, 0.1);
    public NumberSetting critDistance = new NumberSetting("Crit distance", 0.02, 0.5, 0.3, 0.01);
    public BooleanSetting autoCrit = new BooleanSetting("Smart Sprint", true);
    public BooleanSetting block = new BooleanSetting("Cancel While Blocking", true);
    public BooleanSetting players = new BooleanSetting("players", true);
    public BooleanSetting animals = new BooleanSetting("animals", true);
    public BooleanSetting monsters = new BooleanSetting("monsters", true);
    public BooleanSetting villagers = new BooleanSetting("villagers", true);
    public BooleanSetting invisibles = new BooleanSetting("invisibles", true);

    // TODO: add back after teams has been added back
    //public BooleanSetting teams = new BooleanSetting("Teams (will come)", true);


    private Entity target;

    public void TriggerBot() {
        addSettings(mode, hitCooldown, critDistance, autoCrit, block, players, animals, monsters, villagers, invisibles);
    }

    private static double randomizeCooldown() {
        double min = 0.86;
        double max = 0.99;
        int numIterations = 10;
        double result = 0;

        Random random = new Random();

        for (int i = 0; i < numIterations; i++) {
            double value = min + (max - min) * random.nextDouble();
            result += value;
        }

        result /= numIterations;

        double hitCooldown2 = result;

        return hitCooldown2;
    }

    @EventTarget
    public void onUpdate(final EventUpdate event) {
        target = null;
//        Robot robot = null;
//        try {
//            robot = new Robot();
//        } catch (AWTException e) {
//            throw new RuntimeException(e);
//        }

        //idk why tf this would happen but
        if (mc.player == null) {
            return;
        }

        //check if player is blocking AND it is set to cancel the hitting on block
        if (mc.player.isBlocking() && block.isEnabled()) return;

        //check if player is eating/using a bow/doing something else
        if (mc.player.isUsingItem() && block.isEnabled()) return;
        //check if player is in a container/screen
        if (mc.currentScreen instanceof HandledScreen) return;

        if (this.itemInHand() && mc.player.getAttackCooldownProgress(0.0F) >= hitCooldown.getFloatValue()) {
            // IF NO WORKIES, REPLACE THESE WITH
            //if (mc.crosshairTarget instanceof EntityHitResult) {
            //final EntityHitResult entityResult = ((EntityHitResult) mc.crosshairTarget);
            if (mc.crosshairTarget instanceof final EntityHitResult entityResult) {
                if (!this.isValidEntity(entityResult.getEntity())) {
                    return;
                }
                if (mc.player.isOnGround() || mc.player.fallDistance >= critDistance.getFloatValue() || this.hasFlyUtilities()) {
                    if (autoCrit.isEnabled() && !mc.player.isOnGround() && MoveHelper.hasMovement()) {
                        PacketHelper.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
                    }
                    attack(entityResult.getEntity());

                }
            }
        }
    }

    private void attack(Entity entity) {
        mc.interactionManager.attackEntity(mc.player, entity);
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    private boolean hasFlyUtilities() {
        // TODO: add fly as utility
        return mc.player.getAbilities().flying;
    }

    private boolean isValidEntity(final Entity crossHairTarget) {
        if (crossHairTarget instanceof ClientPlayerEntity) {
            return false;
        }
        // if the crossHairTarget is a player and you haven't selected players as targets (in your settings)
        if (!players.isEnabled() && crossHairTarget instanceof PlayerEntity) {
            return false;
        }
        if (!animals.isEnabled() && EntityUtils.isAnimal(crossHairTarget)) {
            return false;
        }
        if (!monsters.isEnabled() && crossHairTarget instanceof MobEntity) {
            return false;
        }
        if (!villagers.isEnabled() && crossHairTarget instanceof VillagerEntity) {
            return false;
        }
        return invisibles.isEnabled() || (!crossHairTarget.isInvisible() && !crossHairTarget.isInvisibleTo(mc.player));
        //if all checks fail, return true!
    }

    private boolean itemInHand() {
        final Item item = mc.player.getMainHandStack().getItem();

        switch (mode.getMode()) {
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