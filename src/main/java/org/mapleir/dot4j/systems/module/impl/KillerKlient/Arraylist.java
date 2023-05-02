package org.mapleir.dot4j.systems.module.impl.KillerKlient;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import org.mapleir.dot4j.gui.setting.BooleanSetting;
import org.mapleir.dot4j.gui.setting.ModeSetting;
import org.mapleir.dot4j.gui.setting.NumberSetting;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;
import org.mapleir.dot4j.systems.module.core.ModuleManager;

import java.util.Comparator;
import java.util.List;

@Module.Info(name = "Arraylist", description = "Enabled modules", category = Category.KILLERKLIENT)

public class Arraylist extends Module {
    public BooleanSetting show = new BooleanSetting("Enabled", true);
    public ModeSetting mode = new ModeSetting("Mode", "Asolfo", "Asolfo", "White");
    public NumberSetting rainbowspeed = new NumberSetting("Rainbow speed", 1, 8, 4, 1);

    public Arraylist() {
        addSettings(show, mode, rainbowspeed);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void draw(MatrixStack matrices) {
        int xOffset = -5;
        int yOffset = 5;

        int index = 0;
        int sWidth = mc.getWindow().getScaledWidth();

        List<Module> enabled = ModuleManager.INSTANCE.getEnabledModules();
        enabled.sort(Comparator.comparingInt(m -> mc.textRenderer.getWidth(((Module) m).getDisplayName())).reversed());

        for (Module mod : enabled) {
            int fWidth = mc.textRenderer.getWidth(mod.getDisplayName());
            int lastWidth;
            int fHeight = mc.textRenderer.fontHeight;
            int fromX = sWidth - fWidth - 5;
            int fromY = (fHeight - 1) * (index) + 1;
            int toX = sWidth - 2;
            int toY = (fHeight - 1) * (index) + fHeight;

            if (index - 1 >= 0) lastWidth = mc.textRenderer.getWidth(enabled.get(index - 1).getDisplayName());
            else lastWidth = sWidth;

            DrawableHelper.fill(matrices, fromX + xOffset, fromY + 1 + yOffset, sWidth + xOffset, toY + 1 + yOffset, 0x60000000);


            DrawableHelper.fill(matrices, fromX + xOffset, fromY + 1 + yOffset, fromX - 1 + xOffset, toY + 1 + yOffset, -1);
            DrawableHelper.fill(matrices, toX + 2 + xOffset, fromY + 1 + yOffset, toX + 1 + xOffset, toY + 1 + yOffset, -1);

            if (index == enabled.size() - 1) {
                DrawableHelper.fill(matrices, fromX + xOffset, toY + yOffset, sWidth + xOffset, toY + 1 + yOffset, -1);
            }
            if (index == 0) {
                DrawableHelper.fill(matrices, fromX - 1 + xOffset, fromY + yOffset, sWidth + xOffset, fromY + 1 + yOffset, -1);
            } else {
                DrawableHelper.fill(matrices, fromX + xOffset, fromY + yOffset, toX - lastWidth - 4 + xOffset, fromY + 1 + yOffset, -1);
            }

            mc.textRenderer.drawWithShadow(matrices, mod.getDisplayName(), sWidth - fWidth + xOffset, (fHeight - 1) * (index) + yOffset, -1);
            index++;
        }
    }
}

