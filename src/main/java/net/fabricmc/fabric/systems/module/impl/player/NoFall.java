package net.fabricmc.fabric.systems.module.impl.player;

import net.fabricmc.fabric.event.EventTarget;
import net.fabricmc.fabric.event.impl.EventMotion;
import net.fabricmc.fabric.event.impl.EventUpdate;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;

import static net.fabricmc.fabric.helper.utils.MoveHelper.getDistanceToGround;

@Module.Info(name = "NoFall", description = "Makes you not take fall damage.", category = Category.PLAYER)
public class NoFall extends Module {

    ModeSetting mode = new ModeSetting("Mode", "Vulcan", "Vulcan", "Vanilla");
    NumberSetting distance = new NumberSetting("distance", 3, 20, 4, 0.1);

    private boolean hasCancelledFallDamage;

    public NoFall() {
        addSettings(mode, distance);
    }

    @EventTarget
    public void onUpdate(final EventUpdate e) {

        double pos = mc.player.getY();

        double distanceToGround = getDistanceToGround(mc.player);

        if (mode.isMode("Vulcan")) {
            if (distanceToGround >= distance.getFloatValue() && !hasCancelledFallDamage) {
                mc.player.updatePosition(mc.player.getX(), mc.player.getY() + 0.1, mc.player.getZ());
                hasCancelledFallDamage = true;
            }
            if (distanceToGround < distance.getFloatValue()) {
                hasCancelledFallDamage = false;
            }
        }
    }

    @EventTarget
    public void onPreMotion(final EventMotion.Pre e) {
        if (isNull()) {
            return;
        }
        if (mc.player.fallDistance >= 1.5F && mode.isMode("Vanilla")) {
            e.setOnGround(true);
        }
    }

}
