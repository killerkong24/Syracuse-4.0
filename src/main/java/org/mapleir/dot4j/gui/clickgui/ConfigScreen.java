package org.mapleir.dot4j.gui.clickgui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.mapleir.dot4j.ClientMain;
import org.mapleir.dot4j.gui.clickgui.components.Component;
import org.mapleir.dot4j.helper.utils.Animation;
import org.mapleir.dot4j.helper.utils.RenderUtils;
import org.mapleir.dot4j.helper.utils.Theme;
import org.mapleir.dot4j.systems.config.Config;
import org.mapleir.dot4j.systems.config.ConfigLoader;

import java.awt.*;
import java.io.IOException;

public class ConfigScreen extends Component {

    public final ClickGUI parent;
    public EditConfigMenu editConfigMenu;
    private Animation createNewAnimX, createNewAnimY;
    private boolean isConfigAdded = false;

    public ConfigScreen(ClickGUI parent) {
        this.parent = parent;
    }

    @Override
    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        float pX = parent.windowX, pY = parent.windowY, pW = parent.width, pH = parent.height;

        mc.textRenderer.draw(matrices, "Your Profiles", pX + 20, pY + 50, Theme.MODULE_TEXT.getRGB());
        // kewl blue underline
        RenderUtils.renderRoundedQuad(matrices, pX + 20, pY + 50 + mc.textRenderer.fontHeight, pX + 30, pY + 50 + mc.textRenderer.fontHeight + 2, 1, 20, Theme.ENABLED);

        // button to add config stuff aka the plus
        RenderUtils.drawCircle(matrices, pX + pW - 30, pY + pH - 30, 15, 30, -1);

        // scales the plus a bit
        matrices.push();
        // when scaling something, and you still want to use the original values where you drew it, just divide the coords by how much you scaled it
        matrices.scale(2f, 2f, 0);                        // I divided by 2
        mc.textRenderer.draw(matrices, "+", (int) ((pX + pW - 35) / 2), (int) ((pY + pH - 37) / 2), Color.BLACK.getRGB());
        matrices.pop();

        matrices.push();
        matrices.scale(0.05f, 0.05f, 0);
        RenderUtils.drawTexturedRectangle(matrices, (pX + pW - 35) * 20, (pY + 10) * 20, "textures/reload.png");
        matrices.pop();

        // draw cfgs
        int offsetX = 0;
        int offsetY = 0;
        for (Config config : ConfigLoader.getConfigs()) {

            if (isConfigAdded && config == ConfigLoader.lastAddedConfig) {
                if (createNewAnimX != null && createNewAnimY != null) {
                    createNewAnimX.update(true, 3);
                    createNewAnimY.update(true, 3);

                    // draw kool box
                    RenderUtils.renderRoundedQuad(matrices, createNewAnimX.getValue(), createNewAnimY.getValue(), createNewAnimX.getValue() + 80, createNewAnimY.getValue() + 80, 5, 20, Theme.SETTINGS_HEADER);
                    mc.textRenderer.draw(matrices, config.getName(), createNewAnimX.getValue() + 5, createNewAnimY.getValue() + 5, -1);

                    // draws desc
                    drawStringWrapped(matrices, config.getDescription(), (int) createNewAnimX.getValue() + 5, (int) (createNewAnimY.getValue() + mc.textRenderer.fontHeight + 10), 70);

                    // stops animation
                    if (createNewAnimX.hasEnded() && createNewAnimY.hasEnded()) {
                        createNewAnimX = null;
                        createNewAnimY = null;
                        isConfigAdded = false;
                    }
                }
            } else {
                // TODO: add a fading animation?
                // see if the config is selected
                if (config == ClientMain.selectedConfig) {
                    RenderUtils.renderRoundedQuad(matrices, pX + offsetX + 17, pY + 72 + offsetY, pX + 103 + offsetX, pY + 158 + offsetY, 5, 20, Color.WHITE);
                }

                RenderUtils.renderRoundedQuad(matrices, pX + offsetX + 20, pY + 75 + offsetY, pX + 100 + offsetX, pY + 155 + offsetY, 5, 20, Theme.SETTINGS_HEADER);
                mc.textRenderer.draw(matrices, config.getName(), pX + offsetX + 25, pY + 80 + offsetY, -1);

                matrices.push();
                // have to multiply by 1 / scale (1 / 0.025 = 40)
                matrices.scale(0.025f, 0.025f, 0);
                RenderUtils.drawTexturedRectangle(matrices, (pX + offsetX + 25) * 40, (pY + offsetY + 136) * 40, "textures/edit.png");
                matrices.pop();

                // draw desc
                drawStringWrapped(matrices, config.getDescription(), (int) pX + offsetX + 25, (int) pY + 85 + mc.textRenderer.fontHeight + offsetY, 70);
            }

            offsetX += 90;

            if (offsetX >= 450) {
                offsetX = 0;
                offsetY = 90;
            }
        }

