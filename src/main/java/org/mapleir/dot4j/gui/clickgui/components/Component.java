package org.mapleir.dot4j.gui.clickgui.components;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class Component {

    protected final MinecraftClient mc = MinecraftClient.getInstance();

    protected static boolean isHovered(float x, float y, float x2, float y2, double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
    }

    public void charTyped(char chr, int modifiers) {
    }
}
