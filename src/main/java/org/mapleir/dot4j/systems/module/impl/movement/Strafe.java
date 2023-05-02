package org.mapleir.dot4j.systems.module.impl.movement;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import org.mapleir.dot4j.gui.setting.ModeSetting;
import org.mapleir.dot4j.gui.setting.NumberSetting;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

@Module.Info(name = "Strafe", description = "Moves, but faster", category = Category.MOVEMENT)
public class Strafe extends Module {

    public NumberSetting speed = new NumberSetting("Speed", 0.5, 2, 0.5, 0.1);
    public ModeSetting mode = new ModeSetting("Mode", "Static", "Static", "Vanilla");

    public Strafe() {
        addSettings(speed, mode);
    }

    public static GameMode getGameMode(PlayerEntity player) {
        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
        if (playerListEntry != null) return playerListEntry.getGameMode();
        return GameMode.DEFAULT;
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.getNetworkHandler() == null) {
            return;
        }

        if (mode.getMode().equalsIgnoreCase("Vanilla")) {
            mc.player.airStrafingSpeed = speed.getFloatValue() / 10;
        } else if (mode.getMode().equalsIgnoreCase("Static")) {
            GameOptions go = mc.options;
            float y = mc.player.getYaw();
            int mx = 0, mz = 0;
            if (go.backKey.isPressed()) {
                mz++;
            }
            if (go.leftKey.isPressed()) {
                mx--;
            }
            if (go.rightKey.isPressed()) {
                mx++;
            }
            if (go.forwardKey.isPressed()) {
                mz--;
            }
            double ts = speed.getFloatValue() / 2;
            double s = Math.sin(Math.toRadians(y));
            double c = Math.cos(Math.toRadians(y));
            double nx = ts * mz * s;
            double nz = ts * mz * -c;
            nx += ts * mx * -c;
            nz += ts * mx * -s;
            Vec3d nv3 = new Vec3d(nx, mc.player.getVelocity().y, nz);
            mc.player.setVelocity(nv3);
        }
        super.onTick();
    }
}