        if (editConfigMenu != null) editConfigMenu.drawScreen(matrices, mouseX, mouseY, delta);
    }

    private void drawStringWrapped(MatrixStack matrices, String text, int x, int y, int boxSize) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Text textObject = Text.of(text);
        OrderedText[] lines = mc.textRenderer.wrapLines(textObject, boxSize).toArray(new OrderedText[0]);

        int offset = 0;
        for (OrderedText orderedText : lines) {
            mc.textRenderer.draw(matrices, orderedText, x, y + offset, 0xFFFFFFFF);
            offset += mc.textRenderer.fontHeight;
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {

        // Just some vars
        float pX = parent.windowX, pY = parent.windowY, pW = parent.width, pH = parent.height;

        if (editConfigMenu != null) {
            editConfigMenu.mouseClicked(mouseX, mouseY, button);
            return;
        }

        if (isHovered(pX + pW - 45, pY + pH - 45, pX + pW - 15, pY + pH - 15, mouseX, mouseY) && button == 0) {
            Config newConfig = new Config("Config Name", "Config Description");
            try {
                newConfig.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
            addConfig(newConfig);
        }

        // reload
        if (isHovered(pX + pW - 40, pY + 10, pX + pW - 10, pY + 40, mouseX, mouseY) && button == 0) {
            try {
                ConfigLoader.loadConfigs();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int offsetX = 0;
        int offsetY = 0;
        for (Config config : ConfigLoader.getConfigs()) {

            // edit
            if (isHovered(pX + offsetX + 25, pY + offsetY + 136, pX + offsetX + 40, pY + offsetY + 150, mouseX, mouseY) && button == 0) {
                editConfigMenu = new EditConfigMenu(this, config);
            }

            if (isHovered(pX + pW - 45, pY + pH - 45, pX + pW - 15, pY + pH - 15, mouseX, mouseY) && button == 0) {
                createNewAnimX = new Animation((int) (pX + pW - 35), (int) pX + offsetX + 20);
                createNewAnimY = new Animation((int) (pY + pH - 37), (int) pY + 75 + offsetY);
            }

            if (isHovered(pX + offsetX + 17, pY + 72 + offsetY, pX + 103 + offsetX, pY + 158 + offsetY, mouseX, mouseY) && button == 0) {
                // before changing we have to save the one before
                try {
                    ClientMain.selectedConfig.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ClientMain.selectedConfig = config;
                // attempts to load config
                try {
                    config.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            offsetX += 90;

            // TODO: >10 configs
            if (offsetX >= 450) {
                offsetX = 0;
                offsetY = 90;
            }
        }
    }

    public void addConfig(Config config) {
        ConfigLoader.addConfig(config);
        isConfigAdded = true;
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (editConfigMenu != null) editConfigMenu.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void charTyped(char chr, int modifiers) {
        if (editConfigMenu != null) editConfigMenu.charTyped(chr, modifiers);
    }
}
