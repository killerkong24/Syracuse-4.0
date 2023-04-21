package org.mapleir.dot4j.systems.module.impl.render;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.mapleir.dot4j.event.EventTarget;
import org.mapleir.dot4j.event.impl.EventUpdate;
import org.mapleir.dot4j.gui.setting.NumberSetting;
import org.mapleir.dot4j.helper.utils.RenderUtils;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

import java.awt.*;


@Module.Info(name = "HitCircle", description = "Renders a circle on your player", category = Category.RENDER)
public class HitCircle extends Module {

    public NumberSetting radiuses = new NumberSetting("Radius", 0.5, 6, 3, 0.1);

    public HitCircle() {
        addSettings(radiuses);
    }

    @EventTarget
    public void onUpdate(final EventUpdate e) {
        float radius = radiuses.getFloatValue();
        Vec3d pos = mc.player.getPos();
        float fov1 = mc.options.getFov().getValue();
        float fov = fov1  * 0.017453292F; // convert fov to radians


        MatrixStack matrixStack = new MatrixStack();
        RenderUtils.drawCircle(matrixStack, mc.player.getX(), mc.player.getY(), radius, 20, Color.RED); // draw the circle at the origin with radius 1.0 and color red



        //matrixStack.translate(mc.player.getX(), mc.player.getY(), mc.player.getZ()); // translate to player position
        //matrixStack.scale(radius, radius, radius); // scale the circle to the desired size
        //matrixStack.multiply(mc.gameRenderer.getBasicProjectionMatrix()); // apply the projection matrix
        //matrixStack.multiply(Matrix4f.perspective(fov, (float) width / (float) height, 0.1f, 100.0f)); // apply the projection matrix



        //RenderUtils.drawCircle(1, mc.player.getY());
    }

}
