package org.mapleir.dot4j.gui.clickgui;

import net.minecraft.client.util.math.MatrixStack;
import org.mapleir.dot4j.gui.clickgui.components.TextComponent;
import org.mapleir.dot4j.helper.utils.Animation;
import org.mapleir.dot4j.helper.utils.RenderUtils;
import org.mapleir.dot4j.helper.utils.Theme;
import org.mapleir.dot4j.systems.config.Config;
import org.mapleir.dot4j.systems.config.ConfigLoader;

import java.awt.*;
import java.io.IOException;

public class EditConfigMenu extends org.mapleir.dot4j.gui.clickgui.components.Component {

    private final ConfigScreen parent;
    private final Animation animation;
    private final org.mapleir.dot4j.gui.clickgui.components.TextComponent nameText;
    private final org.mapleir.dot4j.gui.clickgui.components.TextComponent descriptionText;
    private final Config editing;
    private boolean close;

    public EditConfigMenu(ConfigScreen parent, Config editing) {
        this.parent = parent;
        this.editing = editing;
        animation = new Animation(0, 50);
        nameText = new org.mapleir.dot4j.gui.clickgui.components.TextComponent(parent.parent, editing.getName());
        descriptionText = new TextComponent(parent.parent, editing.getDescription());
        close = false;
    }

    @Override
    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        // gets an outro value

        float pX = parent.parent.windowX, pY = parent.parent.windowY, pWidth = parent.parent.width, pHeight = parent.parent.height;

        animation.update(false, 3);

        RenderUtils.renderRoundedQuad(matrices,
                pX + 197 - animation.getValue(),
                pY + 147 - animation.getValue(),
                pX + 303 + animation.getValue(),
                pY + 153 + animation.getValue(), 10, 20, Color.white);

        RenderUtils.renderRoundedQuad(matrices,
                pX + 200 - animation.getValue(),
                pY + 150 - animation.getValue(),
                pX + 300 + animation.getValue(),
                pY + 150 + animation.getValue(), 10, 20, Theme.CONFIG_EDIT_BG);

        if (animation.hasEnded() && close) {
            parent.editConfigMenu = null;
            return;
        }

        if (animation.hasEnded() && !close) {
            mc.textRenderer.draw(matrices, "Name", pX + 158, pY + 110, Theme.MODULE_TEXT.getRGB());
            nameText.draw(matrices, pX + 155, pY + 120, 12);

            mc.textRenderer.draw(matrices, "Description", pX + 158, pY + 135, Theme.MODULE_TEXT.getRGB());
            descriptionText.draw(matrices, pX + 155, pY + 146, 12);

            matrices.push();
            matrices.scale(1.4f, 1.4f, 0);
            int hover = isHovered(pX + 337, pY + 103, pX + 346, pY + 112, mouseX, mouseY) ? -1 : Theme.MODULE_TEXT.getRGB();
            mc.textRenderer.draw(matrices, "x", (pX + 335) / 1.4f, (pY + 103) / 1.4f, hover);
            matrices.pop();

            // TODO: hover effect
            mc.textRenderer.draw(matrices, "Delete", pX + 315, pY + 185, -1);
        }

        editing.setName(nameText.getText());
        editing.setDescription(descriptionText.getText());
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        float pX = parent.parent.windowX, pY = parent.parent.windowY, pWidth = parent.parent.width, pHeight = parent.parent.height;

        if (isHovered(pX + 337, pY + 103, pX + 346, pY + 112, mouseX, mouseY) && button == 0) {
            close();
        }

        if (isHovered(pX + 310, pY + 185, pX + 345, pY + 195, mouseX, mouseY) && button == 0) {
            if (editing == ConfigLoader.getDefaultConfig()) {
                // TODO: cannot delete default config
                return;
            }
            editing.delete();
            close();
            try {
                ConfigLoader.loadConfigs();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        nameText.mouseClicked(mouseX, mouseY, button);
        descriptionText.mouseClicked(mouseX, mouseY, button);
    }

    private void close() {
        animation.setEnd(7);
        close = true;
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (editing != ConfigLoader.getDefaultConfig()) {
            nameText.keyPressed(keyCode, scanCode, modifiers);
        }

        descriptionText.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void charTyped(char chr, int modifiers) {
        nameText.charTyped(chr, modifiers);
        descriptionText.charTyped(chr, modifiers);
    }
}
