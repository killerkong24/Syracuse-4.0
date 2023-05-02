package org.mapleir.dot4j.systems.module.impl.movement;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.mapleir.dot4j.gui.setting.BooleanSetting;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

@Module.Info(name = "InventoryMove", description = "Move freely", category = Category.MOVEMENT)
public class InventoryMove extends Module {

    BooleanSetting inventory = new BooleanSetting("Inventory", true);
    BooleanSetting shift = new BooleanSetting("Shift", true);

    public InventoryMove() {
        addSettings(inventory, shift);
    }

    @Override
    public void onTick() {
        if (inventory.isEnabled()
                && mc.currentScreen != null
                && !(mc.currentScreen instanceof ChatScreen)
                && !(mc.currentScreen instanceof SignEditScreen)
                && !(mc.currentScreen instanceof BookScreen)) {
            for (KeyBinding k : new KeyBinding[]{mc.options.forwardKey, mc.options.backKey,
                    mc.options.leftKey, mc.options.rightKey, mc.options.jumpKey, mc.options.sprintKey}) {
                k.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(),
                        InputUtil.fromTranslationKey(k.getBoundKeyTranslationKey()).getCode()));
            }
            if (shift.isEnabled()) mc.options.sneakKey.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(),
                    InputUtil.fromTranslationKey(mc.options.sneakKey.getBoundKeyTranslationKey()).getCode()));
        }
    }

}
