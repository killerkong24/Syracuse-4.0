package org.mapleir.dot4j.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.ResourcePackSendS2CPacket;
import org.mapleir.dot4j.systems.module.core.ModuleManager;
import org.mapleir.dot4j.systems.module.impl.KillerKlient.Spoofer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourcePackSendS2CPacket.class)
public class ResourcePackSendS2CPacketMixin {

    @Inject(method = "apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V", at = @At("HEAD"), cancellable = true)
    private void apply(ClientPlayPacketListener clientPlayPacketListener, CallbackInfo ci) {
        if (ModuleManager.INSTANCE.getModuleByClass(Spoofer.class).isEnabled() && ((Spoofer) ModuleManager.INSTANCE.getModuleByClass(Spoofer.class)).resource.isEnabled()) {
            ServerInfo serverInfo = MinecraftClient.getInstance().getCurrentServerEntry();
            if (serverInfo != null && serverInfo.getResourcePackPolicy() == ServerInfo.ResourcePackPolicy.DISABLED) {
                ci.cancel();
            }
        }
    }
}