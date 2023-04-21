package org.mapleir.dot4j.gui.clickgui.components;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import org.mapleir.dot4j.gui.clickgui.ClickGUI;
import org.mapleir.dot4j.gui.setting.BooleanSetting;
import org.mapleir.dot4j.gui.setting.ModeSetting;
import org.mapleir.dot4j.gui.setting.NumberSetting;
import org.mapleir.dot4j.gui.setting.Setting;
import org.mapleir.dot4j.helper.utils.Animation;
import org.mapleir.dot4j.helper.utils.RenderUtils;
import org.mapleir.dot4j.helper.utils.Theme;
import org.mapleir.dot4j.systems.module.core.Module;
import org.mapleir.dot4j.systems.module.core.ModuleManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModButton {

    private final Module module;
    private final ClickGUI parent;
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private final List<Component> components = new ArrayList<>();
    private final Animation toggleAnim;
    private double x, y;

    public ModButton(Module module, double x, double y, ClickGUI parent) {
        this.module = module;
        this.y = y;
        this.x = x;
        this.parent = parent;
        toggleAnim = new Animation(0, 10);
    }

    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {

        x = parent.settingsFieldX;

        if (isHovered(parent.windowX + (float) x + 100 + parent.coordModX, (float) (parent.windowY + y - 10), parent.windowX + (float) x + 425 + parent.coordModX, (float) (parent.windowY + y + 25), mouseX, mouseY)) {
            if (module.isEnabled()) {
                RenderUtils.renderRoundedQuad(matrices, parent.windowX + (float) x + 100 + parent.coordModX, parent.windowY + y - 10, parent.windowX + (float) x + 425 + parent.coordModX, parent.windowY + y + 25, 5, 20, Theme.MODULE_ENABLED_BG_HOVER);
            } else {
                RenderUtils.renderRoundedQuad(matrices, parent.windowX + (float) x + 100 + parent.coordModX, parent.windowY + y - 10, parent.windowX + (float) x + 425 + parent.coordModX, parent.windowY + y + 25, 5, 20, Theme.MODULE_DISABLED_BG_HOVER);
            }
        } else {
            if (module.isEnabled()) {
                RenderUtils.renderRoundedQuad(matrices, parent.windowX + (float) x + 100 + parent.coordModX, parent.windowY + y - 10, parent.windowX + (float) x + 425 + parent.coordModX, parent.windowY + y + 25, 5, 20, Theme.MODULE_ENABLED_BG);
            } else {
                RenderUtils.renderRoundedQuad(matrices, parent.windowX + (float) x + 100 + parent.coordModX, parent.windowY + y - 10, parent.windowX + (float) x + 425 + parent.coordModX, parent.windowY + y + 25, 5, 20, Theme.MODULE_DISABLED_BG);
            }
        }

        RenderUtils.renderRoundedQuad(matrices, parent.windowX + (float) x + 100 + parent.coordModX, parent.windowY + y - 10, parent.windowX + (float) x + 125 + parent.coordModX, parent.windowY + y + 25, 5, 20, Theme.MODULE_COLOR);
        RenderUtils.renderRoundedQuad(matrices, parent.windowX + (float) x + 410 + parent.coordModX, parent.windowY + y - 10, parent.windowX + (float) x + 425 + parent.coordModX, parent.windowY + y + 25, 5, 20, Theme.SETTINGS_HEADER);
        mc.textRenderer.draw(matrices, ".", parent.windowX + (float) x + 416 + parent.coordModX, (float) (parent.windowY + y - 5), Theme.MODULE_TEXT.getRGB());
        mc.textRenderer.draw(matrices, ".", parent.windowX + (float) x + 416 + parent.coordModX, (float) (parent.windowY + y - 1), Theme.MODULE_TEXT.getRGB());
        mc.textRenderer.draw(matrices, ".", parent.windowX + (float) x + 416 + parent.coordModX, (float) (parent.windowY + y + 3), Theme.MODULE_TEXT.getRGB());

        // bobs the description forth a bit
        // TODO: align it to the module name length
        if (isHovered(parent.windowX + (float) x + 100 + parent.coordModX, parent.windowY + (float) y - 10, parent.windowX + (float) x + 425 + parent.coordModX, (float) (parent.windowY + y + 25), mouseX, mouseY)) {
            mc.textRenderer.draw(matrices, module.getDescription() + ".", parent.windowX + (float) x + 225 + parent.coordModX, parent.windowY + (float) (y + 5), Theme.MODULE_TEXT.getRGB());
        } else {
            mc.textRenderer.draw(matrices, module.getDescription() + ".", parent.windowX + (float) x + 220 + parent.coordModX, parent.windowY + (float) (y + 5), Theme.MODULE_TEXT.getRGB());
        }

        // updates the animation value
        toggleAnim.update();

        if (module.isEnabled()) {
            // draws teh modujle name
            mc.textRenderer.draw(matrices, module.getName(), parent.windowX + (float) x + 140 + parent.coordModX, (float) (parent.windowY + y + 5), Theme.NORMAL_TEXT_COLOR.getRGB());
            RenderUtils.renderRoundedQuad(matrices, parent.windowX + (float) x + 100 + parent.coordModX, parent.windowY + y - 10, parent.windowX + (float) x + 125 + parent.coordModX, parent.windowY + y + 25, 5, 20, new Color(41, 117, 221, (int) ((toggleAnim.getValue() * 10) / 100f * 255)));

            toggleAnim.setEnd(10);

            RenderUtils.renderRoundedQuad(matrices, parent.windowX + (float) x + 380 + parent.coordModX, parent.windowY + y + 2, parent.windowX + (float) x + 400 + parent.coordModX, parent.windowY + y + 12, 4, 5, new Color(33, 94, 181, (int) ((toggleAnim.getValue() * 10) / 100f * 255)));
            RenderUtils.drawCircle(matrices, parent.windowX + x + parent.coordModX + 385 + toggleAnim.getValue(), parent.windowY + y + 7, 3.5, 20, Theme.NORMAL_TEXT_COLOR.getRGB());
        } else {
            mc.textRenderer.draw(matrices, module.getName(), parent.windowX + (float) x + 140 + parent.coordModX, parent.windowY + (float) (y + 5), Theme.MODULE_TEXT.getRGB());

            toggleAnim.setEnd(0);

            RenderUtils.renderRoundedQuad(matrices, parent.windowX + (float) x + 380 + parent.coordModX, parent.windowY + y + 2, parent.windowX + (float) x + 400 + parent.coordModX, parent.windowY + y + 12, 4, 5, Theme.TOGGLE_BUTTON_BG);
            RenderUtils.renderRoundedQuad(matrices, parent.windowX + (float) x + 381 + parent.coordModX, parent.windowY + y + 3, parent.windowX + (float) x + 399 + parent.coordModX, parent.windowY + y + 11, 3, 20, Theme.TOGGLE_BUTTON_FILL);
            RenderUtils.drawCircle(matrices, parent.windowX + x + parent.coordModX + 385 + toggleAnim.getValue(), parent.windowY + y + 7, 3.5, 20, Theme.TOGGLE_BUTTON_BG);
        }

        // update y of comp valuies
        float settingsY = parent.windowY + 100 + parent.settingsFNow;
        for (Component component : components) {
            if (component instanceof CheckBox cb) cb.setY(settingsY);
            if (component instanceof Slider s) s.setY(settingsY);
            if (component instanceof ModeComp m) m.setY(settingsY);
            settingsY += 25;
        }

        // only draw components when the module is expanded
        if (module.isExpanded()) components.forEach(c -> c.drawScreen(matrices, mouseX, mouseY, delta));
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        // toggles the module
        if (isHovered(parent.windowX + (float) x + 100 + parent.coordModX, parent.windowY + (float) y - 10, parent.windowX + (float) x + 425 + parent.coordModX, parent.windowY + (float) y + 25, mouseX, mouseY) && button == 0) {
            module.toggle();
        }
        // show settings
        else if (isHovered(parent.windowX + (float) x + 100 + parent.coordModX, parent.windowY + (float) y - 10, parent.windowX + (float) x + 425 + parent.coordModX, parent.windowY + (float) y + 25, mouseX, mouseY) && button == 1) {

            if (parent.selectedModule != null && parent.selectedModule != module) {
                parent.selectedModule = null;
                for (Module module : ModuleManager.INSTANCE.getModulesByCategory(parent.selectedCategory))
                    module.setExpanded(false);
            }
            if (parent.selectedModule != module) {
                parent.settingsFieldX = 0;
                parent.selectedModule = module;

                components.clear();
                for (Module module : ModuleManager.INSTANCE.getModulesByCategory(parent.selectedCategory))
                    if (module != this.module) module.setExpanded(false);

                module.setExpanded(true);

                float settingsY = parent.windowY + 100 + parent.settingsFNow;
                for (Setting setting : module.getSettings()) {
                    if (setting instanceof BooleanSetting booleanSetting) {
                        components.add(new CheckBox(booleanSetting, parent, 0, settingsY));
                    } else if (setting instanceof NumberSetting numberSetting) {
                        components.add(new Slider(numberSetting, 0, settingsY, parent));
                    } else if (setting instanceof ModeSetting modeSetting) {
                        components.add(new ModeComp(modeSetting, parent, 0, settingsY));
                    }
                    settingsY += 25;
                }
            } else if (parent.selectedModule == module) {
                parent.selectedModule = null;
                for (Module module : ModuleManager.INSTANCE.getModulesByCategory(parent.selectedCategory))
                    module.setExpanded(false);
            }
        }

        if (module.isExpanded()) components.forEach(c -> c.mouseClicked(mouseX, mouseY, button));
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (module.isExpanded()) components.forEach(c -> c.mouseReleased(mouseX, mouseY, button));
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (module.isExpanded()) {
            components.forEach(c -> c.keyPressed(keyCode, scanCode, modifiers));
        }
    }

    public void charTyped(char chr, int modifiers) {
        if (module.isExpanded()) {
            components.forEach(c -> c.charTyped(chr, modifiers));
        }
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    private boolean isHovered(float x, float y, float x2, float y2, double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    public Module getModule() {
        return module;
    }
}
