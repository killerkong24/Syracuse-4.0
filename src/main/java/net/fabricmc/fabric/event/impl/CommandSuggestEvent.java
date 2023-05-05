package net.fabricmc.fabric.event.impl;

import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import net.fabricmc.fabric.event.Event;

public class CommandSuggestEvent extends Event {

    private final CommandSuggestionsS2CPacket packet;

    public CommandSuggestEvent(CommandSuggestionsS2CPacket packet) {
        this.packet = packet;
    }

    public CommandSuggestionsS2CPacket getPacket() {
        return packet;
    }
}
