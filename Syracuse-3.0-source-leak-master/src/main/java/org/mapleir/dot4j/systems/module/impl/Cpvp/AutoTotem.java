package org.mapleir.dot4j.systems.module.impl.Cpvp;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.mapleir.dot4j.event.EventTarget;
import org.mapleir.dot4j.event.impl.EventUpdate;
import org.mapleir.dot4j.gui.setting.BooleanSetting;
import org.mapleir.dot4j.gui.setting.ModeSetting;
import org.mapleir.dot4j.gui.setting.NumberSetting;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

@Module.Info(name = "AutoTotem", description = "anti dier", category = Category.CPVP)
public class AutoTotem extends Module {

    private final ModeSetting mode = new ModeSetting("mode","fast totem", "inv totem");
    private final NumberSetting delay = new NumberSetting("delay",0, 20,0,1);
    private final NumberSetting totemSlot = new NumberSetting("totem slot",1, 9,9,1);
    private final BooleanSetting hotBarRestock = new BooleanSetting("hotbar Restock", false);

    private int nextTickSlot;
    private int totems;


    private int clock;
    public AutoTotem() {
        addSettings(mode,delay,totemSlot,hotBarRestock);
    }

    @Override
    public void onEnable(){
        super.onEnable();
        clock = -1;
    }

    @EventTarget
    public void onTick() {
        //fast totem
        if (mode.isMode("fast totem")) {
            finishMovingTotem();

            PlayerInventory inventory = mc.player.getInventory();
            int nextTotemSlot = searchForTotems(inventory);

            ItemStack offhandStack = inventory.getStack(40);
            if (isTotem(offhandStack)) {
                totems++;
                return;
            }

            if (mc.currentScreen instanceof HandledScreen
                    && !(mc.currentScreen instanceof AbstractInventoryScreen))
                return;

            if (nextTotemSlot != -1)
                moveTotem(nextTotemSlot, offhandStack);

            if (hotBarRestock.isEnabled()) {
                mc.interactionManager.clickSlot(0, nextTotemSlot, 0, SlotActionType.PICKUP, mc.player);
                mc.interactionManager.clickSlot(0, (int) totemSlot.getValue(), 0, SlotActionType.PICKUP, mc.player);
            }
        }

        ///inv
        if (mode.isMode("bad totem")) {
            PlayerInventory inventory = mc.player.getInventory();
            ItemStack offhand = inventory.getStack(45);
            int nextTotemSlot = searchForTotems(inventory);
            if (mc.currentScreen instanceof InventoryScreen) {
                if (clock > -1){
                    clock--;
                    return;
                }
                if (!isTotem(offhand)) {
                    mc.interactionManager.clickSlot(0, nextTotemSlot, 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(0, 45, 0, SlotActionType.PICKUP, mc.player);
                }
                if (hotBarRestock.isEnabled()) {
                    ItemStack totemslot = inventory.getStack((int) totemSlot.getValue());
                    mc.interactionManager.clickSlot(0, searchinvForTotems(), 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(0, (int) totemSlot.getValue() + 35, 0, SlotActionType.PICKUP, mc.player);
                }
            }
            if (!(mc.currentScreen instanceof InventoryScreen)){
                clock = (int) delay.getValue();
            }
        }
    }

    private void moveTotem(int nextTotemSlot, ItemStack offhandStack)
    {
        boolean offhandEmpty = offhandStack.isEmpty();

        mc.interactionManager.clickSlot(0, nextTotemSlot, 0, SlotActionType.PICKUP, mc.player);
        mc.interactionManager.clickSlot(0, 45, 0, SlotActionType.PICKUP, mc.player);

        if(!offhandEmpty)
            nextTickSlot = nextTotemSlot;
    }

    private void finishMovingTotem()
    {
        if(nextTickSlot == -1)
            return;

        mc.interactionManager.clickSlot(0, nextTickSlot, 0, SlotActionType.PICKUP, mc.player);
        nextTickSlot = -1;
    }

    private int searchForTotems(PlayerInventory inventory)
    {
        totems = 0;
        int nextTotemSlot = -1;

        for(int slot = 0; slot <= 36; slot++)
        {
            if(!isTotem(inventory.getStack(slot)))
                continue;

            totems++;

            if(nextTotemSlot == -1)
                nextTotemSlot = slot < 9 ? slot + 36 : slot;
        }

        return nextTotemSlot;
    }
    private int searchinvForTotems()
    {
        PlayerInventory inv = mc.player.getInventory();
        for (int i = 9; i < 36; i++)
        {
            if (inv.main.get(i).getItem() == Items.TOTEM_OF_UNDYING)
                return i;
        }
        return -1;
    }

    private boolean isTotem(ItemStack stack)
    {
        return stack.getItem() == Items.TOTEM_OF_UNDYING;
    }
}
