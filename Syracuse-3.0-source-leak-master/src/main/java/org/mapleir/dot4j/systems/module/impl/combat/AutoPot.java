package org.mapleir.dot4j.systems.module.impl.combat;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.mapleir.dot4j.gui.setting.NumberSetting;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

@Module.Info(name = "AutoPot", description = "Pots you automatically", category = Category.COMBAT)
public class AutoPot extends Module {

    NumberSetting health = new NumberSetting("Health", 1, 19, 8, 1);
    NumberSetting delay = new NumberSetting("Delay", 2, 20, 10, 1);
    Float prevPitch;
    int potSlot;
    int preSlot;
    int ticksAfterPotion = 0;

    public AutoPot() {
        addSettings(health, delay);
    }

    @Override
    public void onTick() {
        super.onTick();
        if (mc.player.getHealth() <= health.getFloatValue()) {
            if (hotbarContainsPotions()) {
                if (ticksAfterPotion > 0) {
                    ticksAfterPotion++;
                    if (ticksAfterPotion > delay.getValue()) {
                        ticksAfterPotion = 0;
                    }
                    return;
                }
                prevPitch = mc.player.getPitch();

                mc.player.setPitch(90.0f);
                mc.player.getInventory().selectedSlot = potSlot;

                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                ticksAfterPotion++;

                mc.player.setPitch(prevPitch);
                mc.player.getInventory().selectedSlot = preSlot;
            }
        } else {
            preSlot = mc.player.getInventory().selectedSlot;
            ticksAfterPotion = 0;
        }
    }

    public boolean hotbarContainsPotions() {
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.player.getInventory().getStack(i);
            if (itemStack.getItem() == Items.SPLASH_POTION) {
                potSlot = i;
                return true;
            }
        }
        return false;
    }
}
