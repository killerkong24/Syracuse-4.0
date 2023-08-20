package net.fabricmc.fabric.gui.clickgui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.components.ModButton;
import net.fabricmc.fabric.helper.utils.Animation;
import net.fabricmc.fabric.helper.utils.RenderUtils;
import net.fabricmc.fabric.helper.utils.Theme;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.systems.module.core.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VapeClickGui extends Screen {

    // Position saving
    private static final VapeClickGui INSTANCE = new VapeClickGui();
    // Minecraft reference
    private final MinecraftClient mc = MinecraftClient.getInstance();

    // Coords Dragging window around
    private float dragX, dragY;
    // True if dragging
    private boolean drag = false;

    // Window coordinates
    public float windowX = 200, windowY = 200;
    // Window size
    public float width = 500, height = 310;
    // Selected category
    public Category selectedCategory = Category.COMBAT;
    // Selected Module to edit
    public Module selectedModule;

    // Values for animation
    public float lastPercent;
    public float percent;
    public float percent2;
    public float lastPercent2;
    public float outro;
    public float lastOutro;

    // Boolean wether window should close
    private boolean close = false;
    // Window is closed
    private boolean closed;

    // Mouse wheel
    private double dWheel;

    // Value of the Category underline
    private float hy = windowY + 40;

    // List of all the mod buttons
    public final List<ModButton> modButtons = new ArrayList<>();

    // For moving them to the side when opening settings
    public int coordModX = 0;

    // X coordinate of Settings Field
    public float settingsFieldX;
    // Animation values for Settings
    public float settingsFNow, settingsF;

    // Mod Scroll
    private float modScrollEnd, modScrollNow;

    // Wether the mods or the config menu should be shown
    private ViewType viewType;

    // Config Screen instance
    private ConfigScreen configScreen;

    private Animation transition;

    protected VapeClickGui() {
        super(Text.of("Vape Click gui"));
    }

    @Override
    protected void init() {
        // Initialize open animation values
        percent = 1.33f;
        lastPercent = 1f;
        percent2 = 1.33f;
        lastPercent2 = 1f;
        outro = 1;
        lastOutro = 1;

        // Stop dragging when reopening
        drag = false;

        // Unselect module
        selectedModule = null;
        // Clear list of buttons
        modButtons.clear();

        // Set new buttons
        float modY = 70 + modScrollNow;
        for(Module module : ModuleManager.INSTANCE.getModulesByCategory(selectedCategory)) {
            modButtons.add(new ModButton(module, 0, modY, this));
            modY += 40;
        }

        viewType = ViewType.HOME;
        configScreen = new ConfigScreen(this);

        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        MatrixStack matrices = context.getMatrices();

        // Get an outro value
        float outro = smoothTrans(this.outro, lastOutro);
        // Check if player is ingame
        if (mc.currentScreen == null) {
            matrices.translate(mc.getWindow().getScaledWidth() / 2f, mc.getWindow().getScaledHeight() / 2f, 0);
            matrices.scale(outro, outro, 0);
            matrices.translate(-mc.getWindow().getScaledWidth() / 2f, -mc.getWindow().getScaledHeight() / 2f, 0);
        }

        // Get percent values
        percent = smoothTrans(this.percent, lastPercent);
        percent2 = smoothTrans(this.percent2, lastPercent2);

        if (percent > 0.98) {
            // Scale window
            matrices.translate(mc.getWindow().getScaledWidth() / 2f, mc.getWindow().getScaledHeight() / 2f, 0);
            matrices.scale(percent, percent, 0);
            matrices.translate(-mc.getWindow().getScaledWidth() / 2f, -mc.getWindow().getScaledHeight() / 2f, 0);
        } else {
            if (percent2 <= 1) {
                matrices.translate(mc.getWindow().getScaledWidth() / 2f, mc.getWindow().getScaledHeight() / 2f, 0);
                matrices.scale(percent2, percent2, 0);
                matrices.translate(-mc.getWindow().getScaledWidth() / 2f, -mc.getWindow().getScaledHeight() / 2f, 0);
            }
        }

        if(percent <= 1.5 && close) {
            percent = smoothTrans(this.percent, 2);
            percent2 = smoothTrans(this.percent2, 2);
        }

        if(percent >= 1.4  && close) {
            percent = 1.5f;
            closed = true;
            // Set ingame focus
            mc.currentScreen = null;
        }

        // Dragging the window
        if(drag) {
            // Updating window positions
            if (dragX == 0 && dragY == 0) {
                dragX = mouseX - windowX;
                dragY = mouseY - windowY;
            } else {
                windowX = mouseX - dragX;
                windowY = mouseY - dragY;
            }
        }
        // Anti-Issue
        else if (dragX != 0 || dragY != 0) {
            dragX = 0;
            dragY = 0;
        }

        // Draw the window                                                                                  corner radius
        RenderUtils.renderRoundedQuad(context, windowX, windowY, windowX + width, windowY + height, 25, 20, Theme.WINDOW_COLOR);

        // Draw the Client name
        ClientMain.getFontRenderer().drawString(matrices, ClientMain.getINSTANCE().getName(), (windowX + 20), (windowY + 20), -1);

        if(viewType == ViewType.CONFIG) {

            // Transition
            if(transition != null) {
                transition.update(true);
                RenderUtils.fill(context, transition.getValue(), windowY, transition.getEnd() - 19, windowY + height, Theme.WINDOW_COLOR.getRGB());
                if(transition.hasEnded()) transition = null;
            }

            configScreen.drawScreen(context, mouseX, mouseY, delta);

            // Back button
            if(isHovered(windowX + 20, windowY + height - 50, windowX + 20 + 32, windowY + height - 50 + 32, mouseX, mouseY) && configScreen.editConfigMenu == null) {
                matrices.push();
                RenderUtils.drawTexturedRectangle(context, windowX + 20, windowY + height - 50, "textures/back.png");
                matrices.pop();
            }
            else  {
                matrices.push();
                RenderSystem.setShader(GameRenderer::getPositionColorProgram);
                RenderSystem.setShaderColor(0.25f, 0.25f, 0.25f, 1f);
                RenderUtils.drawTexturedRectangle(context, windowX + 20, windowY + height - 50, "textures/back.png");
                RenderSystem.clearColor(1f, 1f, 1f, 1f);
                matrices.pop();
            }

            return;
        }

        Window window = mc.getWindow();
        // Draw the categories
        // RenderSystem.enableScissor((int) (windowX + 5) * 2, (int) (-windowY + (height / 2f) + 55) * 2, window.getScaledWidth() + 40, window.getScaledHeight() - 10);
        RenderUtils.enableScissor(windowX + 5, windowY + 55, windowX + width - 5, windowY + height - 15);

        // Easier to see where is being scissored
        //RenderUtils.fill(matrices.peek().getPositionMatrix(), window.getScaledWidth(), window.getScaledHeight(), -window.getScaledWidth(), -window.getScaledHeight(), -1);

        // Only show categories when no mod is expanded
        if(selectedModule == null) {

            // Show Config icon
            if(isHovered(windowX + 20, windowY + height - 50, windowX + 20 + 32, windowY + height - 50 + 32, mouseX, mouseY)) {
                matrices.push();
                RenderUtils.drawTexturedRectangle(context, windowX + 20, windowY + height - 50, "textures/config.png");
                matrices.pop();
            }
            else  {
                matrices.push();
                RenderSystem.setShader(GameRenderer::getPositionColorProgram);
                RenderSystem.setShaderColor(0.25f, 0.25f, 0.25f, 1f);
                RenderUtils.drawTexturedRectangle(context, windowX + 20, windowY + height - 50, "textures/config.png");
                RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                matrices.pop();
            }

            // Category room between
            float cateY = windowY + 65;

            for (Category category : Category.values()) {
                // If the category is selected
                if (category == selectedCategory) {
                    // White Category name for highlighting
                    ClientMain.getFontRenderer().drawString(matrices, category.name(), windowX + 20, cateY, -1);
                    // Draw the Category underline
                    RenderUtils.renderRoundedQuad(context, windowX + 20, hy + mc.textRenderer.fontHeight + 2, windowX + 30, hy + mc.textRenderer.fontHeight + 4, 1, 20, Theme.ENABLED);

                    // Update the underline position
                    if (hy != cateY) {
                        hy += (cateY - hy) / 2f;
                    }
                } else {
                    // Gray-ish category name
                    ClientMain.getFontRenderer().drawString(matrices, category.name(), windowX + 20, cateY, Theme.UNFOCUSED_TEXT_COLOR.getRGB());
                }

                cateY += 25;
            }
        }

        // Update setting values
        if (selectedModule != null) {
            if (settingsFieldX > -80) {
                settingsFieldX -= 5;
            }
        } else {
            if (settingsFieldX < 0) {
                settingsFieldX += 5;
            }
        }

        if(selectedModule != null) {
            // Draw Settings field

            RenderUtils.renderRoundedQuad(context,windowX + 430 + settingsFieldX, windowY + 60, windowX + width, windowY + height - 20, 5, 20, Theme.SETTINGS_BG);
            RenderUtils.renderRoundedQuad(context, windowX + 430 + settingsFieldX, windowY + 60, windowX + width, windowY + 85, 5, 20, Theme.SETTINGS_HEADER);

            ClientMain.getFontRenderer().drawString(matrices, selectedModule.getName(), windowX + 450 + settingsFieldX, windowY + 70, -1);

            // Settings scroll
            if (isHovered(windowX + 430 + (int) settingsFieldX, windowY + 60, windowX + width, windowY + height - 20, mouseX, mouseY)) {
                if (dWheel < 0 && Math.abs(settingsF) + 170 < (selectedModule.getSettings().size() * 25)) {
                    settingsF -= 32;
                }
                if (dWheel > 0 && settingsF < 0) {
                    settingsF += 32;
                }
            }

            // Update Setting animation values
            if (settingsFNow != settingsF) {
                settingsFNow += (settingsF - settingsFNow) / 2f;
                settingsFNow = (int) settingsFNow;
            }
        }

        // Scroll
        if (isHovered(windowX + 100 + settingsFieldX, windowY + 60, windowX + 425 + settingsFieldX, windowY + height, mouseX, mouseY)) {
            if (dWheel < 0 && Math.abs(modScrollEnd) + 220 < (ModuleManager.INSTANCE.getModulesByCategory(selectedCategory).size() * 40)) {
                modScrollEnd -= 25;
            }
            if (dWheel > 0 && modScrollEnd < 0) {
                modScrollEnd += 25;
            }
        }

        // Smoother scroll
        if (modScrollNow != modScrollEnd) {
            modScrollNow += (modScrollEnd - modScrollNow) / 2f;
            modScrollNow = (int) modScrollNow;
        }

        // Draw the Mod Buttons
        float modY = 70 + modScrollNow;
        for(ModButton modButton : modButtons) {
            if(ModuleManager.INSTANCE.getModulesByCategory(selectedCategory).contains(modButton.getModule())) {
                modButton.setY(modY);
                modButton.drawScreen(context, mouseX, mouseY, delta);
                modY += 40;
            }
        }

        RenderUtils.disableScissor();

        // Transition
        if(transition != null) {
            transition.update(true);
            RenderUtils.fill(context, transition.getValue(), windowY, transition.getEnd() - 19, windowY + height, Theme.WINDOW_COLOR.getRGB());
            if(transition.hasEnded()) transition = null;
        }
    }

    // Smoothly transitions between 2 values (stolen from Lune lol, the more fps you have the faster it will be (made for optimization purposes))
    public float smoothTrans(double current, double last){
        return (float) (current + (last - current) / (Double.parseDouble(MinecraftClient.getInstance().fpsDebugString.split(" ")[0]) / 10));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        // Drag the Window
        if (isHovered(windowX, windowY, windowX + width, windowY + 20, mouseX, mouseY) && button == 0) {
            drag = true;
        }

        // Click on the config Icon                                                                                                                                                Null check so u can't leave when editing a config
        if(isHovered(windowX + 20, windowY + height - 50, windowX + 20 + 32, windowY + height - 50 + 32, mouseX, mouseY) && button == 0 && selectedModule == null && configScreen.editConfigMenu == null) {
            transition = new Animation((int) (windowX - 20), (int) (windowX + width + 20));
            if(viewType == ViewType.CONFIG) {
                viewType = ViewType.HOME;
            }
            else viewType = ViewType.CONFIG;
        }

        if(viewType == ViewType.CONFIG) {
            configScreen.mouseClicked(mouseX, mouseY, button);
            return super.mouseClicked(mouseX, mouseY, button);
        }

        // When the Category is selected
        float cateY = windowY + 65;
        for(Category category : Category.values()) {
            if (isHovered(windowX, cateY - 8, windowX + 50, cateY + mc.textRenderer.fontHeight + 8, mouseX, mouseY) && button == 0) {
                // Set the new underline position
                if(category == selectedCategory) {
                    hy = cateY;
                }

                // Set new category
                selectedCategory = category;

                dWheel = 0;

                // Update buttons to match new category
                modButtons.clear();
                float modY = 70 + modScrollNow;
                for(Module module : ModuleManager.INSTANCE.getModulesByCategory(selectedCategory)) {
                    modButtons.add(new ModButton(module, 0, modY, this));
                    modY += 40;
                }
            }

            cateY += 25;
        }

        // Mouse Clicked hook for the buttons
        for(ModButton modButton : modButtons) {
            if(ModuleManager.INSTANCE.getModulesByCategory(selectedCategory).contains(modButton.getModule())) {
                modButton.mouseClicked(mouseX, mouseY, button);
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {

        // Stop dragging
        drag = false;

        if(viewType == ViewType.CONFIG) {
            configScreen.mouseReleased(mouseX, mouseY, button);
            return super.mouseReleased(mouseX, mouseY, button);
        }

        for(ModButton modButton : modButtons) {
            if(ModuleManager.INSTANCE.getModulesByCategory(selectedCategory).contains(modButton.getModule())) {
                modButton.mouseReleased(mouseX, mouseY, button);
            }
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {

        // Set Scroll value
        if(isHovered(windowX + 100 + settingsFieldX, windowY + 60, windowX + 425 + settingsFieldX, windowY + height, mouseX, mouseY)) this.dWheel = amount;

        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for(ModButton mb : modButtons) {
            mb.keyPressed(keyCode, scanCode, modifiers);
        }
        configScreen.keyPressed(keyCode, scanCode, modifiers);

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        for(ModButton mb : modButtons) {
            mb.charTyped(chr, modifiers);
        }
        configScreen.charTyped(chr, modifiers);

        return super.charTyped(chr, modifiers);
    }

    @Override
    public void close() {
        super.close();
        // Save current config
        try {
            ClientMain.getINSTANCE().selectedConfig.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Reload the selected config
        try {
            ClientMain.getINSTANCE().selectedConfig.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isHovered(float x, float y, float x2, float y2, double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    public static VapeClickGui getINSTANCE() {
        return INSTANCE;
    }
}
