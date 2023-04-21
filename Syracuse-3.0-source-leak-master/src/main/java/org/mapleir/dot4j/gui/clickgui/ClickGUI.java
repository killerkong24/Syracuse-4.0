package org.mapleir.dot4j.gui.clickgui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.mapleir.dot4j.ClientMain;
import org.mapleir.dot4j.gui.clickgui.components.ModButton;
import org.mapleir.dot4j.helper.utils.Animation;
import org.mapleir.dot4j.helper.utils.RenderUtils;
import org.mapleir.dot4j.helper.utils.Theme;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;
import org.mapleir.dot4j.systems.module.core.ModuleManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Screen {

    private static final ClickGUI INSTANCE = new ClickGUI();
    public final List<ModButton> modButtons = new ArrayList<>();
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private final boolean close = false;
    public float windowX = 200, windowY = 200;
    public float width = 500, height = 310;
    public Category selectedCategory = Category.COMBAT;
    public Module selectedModule;
    // values for anim
    public float lastPercent;
    public float percent;
    public float percent2;
    public float lastPercent2;
    public float outro;
    public float lastOutro;
    public int coordModX = 0;
    public float settingsFieldX;
    public float settingsFNow, settingsF;
    private float dragX, dragY;
    private boolean drag = false;
    private boolean closed;
    private double dWheel;
    private float hy = windowY + 40;
    private float modScrollEnd, modScrollNow;

    private ViewType viewType;

    private ConfigScreen configScreen;

    private Animation transition;

    protected ClickGUI() {
        super(Text.of("klick guei"));
    }

    public static ClickGUI getINSTANCE() {
        return INSTANCE;
    }

    @Override
    protected void init() {
        // inits the open animation values
        percent = 1.33f;
        lastPercent = 1f;
        percent2 = 1.33f;
        lastPercent2 = 1f;
        outro = 1;
        lastOutro = 1;

        // stops dragging when reopening
        drag = false;

        // unselects the module
        selectedModule = null;
        // clear list of buttons
        modButtons.clear();

        // sets new buttons
        float modY = 70 + modScrollNow;
        for (Module module : ModuleManager.INSTANCE.getModulesByCategory(selectedCategory)) {
            modButtons.add(new ModButton(module, 0, modY, this));
            modY += 40;
        }

        viewType = ViewType.HOME;
        configScreen = new ConfigScreen(this);

        super.init();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);

        // get outro value
        float outro = smoothTrans(this.outro, lastOutro);
        // check if user is ingame
        if (mc.currentScreen == null) {
            matrices.translate(mc.getWindow().getScaledWidth() / 2, mc.getWindow().getScaledHeight() / 2, 0);
            matrices.scale(outro, outro, 0);
            matrices.translate(-mc.getWindow().getScaledWidth() / 2, -mc.getWindow().getScaledHeight() / 2, 0);
        }

        // get percent values
        percent = smoothTrans(this.percent, lastPercent);
        percent2 = smoothTrans(this.percent2, lastPercent2);

        if (percent > 0.98) {
            // scale window
            matrices.translate(mc.getWindow().getScaledWidth() / 2, mc.getWindow().getScaledHeight() / 2, 0);
            matrices.scale(percent, percent, 0);
            matrices.translate(-mc.getWindow().getScaledWidth() / 2, -mc.getWindow().getScaledHeight() / 2, 0);
        } else {
            if (percent2 <= 1) {
                matrices.translate(mc.getWindow().getScaledWidth() / 2, mc.getWindow().getScaledHeight() / 2, 0);
                matrices.scale(percent2, percent2, 0);
                matrices.translate(-mc.getWindow().getScaledWidth() / 2, -mc.getWindow().getScaledHeight() / 2, 0);
            }
        }

        if (percent <= 1.5 && close) {
            percent = smoothTrans(this.percent, 2);
            percent2 = smoothTrans(this.percent2, 2);
        }

        if (percent >= 1.4 && close) {
            percent = 1.5f;
            closed = true;
            // set ingame focus
            mc.currentScreen = null;
        }

        // drags the window
        if (drag) {
            // update window pos
            if (dragX == 0 && dragY == 0) {
                dragX = mouseX - windowX;
                dragY = mouseY - windowY;
            } else {
                windowX = mouseX - dragX;
                windowY = mouseY - dragY;
            }
        }
        // prevents some issues
        else if (dragX != 0 || dragY != 0) {
            dragX = 0;
            dragY = 0;
        }

        // draws the window corner radius
        RenderUtils.renderRoundedQuad(matrices, windowX, windowY, windowX + width, windowY + height, 25, 20, Theme.WINDOW_COLOR);

        matrices.push();
        matrices.scale(2, 2, 0);
        mc.textRenderer.draw(matrices, ClientMain.getName(), (windowX + 20) / 2, (windowY + 20) / 2, -1);
        matrices.pop();

        if (viewType == ViewType.CONFIG) {

            if (transition != null) {
                transition.update(true);
                RenderUtils.fill(matrices, transition.getValue(), windowY, transition.getEnd() - 19, windowY + height, Theme.WINDOW_COLOR.getRGB());
                if (transition.hasEnded()) transition = null;
            }

            configScreen.drawScreen(matrices, mouseX, mouseY, delta);

            // back button
            if (isHovered(windowX + 20, windowY + height - 50, windowX + 20 + 32, windowY + height - 50 + 32, mouseX, mouseY) && configScreen.editConfigMenu == null) {
                matrices.push();
                RenderUtils.drawTexturedRectangle(matrices, windowX + 20, windowY + height - 50, "textures/back.png");
                matrices.pop();
            } else {
                matrices.push();
                RenderSystem.setShader(GameRenderer::getPositionColorProgram);
                RenderSystem.setShaderColor(0.25f, 0.25f, 0.25f, 1f);
                RenderUtils.drawTexturedRectangle(matrices, windowX + 20, windowY + height - 50, "textures/back.png");
                RenderSystem.clearColor(1f, 1f, 1f, 1f);
                matrices.pop();
            }

            return;
        }

        Window window = mc.getWindow();
        // draw the categories
        RenderSystem.enableScissor((int) (windowX + 5) * 2, (int) (-windowY + (height / 2) + 55) * 2, window.getScaledWidth() + 40, window.getScaledHeight() - 10);

        // easier to see where is being scissored
        //RenderUtils.fill(matrices.peek().getPositionMatrix(), window.getScaledWidth(), window.getScaledHeight(), -window.getScaledWidth(), -window.getScaledHeight(), -1);

        // only show categories when no mod is expanded
        if (selectedModule == null) {

            // show the cfg icon
            if (isHovered(windowX + 20, windowY + height - 50, windowX + 20 + 32, windowY + height - 50 + 32, mouseX, mouseY)) {
                matrices.push();
                RenderUtils.drawTexturedRectangle(matrices, windowX + 20, windowY + height - 50, "textures/config.png");
                matrices.pop();
            } else {
                matrices.push();
                RenderSystem.setShader(GameRenderer::getPositionColorProgram);
                RenderSystem.setShaderColor(0.25f, 0.25f, 0.25f, 1f);
                RenderUtils.drawTexturedRectangle(matrices, windowX + 20, windowY + height - 50, "textures/config.png");
                matrices.pop();
            }

            // cateogry room between
            float cateY = windowY + 65;

            for (Category category : Category.values()) {
                if (category == selectedCategory) {
                    mc.textRenderer.draw(matrices, category.name(), windowX + 20, cateY, -1);
                    // draws the category underline
                    RenderUtils.renderRoundedQuad(matrices, windowX + 20, hy + mc.textRenderer.fontHeight + 2, windowX + 30, hy + mc.textRenderer.fontHeight + 4, 1, 20, Theme.ENABLED);

                    // updates the underline position
                    if (hy != cateY) {
                        hy += (cateY - hy) / 20;
                    }
                } else {
                    mc.textRenderer.draw(matrices, category.name(), windowX + 20, cateY, Theme.UNFOCUSED_TEXT_COLOR.getRGB());
                }

                cateY += 25;
            }
        }

        // updates the setting values
        if (selectedModule != null) {
            if (settingsFieldX > -80) {
                settingsFieldX -= 5;
            }
        } else {
            if (settingsFieldX < 0) {
                settingsFieldX += 5;
            }
        }

        if (selectedModule != null) {
            // draws field

            RenderUtils.renderRoundedQuad(matrices, windowX + 430 + settingsFieldX, windowY + 60, windowX + width, windowY + height - 20, 5, 20, Theme.SETTINGS_BG);
            RenderUtils.renderRoundedQuad(matrices, windowX + 430 + settingsFieldX, windowY + 60, windowX + width, windowY + 85, 5, 20, Theme.SETTINGS_HEADER);

            mc.textRenderer.draw(matrices, selectedModule.getName(), windowX + 450 + settingsFieldX, windowY + 70, -1);

            // settings scroll
            if (isHovered(windowX + 430 + (int) settingsFieldX, windowY + 60, windowX + width, windowY + height - 20, mouseX, mouseY)) {
                if (dWheel < 0 && Math.abs(settingsF) + 170 < (selectedModule.getSettings().size() * 25)) {
                    settingsF -= 32;
                }
                if (dWheel > 0 && settingsF < 0) {
                    settingsF += 32;
                }
            }

            // updates setting animation values
            if (settingsFNow != settingsF) {
                settingsFNow += (settingsF - settingsFNow) / 20;
                settingsFNow = (int) settingsFNow;
            }
        }

        // scroll
        if (isHovered(windowX + 100 + settingsFieldX, windowY + 60, windowX + 425 + settingsFieldX, windowY + height, mouseX, mouseY)) {
            if (dWheel < 0 && Math.abs(modScrollEnd) + 220 < (ModuleManager.INSTANCE.getModulesByCategory(selectedCategory).size() * 40)) {
                modScrollEnd -= 25;
            }
            if (dWheel > 0 && modScrollEnd < 0) {
                modScrollEnd += 25;
            }
        }

        // smoother scroll uwu
        if (modScrollNow != modScrollEnd) {
            modScrollNow += (modScrollEnd - modScrollNow) / 20;
            modScrollNow = (int) modScrollNow;
        }

        // draws the module buttons
        float modY = 70 + modScrollNow;
        for (ModButton modButton : modButtons) {
            if (ModuleManager.INSTANCE.getModulesByCategory(selectedCategory).contains(modButton.getModule())) {
                modButton.setY(modY);
                modButton.drawScreen(matrices, mouseX, mouseY, delta);
                modY += 40;
            }
        }
        RenderSystem.disableScissor();

        if (transition != null) {
            transition.update(true);
            RenderUtils.fill(matrices, transition.getValue(), windowY, transition.getEnd() - 19, windowY + height, Theme.WINDOW_COLOR.getRGB());
            if (transition.hasEnded()) transition = null;
        }
    }

    // smoothly transitions between 2 values
    public float smoothTrans(double current, double last) {
        return (float) (current + (last - current) / (MinecraftClient.getInstance().getCurrentFps() / 10));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        // drgas  widow
        if (isHovered(windowX, windowY, windowX + width, windowY + 20, mouseX, mouseY) && button == 0) {
            drag = true;
        }

        // config icon                                                                                                                                                Null check so u can't leave when editing a config
        if (isHovered(windowX + 20, windowY + height - 50, windowX + 20 + 32, windowY + height - 50 + 32, mouseX, mouseY) && button == 0 && selectedModule == null && configScreen.editConfigMenu == null) {
            transition = new Animation((int) (windowX - 20), (int) (windowX + width + 20));
            if (viewType == ViewType.CONFIG) {
                viewType = ViewType.HOME;
            } else viewType = ViewType.CONFIG;
        }

        if (viewType == ViewType.CONFIG) {
            configScreen.mouseClicked(mouseX, mouseY, button);
            return super.mouseClicked(mouseX, mouseY, button);
        }

        // when cateogry is deleted
        float cateY = windowY + 65;
        for (Category category : Category.values()) {
            if (isHovered(windowX, cateY - 8, windowX + 50, cateY + mc.textRenderer.fontHeight + 8, mouseX, mouseY) && button == 0) {
                // sets the new underline position
                if (category == selectedCategory) {
                    hy = cateY;
                }

                // set new category
                selectedCategory = category;

                dWheel = 0;

                // updates buttons to match new category
                modButtons.clear();
                float modY = 70 + modScrollNow;
                for (Module module : ModuleManager.INSTANCE.getModulesByCategory(selectedCategory)) {
                    modButtons.add(new ModButton(module, 0, modY, this));
                    modY += 40;
                }
            }

            cateY += 25;
        }

        // mouse clicked hook for the buttons
        for (ModButton modButton : modButtons) {
            if (ModuleManager.INSTANCE.getModulesByCategory(selectedCategory).contains(modButton.getModule())) {
                modButton.mouseClicked(mouseX, mouseY, button);
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {

        // stop dragging
        drag = false;

        if (viewType == ViewType.CONFIG) {
            configScreen.mouseReleased(mouseX, mouseY, button);
            return super.mouseReleased(mouseX, mouseY, button);
        }

        for (ModButton modButton : modButtons) {
            if (ModuleManager.INSTANCE.getModulesByCategory(selectedCategory).contains(modButton.getModule())) {
                modButton.mouseReleased(mouseX, mouseY, button);
            }
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {

        // set scroll value
        if (isHovered(windowX + 100 + settingsFieldX, windowY + 60, windowX + 425 + settingsFieldX, windowY + height, mouseX, mouseY))
            this.dWheel = amount;

        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (ModButton mb : modButtons) {
            mb.keyPressed(keyCode, scanCode, modifiers);
        }
        configScreen.keyPressed(keyCode, scanCode, modifiers);

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        for (ModButton mb : modButtons) {
            mb.charTyped(chr, modifiers);
        }
        configScreen.charTyped(chr, modifiers);

        return super.charTyped(chr, modifiers);
    }

    @Override
    public void close() {
        super.close();
        try {
            ClientMain.selectedConfig.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ClientMain.selectedConfig.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isHovered(float x, float y, float x2, float y2, double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
}
