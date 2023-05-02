package org.mapleir.dot4j.systems.module.impl.movement;


import net.minecraft.network.Packet;
import net.minecraft.util.math.Vec3d;
import org.mapleir.dot4j.event.EventTarget;
import org.mapleir.dot4j.event.impl.EventUpdate;
import org.mapleir.dot4j.event.impl.PacketSendEvent;
import org.mapleir.dot4j.gui.setting.NumberSetting;
import org.mapleir.dot4j.helper.utils.PacketHelper;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

import java.util.ArrayList;
import java.util.List;

import static org.mapleir.dot4j.helper.utils.SyraMC.mc;
@Module.Info(name = "Fake Lag", description = "makes it look like your lagging", category = Category.MOVEMENT)
public class FakeLag extends Module {

    private final NumberSetting delay = new NumberSetting("Delay", 0.0, 1000.0, 300.0, 1.0);

    private double tickTimer;
    private final List<Packet<?>> packets = new ArrayList<>();

    private boolean sending;
    // Todo: Render fake lag
    private Vec3d lastPos;

    public FakeLag() {
        addSettings(delay);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        packets.clear();
        tickTimer = 0;
        lastPos = mc.player.getPos();
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {

        if(mc.player == null) return;

        if(tickTimer==(delay.getFloatValue())) {
            sending = true;
            tickTimer = 0;

            if(packets.isEmpty()) {
                sending = false;
                return;
            }

            for (Packet<?> packet : packets) {
                PacketHelper.sendPacket(packet);
            }

            sending = false;
            packets.clear();
            lastPos = mc.player.getPos();
        }
    }

    @EventTarget
    public void onPacket(PacketSendEvent e) {
        if(mc.player == null) return;

        if(!sending) {
            packets.add(e.getPacket());
            e.setCancelled(true);
        }
    }
}
