package net.fabricmc.fabric.gui.clickgui.components;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.clickgui.VapeClickGui;
import net.fabricmc.fabric.helper.utils.RenderUtils;
import net.fabricmc.fabric.helper.utils.Theme;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class TextComponent {

    private final VapeClickGui parent;
    private String text;
    private boolean focused;
    private float x, y, height;
    private final MinecraftClient mc = MinecraftClient.getInstance();

    public TextComponent(VapeClickGui parent, String startingText) {
        this.parent = parent;
        this.text = startingText;
    }

    public void draw(DrawContext context, float x, float y, float height) {

        this.x = x;
        this.y = y;
        this.height = height;

        RenderUtils.renderRoundedQuad(context, x , y, x + mc.textRenderer.getWidth(text) + 5, y + height, 5, 20, Theme.SETTINGS_HEADER);

        ClientMain.getFontRenderer().drawString(context.getMatrices(), text, x + 2, y + 2, -1);
    }

    public void keyPressed(int key, int scanCode, int modifiers) {
        if(!focused) return;

        boolean control = MinecraftClient.IS_SYSTEM_MAC ? modifiers == GLFW_MOD_SUPER : modifiers == GLFW_MOD_CONTROL;

        if(control && key == GLFW_KEY_C) {
            mc.keyboard.setClipboard(text);
            return;
        }
        if(control && key == GLFW_KEY_X) {
            mc.keyboard.setClipboard(text);
            clearSelection();
        }
        if (key == GLFW_KEY_ENTER || key == GLFW_KEY_KP_ENTER) {
            focused = false;
        }
        if(control && key == GLFW_KEY_V) {
            String clipboard = mc.keyboard.getClipboard();
            text += clipboard;
        }
        if(key == GLFW_KEY_BACKSPACE) {
            if(text.length() <= 0) return;
            StringBuilder sb = new StringBuilder(text);
            sb.deleteCharAt(text.length() - 1);
            text = sb.toString();
        }

    }

    public void charTyped(char letter, int modifiers) {

        if(!focused) return;

        boolean control = MinecraftClient.IS_SYSTEM_MAC ? modifiers == GLFW_MOD_SUPER : modifiers == GLFW_MOD_CONTROL;
        boolean shift = modifiers == GLFW_MOD_SHIFT;

        if(!control && isValid(letter)) {
            if(shift) {
                letter = ((Character) letter).toString().toUpperCase().charAt(0);
            }
            text += letter;
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        focused = Component.isHovered(x, y, x + mc.textRenderer.getWidth(text) + 5, y + height, mouseX, mouseY) && button == 0;
    }

    public void clearSelection() {
        text = "";
    }

    private boolean isValid(char letter) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!\"$%&/()=? _-:.,;+*/#'";
        List<Character> characters = new ArrayList<>();
        for (char ch : chars.toCharArray()) {
            characters.add(ch);
        }
        return characters.contains(letter);
    }

    public String getText() {
        return text;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }
}
