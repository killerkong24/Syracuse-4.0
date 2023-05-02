package org.mapleir.dot4j.systems.module.impl.player;

import org.mapleir.dot4j.event.EventTarget;
import org.mapleir.dot4j.event.impl.EventMotion;
import org.mapleir.dot4j.event.impl.EventUpdate;
import org.mapleir.dot4j.gui.setting.ModeSetting;
import org.mapleir.dot4j.gui.setting.NumberSetting;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

import static org.mapleir.dot4j.helper.utils.MoveHelper.getDistanceToGround;

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
