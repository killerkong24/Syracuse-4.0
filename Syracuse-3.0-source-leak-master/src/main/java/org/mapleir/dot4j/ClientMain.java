package org.mapleir.dot4j;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.mapleir.dot4j.event.impl.WorldRenderEndEvent;
import org.mapleir.dot4j.gui.clickgui.ClickGUI;
import org.mapleir.dot4j.helper.utils.Theme;
import org.mapleir.dot4j.systems.config.Config;
import org.mapleir.dot4j.systems.config.ConfigLoader;
import org.mapleir.dot4j.systems.module.core.Module;
import org.mapleir.dot4j.systems.module.core.ModuleManager;

import java.io.IOException;

import static org.mapleir.dot4j.helper.utils.PacketHelper.mc;

public class ClientMain implements ModInitializer {

    private static final ClientMain INSTANCE = new ClientMain();

    private static final String name = "syracuse.vip";
    private static final String commandPrefix = "#";

    public static Config selectedConfig;
    public boolean isSelfDestucted = false;

    public static ClientMain getINSTANCE() {
        return INSTANCE;
    }

    // Getters
    public static String getName() {
        return name;
    }

    public static String getCommandPrefix() {
        return commandPrefix;
    }

    @Override
    public void onInitialize() {
        try {
            ConfigLoader.loadConfigs();
        } catch (IOException e) {
            e.printStackTrace();
        }
        WorldRenderEndEvent.init();
        Theme.dark();
//        }
    }

    public void onKeyPress(int key, int action) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.currentScreen == null) {
            if (mc.options.debugProfilerEnabled == true){
                if (action == GLFW.GLFW_PRESS) {
                    if (key == GLFW.GLFW_KEY_RIGHT_ALT) mc.setScreen(ClickGUI.getINSTANCE());
                    for (Module module : ModuleManager.INSTANCE.getModules()) {
                        if (module.getKey() == key) {
                            module.toggle();
                        }
                    }
                }
            }
        }
    }

    public void onTick() {
        if (mc.player != null) {
            for (Module module : ModuleManager.INSTANCE.getEnabledModules()) {
                module.onTick();
            }
        }
    }
}
