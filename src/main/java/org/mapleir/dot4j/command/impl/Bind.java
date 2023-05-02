package org.mapleir.dot4j.command.impl;

import org.lwjgl.glfw.GLFW;
import org.mapleir.dot4j.command.Command;
import org.mapleir.dot4j.helper.utils.ChatUtils;
import org.mapleir.dot4j.helper.utils.KeyUtils;
import org.mapleir.dot4j.systems.module.core.Module;
import org.mapleir.dot4j.systems.module.core.ModuleManager;

import java.util.HashMap;
import java.util.Map;

public class Bind extends Command {
    private static final Map<String, Integer> KEY_MAP = new HashMap<>();

    static {
        KEY_MAP.put("a", GLFW.GLFW_KEY_A);
        KEY_MAP.put("b", GLFW.GLFW_KEY_B);
        KEY_MAP.put("c", GLFW.GLFW_KEY_C);
        KEY_MAP.put("d", GLFW.GLFW_KEY_D);
        KEY_MAP.put("e", GLFW.GLFW_KEY_E);
        KEY_MAP.put("f", GLFW.GLFW_KEY_F);
        KEY_MAP.put("g", GLFW.GLFW_KEY_G);
        KEY_MAP.put("h", GLFW.GLFW_KEY_H);
        KEY_MAP.put("i", GLFW.GLFW_KEY_I);
        KEY_MAP.put("j", GLFW.GLFW_KEY_J);
        KEY_MAP.put("k", GLFW.GLFW_KEY_K);
        KEY_MAP.put("l", GLFW.GLFW_KEY_L);
        KEY_MAP.put("m", GLFW.GLFW_KEY_M);
        KEY_MAP.put("n", GLFW.GLFW_KEY_N);
        KEY_MAP.put("o", GLFW.GLFW_KEY_O);
        KEY_MAP.put("p", GLFW.GLFW_KEY_P);
        KEY_MAP.put("q", GLFW.GLFW_KEY_Q);
        KEY_MAP.put("r", GLFW.GLFW_KEY_R);
        KEY_MAP.put("s", GLFW.GLFW_KEY_S);
        KEY_MAP.put("t", GLFW.GLFW_KEY_T);
        KEY_MAP.put("u", GLFW.GLFW_KEY_U);
        KEY_MAP.put("v", GLFW.GLFW_KEY_V);
        KEY_MAP.put("w", GLFW.GLFW_KEY_W);
        KEY_MAP.put("x", GLFW.GLFW_KEY_X);
        KEY_MAP.put("y", GLFW.GLFW_KEY_Y);
        KEY_MAP.put("z", GLFW.GLFW_KEY_Z);
        KEY_MAP.put("0", GLFW.GLFW_KEY_0);
        KEY_MAP.put("1", GLFW.GLFW_KEY_1);
        KEY_MAP.put("2", GLFW.GLFW_KEY_2);
        KEY_MAP.put("3", GLFW.GLFW_KEY_3);
        KEY_MAP.put("4", GLFW.GLFW_KEY_4);
        KEY_MAP.put("5", GLFW.GLFW_KEY_5);
        KEY_MAP.put("6", GLFW.GLFW_KEY_6);
        KEY_MAP.put("7", GLFW.GLFW_KEY_7);
        KEY_MAP.put("8", GLFW.GLFW_KEY_8);
        KEY_MAP.put("9", GLFW.GLFW_KEY_9);
        KEY_MAP.put("f1", GLFW.GLFW_KEY_F1);
        KEY_MAP.put("f2", GLFW.GLFW_KEY_F2);
        KEY_MAP.put("f3", GLFW.GLFW_KEY_F3);
        KEY_MAP.put("f4", GLFW.GLFW_KEY_F4);
        KEY_MAP.put("f5", GLFW.GLFW_KEY_F5);
        KEY_MAP.put("f6", GLFW.GLFW_KEY_F6);
        KEY_MAP.put("f7", GLFW.GLFW_KEY_F7);
        KEY_MAP.put("f8", GLFW.GLFW_KEY_F8);
        KEY_MAP.put("f9", GLFW.GLFW_KEY_F9);
        KEY_MAP.put("f10", GLFW.GLFW_KEY_F10);
        KEY_MAP.put("f11", GLFW.GLFW_KEY_F11);
        KEY_MAP.put("f12", GLFW.GLFW_KEY_F12);
        KEY_MAP.put("tab", GLFW.GLFW_KEY_TAB);
        KEY_MAP.put("capslock", GLFW.GLFW_KEY_CAPS_LOCK);
        KEY_MAP.put("leftshift", GLFW.GLFW_KEY_LEFT_SHIFT);
        KEY_MAP.put("rightshift", GLFW.GLFW_KEY_RIGHT_SHIFT);
        KEY_MAP.put("leftctrl", GLFW.GLFW_KEY_LEFT_CONTROL);
        KEY_MAP.put("rightctrl", GLFW.GLFW_KEY_RIGHT_CONTROL);
        KEY_MAP.put("leftalt", GLFW.GLFW_KEY_LEFT_ALT);
        KEY_MAP.put("rightalt", GLFW.GLFW_KEY_RIGHT_ALT);
        KEY_MAP.put("space", GLFW.GLFW_KEY_SPACE);
        KEY_MAP.put("enter", GLFW.GLFW_KEY_ENTER);
        KEY_MAP.put("escape", GLFW.GLFW_KEY_ESCAPE);
        KEY_MAP.put("backspace", GLFW.GLFW_KEY_BACKSPACE);
        KEY_MAP.put("delete", GLFW.GLFW_KEY_DELETE);
        KEY_MAP.put("insert", GLFW.GLFW_KEY_INSERT);
        KEY_MAP.put("home", GLFW.GLFW_KEY_HOME);
        KEY_MAP.put("end", GLFW.GLFW_KEY_END);
        KEY_MAP.put("pageup", GLFW.GLFW_KEY_PAGE_UP);
        KEY_MAP.put("pagedown", GLFW.GLFW_KEY_PAGE_DOWN);
        KEY_MAP.put("up", GLFW.GLFW_KEY_UP);
        KEY_MAP.put("down", GLFW.GLFW_KEY_DOWN);
        KEY_MAP.put("left", GLFW.GLFW_KEY_LEFT);
        KEY_MAP.put("right", GLFW.GLFW_KEY_RIGHT);
        KEY_MAP.put("numlock", GLFW.GLFW_KEY_NUM_LOCK);
        KEY_MAP.put("numpad0", GLFW.GLFW_KEY_KP_0);
        KEY_MAP.put("numpad1", GLFW.GLFW_KEY_KP_1);
        KEY_MAP.put("numpad2", GLFW.GLFW_KEY_KP_2);
        KEY_MAP.put("numpad3", GLFW.GLFW_KEY_KP_3);
        KEY_MAP.put("numpad4", GLFW.GLFW_KEY_KP_4);
        KEY_MAP.put("numpad5", GLFW.GLFW_KEY_KP_5);
        KEY_MAP.put("numpad6", GLFW.GLFW_KEY_KP_6);
        KEY_MAP.put("numpad7", GLFW.GLFW_KEY_KP_7);
        KEY_MAP.put("numpad8", GLFW.GLFW_KEY_KP_8);
        KEY_MAP.put("numpad9", GLFW.GLFW_KEY_KP_9);
        KEY_MAP.put("numpaddecimal", GLFW.GLFW_KEY_KP_DECIMAL);
        KEY_MAP.put("numpadadd", GLFW.GLFW_KEY_KP_ADD);
        KEY_MAP.put("numpadsubtract", GLFW.GLFW_KEY_KP_SUBTRACT);
        KEY_MAP.put("numpadmultiply", GLFW.GLFW_KEY_KP_MULTIPLY);
        KEY_MAP.put("numpaddivide", GLFW.GLFW_KEY_KP_DIVIDE);
    }

    public Bind() {
        super("bind", "Binds a specified module", "");
    }

    @Override
    public void onCmd(String message, String[] args) {
        if (args.length < 3) {
            ChatUtils.addChatMessage("Not enough arguments.");
            return;
        }

        String mod = args[1];
        String key = args[2];
        Module module = ModuleManager.INSTANCE.getModuleByName(mod);
        if (module == null) {
            ChatUtils.addChatMessage("Invalid module name.");
            return;
        }

        int boundKey;
        try {
            boundKey = Integer.parseInt(key);
        } catch (NumberFormatException e) {
            if (KEY_MAP.containsKey(key.toLowerCase())) {
                boundKey = KEY_MAP.get(key.toLowerCase());
            } else {
                ChatUtils.addChatMessage("Invalid key format.");
                return;
            }
        }

        module.setKey(boundKey);
        ChatUtils.addChatMessage("Bound " + mod + " to " + KeyUtils.getKey(boundKey));
    }
}