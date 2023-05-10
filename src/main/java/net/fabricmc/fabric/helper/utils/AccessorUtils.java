package net.fabricmc.fabric.helper.utils;

import net.fabricmc.fabric.mixin.MixinConfigClientSide;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

public class AccessorUtils {
    @Nullable
    public static Slot getSlotUnderMouse(HandledScreen<?> gui) {
        return ((MixinConfigClientSide)gui).itemscroller_getHoveredSlot();
    }
}
