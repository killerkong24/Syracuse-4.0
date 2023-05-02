package org.mapleir.dot4j.helper.utils;

import net.minecraft.text.Text;

import static org.mapleir.dot4j.helper.utils.SyraMC.mc;

public class ChatUtils {


    // unicode for §
    private final String paragraph = "\u00A7";

    public static void addChatMessage(String message) {
        mc.inGameHud.getChatHud().addMessage(Text.literal(message));
    }

    public void sendMsg(String text) {
        if (mc.player != null) mc.player.sendMessage(Text.of(translate(text)));
    }

    public void sendMsg(Text text) {
        if (mc.player != null) mc.player.sendMessage(text);
    }

    public String translate(String text) {
        return text.replace("&", paragraph);
    }
}
