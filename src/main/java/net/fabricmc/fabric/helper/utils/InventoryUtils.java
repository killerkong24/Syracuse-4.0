package net.fabricmc.fabric.helper.utils;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

import static net.fabricmc.fabric.helper.utils.SyraMC.mc;

public class InventoryUtils {
    public static int previousSlot = -1;

    public static final int HOTBAR_START = 0;
    public static final int HOTBAR_END = 8;

    public static final int OFFHAND = 45;

    public static final int MAIN_START = 9;
    public static final int MAIN_END = 35;

    public static final int ARMOR_START = 36;
    public static final int ARMOR_END = 39;
    public static boolean selectItemFromHotbar(Predicate<Item> item) {
        final PlayerInventory inv = mc.player.getInventory();

        for (int i = 0; i < 9; i++) {
            final ItemStack itemStack = inv.getStack(i);
            if (!item.test(itemStack.getItem()))
                continue;
            inv.selectedSlot = i;
            return true;
        }

        return false;
    }

    public static boolean selectItemFromHotbar(Item item) {
        return selectItemFromHotbar(i -> i == item);
    }

    public static boolean hasItemInHotbar(Predicate<Item> item) {
        final PlayerInventory inv = mc.player.getInventory();

        for (int i = 0; i < 9; i++) {
            final ItemStack itemStack = inv.getStack(i);
            if (item.test(itemStack.getItem()))
                return true;
        }
        return false;
    }

    public static int countItem(Predicate<Item> item) {
        final PlayerInventory inv = mc.player.getInventory();

        int count = 0;

        for (int i = 0; i < 36; i++) {
            final ItemStack itemStack = inv.getStack(i);
            if (item.test(itemStack.getItem()))
                count += itemStack.getCount();
        }

        return count;
    }

    public static int countItem(Item item) {
        return countItem(i -> i == item);
    }
}
