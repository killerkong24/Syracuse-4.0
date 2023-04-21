package org.mapleir.dot4j.systems.module.impl.player;

import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.mapleir.dot4j.gui.setting.ModeSetting;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

@Module.Info(name = "Criticals", description = "Crits for you.", category = Category.COMBAT)
public class Criticals extends Module {
    ModeSetting mode = new ModeSetting("Mode", "Packet");

    public Criticals() {
        addSettings(mode);
    }

    // SOMEONE PLEASE MAKE A ON PACKET SEND AND CHECK IF ITS A PlayerInteractEntityC2SPacket


    @Override
    public void onEnable() {
        super.onEnable();
        sendMsg("SOMEONE PLEASE MAKE A ON PACKET SEND AND CHECK IF ITS A PlayerInteractEntityC2SPacket");
    }

    private void doCrit() {
        if (mc.player.isClimbing() || mc.player.isTouchingWater() || mc.player.hasVehicle()) return;
        if (mc.player.isSprinting()) {
            mc.player.setSprinting(false);
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
        }

        if (mc.player.isOnGround()) {
            double x = mc.player.getX();
            double y = mc.player.getY();
            double z = mc.player.getZ();

            if (mode.isMode("Packet")) {
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.0633, z, false));
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false));
            }
        }
    }
}