package net.fabricmc.fabric.systems.module.impl.KillerKlient;

import net.fabricmc.fabric.ClientMain;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.systems.module.core.ModuleManager;

import java.util.List;

@Module.Info(name = "Arraylist", description = "Enabled modules", category = Category.KILLERKLIENT)

public class Arraylist extends HudModule {
    public BooleanSetting show = new BooleanSetting("Enabled", true);
    public ModeSetting mode = new ModeSetting("Mode", "Asolfo", "Asolfo", "White");
    public NumberSetting rainbowspeed = new NumberSetting("Rainbow speed", 1, 8, 4, 1);

    public Arraylist() {
        super("Arraylist", "Shows a list of enabled Modules", Category.RENDER,0,0,0,0);
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
    public void draw(DrawContext context) {

        MatrixStack matrices = context.getMatrices();

        List<Module> enabledModules = ModuleManager.INSTANCE.getEnabledModules();
        // Sort
        enabledModules.sort((m1, m2) -> mc.textRenderer.getWidth(m2.getDisplayName()) - mc.textRenderer.getWidth(m1.getDisplayName()));

        boolean leftHalf = getX() < (mc.getWindow().getScaledWidth() / 2);
        boolean rightHalf = getX() > (mc.getWindow().getScaledWidth() / 2);

        setWidth(mc.textRenderer.getWidth(enabledModules.get(0).getDisplayName()));

        if((getX() + getWidth()) > mc.getWindow().getScaledWidth()) {
            setX(mc.getWindow().getScaledWidth() - (getWidth()));
        }

        int offset = 0;
        for(Module module : enabledModules) {
            if(leftHalf) {
                ClientMain.getFontRenderer().drawString(matrices, module.getDisplayName(), getX(), getY() + offset, -1);
            }
            else if(rightHalf) {
                ClientMain.getFontRenderer().drawString(matrices, module.getDisplayName(), getX() + getWidth() - mc.textRenderer.getWidth(module.getDisplayName()), getY() + offset, -1);
            }
            offset += mc.textRenderer.fontHeight + 2;
        }

        setHeight(offset);
    }
}

