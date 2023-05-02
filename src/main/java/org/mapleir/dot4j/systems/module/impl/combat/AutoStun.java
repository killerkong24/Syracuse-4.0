package org.mapleir.dot4j.systems.module.impl.combat;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

@Module.Info(name = "AutoStun", description = "Breaks shields", category = Category.COMBAT)
public class AutoStun extends Module {

    @Override
    public void onTick() {
        PlayerEntity player = mc.player;
        ItemStack heldItem = player.getMainHandStack();

        if (heldItem.getItem() instanceof AxeItem) {
            if (player.isUsingItem() && player.getActiveItem().getItem() instanceof ShieldItem) {
                int axeSlot = findAxeHotbarSlot(player);
                if (axeSlot >= 0) {
                    player.getInventory().selectedSlot = axeSlot;
                    while (player.isUsingItem() && player.getActiveItem().getItem() instanceof ShieldItem) {
                        player.attack(player.getAttacker());
                    }
                }
            }
        }

    }

    private int findAxeHotbarSlot(PlayerEntity player) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.getItem() instanceof AxeItem) {
                return i;
            }
        }
        return -1;
    }

}
