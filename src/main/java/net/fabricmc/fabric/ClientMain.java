package net.fabricmc.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.event.impl.WorldRenderEndEvent;
import net.fabricmc.fabric.gui.clickgui.ClickGUI;
import net.fabricmc.fabric.helper.utils.PacketHelper;
import net.fabricmc.fabric.helper.utils.Theme;
import net.fabricmc.fabric.systems.config.Config;
import net.fabricmc.fabric.systems.config.ConfigLoader;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.systems.module.core.ModuleManager;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

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
                if (action == GLFW.GLFW_PRESS) {
                    if (key == GLFW.GLFW_KEY_8) mc.setScreen(ClickGUI.getINSTANCE());
                    for (Module module : ModuleManager.INSTANCE.getModules()) {
                        if (module.getKey() == key) {
                            module.toggle();

                    }
                }
            }
        }
    }

    public void onTick() {
        if (PacketHelper.mc.player != null) {
            for (Module module : ModuleManager.INSTANCE.getEnabledModules()) {
                module.onTick();
            }
        }
    }
}
