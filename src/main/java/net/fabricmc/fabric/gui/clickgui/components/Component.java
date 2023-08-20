package net.fabricmc.fabric.gui.clickgui.components;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class Component {

    protected final MinecraftClient mc = MinecraftClient.getInstance();

    public void drawScreen(DrawContext context, int mouseX, int mouseY, float delta) {}

    public void mouseReleased(double mouseX, double mouseY, int button) {}

    public void mouseClicked(double mouseX, double mouseY, int button) {}

    public void keyPressed(int keyCode, int scanCode, int modifiers) {}

    public void charTyped(char chr, int modifiers) {}

    public static boolean isHovered(float x, float y, float x2, float y2, double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
}
