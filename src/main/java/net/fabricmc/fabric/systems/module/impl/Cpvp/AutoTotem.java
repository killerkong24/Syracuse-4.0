package net.fabricmc.fabric.systems.module.impl.Cpvp;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;

@Module.Info(name = "AutoTotem", description = "anti dier", category = Category.CPVP)
public class AutoTotem extends Module {

    private final ModeSetting mode = new ModeSetting("mode","fast totem", "inv totem","fast totem");
    private final NumberSetting delay = new NumberSetting("delay",0, 20,0,1);
    private final NumberSetting totemSlot = new NumberSetting("totem slot",0, 8,8,1);
    public BooleanSetting autoSwitch = new BooleanSetting("Auto Switch", false);
    private int nextTickSlot;
    private int totems;


    private int clock;

    public AutoTotem() {
        addSettings(mode,delay,totemSlot,autoSwitch);
    }

    @Override
    public void onEnable(){
        super.onEnable();
        clock = -1;
    }

    @Override
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

            if (autoSwitch.isEnabled())
                inventory.selectedSlot = (int) totemSlot.getValue();
        }

        ///inv
        if (mode.isMode("inv totem")) {
            PlayerInventory inventory = mc.player.getInventory();
            ItemStack offhandStack = inventory.getStack(40);

            int nextTotemSlot = searchForTotems(inventory);
            if (!(mc.currentScreen instanceof InventoryScreen))
            {
                clock = -1;
                return;
            }
            if (clock == -1)
                clock = (int) delay.getValue();
            if (clock > 0)
            {
                clock--;
                return;
            }
            PlayerInventory inv = mc.player.getInventory();

            if (autoSwitch.isEnabled()) {
                inv.selectedSlot = (int) totemSlot.getValue();
            }
            if (inv.offHand.get(0).getItem() != Items.TOTEM_OF_UNDYING) {
                int slot = nextTotemSlot;
                if (slot != -1) {
                    mc.interactionManager.clickSlot(((InventoryScreen) mc.currentScreen).getScreenHandler().syncId, slot, 40, SlotActionType.SWAP, mc.player);
                    return;
                }
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

    private boolean isTotem(ItemStack stack)
    {
        return stack.getItem() == Items.TOTEM_OF_UNDYING;
    }
}
