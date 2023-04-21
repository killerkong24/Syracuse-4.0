package org.mapleir.dot4j.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import org.mapleir.dot4j.ClientMain;
import org.mapleir.dot4j.command.Command;
import org.mapleir.dot4j.command.CommandManager;
import org.mapleir.dot4j.event.impl.CommandSuggestEvent;
import org.mapleir.dot4j.event.impl.PacketSendEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onCommandSuggestions", at = @At("TAIL"))
    public void onCmdSuggest(CommandSuggestionsS2CPacket packet, CallbackInfo ci) {
        new CommandSuggestEvent(packet).call();
    }

    @Inject(method = "sendPacket", at = @At("HEAD"))
    public void onSend(Packet<?> packet, CallbackInfo ci) {
        PacketSendEvent pse = new PacketSendEvent(packet);
        if (pse.isCancelled()) ci.cancel();
    }

    // TODO: make a randomized prefix and make the randomized string prefix thing in gui
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void sendChatMessage(String msg, CallbackInfo ci) {
        if (!ClientMain.getINSTANCE().isSelfDestucted) {
            StringBuilder CMD = new StringBuilder();
            for (int i = 1; i < msg.toCharArray().length; ++i) {
                CMD.append(msg.toCharArray()[i]);
            }
            String[] args = CMD.toString().split(" ");

            if (msg.startsWith(ClientMain.getCommandPrefix())) {
                for (Command command : CommandManager.INSTANCE.getCmds()) {
                    if (args[0].equalsIgnoreCase(command.getName())) {
                        command.onCmd(msg, args);
                        ci.cancel();
                        break;
                    }
                }
            }
        }
    }
}
