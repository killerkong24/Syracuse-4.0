package org.mapleir.dot4j.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import org.mapleir.dot4j.event.impl.PacketReceiveEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(method = "handlePacket", at = @At("HEAD"))
    private static void onHandle(Packet<?> packet, PacketListener listener, CallbackInfo ci) {
        PacketReceiveEvent pse = new PacketReceiveEvent(packet);
        if (pse.isCancelled()) ci.cancel();
    }
}
