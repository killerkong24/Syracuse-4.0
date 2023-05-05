package net.fabricmc.fabric.command.impl;

import net.fabricmc.fabric.command.Command;
import net.minecraft.client.network.ClientPlayerEntity;
import net.fabricmc.fabric.helper.utils.ChatUtils;

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
