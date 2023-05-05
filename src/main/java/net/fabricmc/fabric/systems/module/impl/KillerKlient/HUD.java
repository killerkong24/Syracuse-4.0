package net.fabricmc.fabric.systems.module.impl.KillerKlient;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;

@Module.Info(name = "HUD", description = "HUD Elements", category = Category.KILLERKLIENT)
public class HUD extends Module {

    BooleanSetting rgb = new BooleanSetting("RGB", true);
    BooleanSetting fps = new BooleanSetting("FPS", true);
    BooleanSetting hp = new BooleanSetting("HP", true);
    BooleanSetting xyz = new BooleanSetting("XYZ", true);
    BooleanSetting username = new BooleanSetting("Username", true);
    BooleanSetting direction = new BooleanSetting("Direction", true);


    public HUD() {
        addSettings(rgb, fps, hp, xyz, username, direction);
    }

    public static int getRainbow(float sat, float bri, double speed, int offset) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + offset) / speed) % 360;
        return 0xff000000 | MathHelper.hsvToRgb((float) (rainbowState / 360.0), sat, bri);
    }


    @Override
    public void draw(MatrixStack matrices) {
        if (rgb.isEnabled()) {
            mc.textRenderer.drawWithShadow(matrices, "§lS", 2, 2, getRainbow(0.5f, 1f, 25, 0));
            mc.textRenderer.drawWithShadow(matrices, "yracuse", mc.textRenderer.getWidth("S") + 3, 2, 0xFFFFFF);
        } else {
            mc.textRenderer.drawWithShadow(matrices, "S", 2, 2, 0x6FA8DC);
            mc.textRenderer.drawWithShadow(matrices, "yracuse", 2 + mc.textRenderer.getWidth("S"), 2, 0xFFFFFF);
        }
        if (fps.isEnabled()) {
            mc.getCurrentFps();
            mc.textRenderer.drawWithShadow(matrices, "FPS: " + mc.getCurrentFps(), 2, 4 + mc.textRenderer.fontHeight, 0xFFFFFF);
        }
        if (hp.isEnabled()) {
            mc.player.getHealth();
            if (mc.player.getHealth() >= 15) {
                mc.textRenderer.drawWithShadow(matrices, "HP: " + Math.round(mc.player.getHealth()), 2, 6 + mc.textRenderer.fontHeight * 2, 0xFF0FFF33);
            } else if (mc.player.getHealth() > 10) {
                mc.textRenderer.drawWithShadow(matrices, "HP: " + Math.round(mc.player.getHealth()), 2, 6 + mc.textRenderer.fontHeight * 2, 0xFFFF8C00);
            } else if (mc.player.isAlive()) {
                mc.textRenderer.drawWithShadow(matrices, "HP: " + Math.round(mc.player.getHealth()), 2, 6 + mc.textRenderer.fontHeight * 2, 0xFFFF0A0A);
            } else {
                mc.textRenderer.drawWithShadow(matrices, "HP: " + Math.round(mc.player.getHealth()), 2, 6 + mc.textRenderer.fontHeight * 2, 0xFF000000);
            }
        }
        if (xyz.isEnabled()) {
            mc.player.getBlockPos();
            mc.textRenderer.drawWithShadow(matrices, "XYZ: " + mc.player.getBlockPos().getX() + " " + mc.player.getBlockPos().getY() + " " + mc.player.getBlockPos().getZ(), 2, 8 + mc.textRenderer.fontHeight * 3, 0xFFFFFF);
        }
        if (username.isEnabled()) {
            mc.player.getDisplayName();
            mc.textRenderer.drawWithShadow(matrices, "Username: " + mc.player.getDisplayName().getString(), 2, 10 + mc.textRenderer.fontHeight * 4, 0xFF6A54);
        }
        if (direction.isEnabled()) {
            mc.player.getMovementDirection();
            mc.textRenderer.drawWithShadow(matrices, "Direction: " + mc.player.getMovementDirection(), 2, 20 + mc.textRenderer.fontHeight * 4, -1);
        }
    }
}
