package net.fabricmc.fabric.systems.module.impl.combat;

import net.fabricmc.fabric.systems.module.core.Category;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.fabricmc.fabric.systems.module.core.Module;


@Module.Info(name = "AutoStun", description = "Breaks shields", category = Category.COMBAT)
public class AutoStun extends Module {

    @Override
    public void onTick() {
        if(mc.player ==null||mc.targetedEntity == null){
            return;
        }
        if(targetUsingSheild()){
            findAxeHotbarSlot(mc.player);
            mc.interactionManager.attackEntity(mc.player,mc.targetedEntity);
            mc.player.swingHand(Hand.MAIN_HAND);
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
    public boolean targetUsingSheild(){
        Entity target = mc.targetedEntity;
        if(target instanceof PlayerEntity && ((PlayerEntity) target).isUsingItem() && ((PlayerEntity) target).getActiveItem().getItem() == Items.SHIELD){
            return true;
        }
        return false;
    }

}
