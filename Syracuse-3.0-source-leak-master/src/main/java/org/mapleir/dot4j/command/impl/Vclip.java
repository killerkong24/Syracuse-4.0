package org.mapleir.dot4j.command.impl;

import net.minecraft.client.network.ClientPlayerEntity;
import org.mapleir.dot4j.command.Command;
import org.mapleir.dot4j.helper.utils.ChatUtils;

public class Vclip extends Command {
    public Vclip() {
        super("Vclip", "clip up", "vclip");
    }


    @Override
    public void onCmd(String message, String[] args) {
        if (args.length < 1) {
            ChatUtils.addChatMessage("Not enough arguments.");
            return;
        }
        int height = Integer.parseInt(args[1]);

        ClientPlayerEntity player = mc.player;
        player.updatePosition(player.getX(), player.getY() + height, player.getZ());
    }
}
