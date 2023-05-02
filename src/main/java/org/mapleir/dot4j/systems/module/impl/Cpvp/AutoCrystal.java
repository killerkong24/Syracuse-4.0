package org.mapleir.dot4j.systems.module.impl.Cpvp;

import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.lwjgl.glfw.GLFW;
import org.mapleir.dot4j.event.EventTarget;
import org.mapleir.dot4j.event.impl.EndCrystalExplosionMcPlayerEvent;
import org.mapleir.dot4j.event.impl.EventMotion;
import org.mapleir.dot4j.event.impl.EventUpdate;
import org.mapleir.dot4j.gui.setting.BooleanSetting;
import org.mapleir.dot4j.gui.setting.ModeSetting;
import org.mapleir.dot4j.gui.setting.NumberSetting;
import org.mapleir.dot4j.helper.utils.CrystalUtils;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

@Module.Info(name = "AutoCrystal", description = "haxer tester", category = Category.CPVP)
public class AutoCrystal extends Module {
    public NumberSetting placeTicks = new NumberSetting("PlaceTicks", 0, 3, 0, 0.1);
    public NumberSetting breakTicks = new NumberSetting("BreakTicks", 0, 2, 0, 0.1);
    ModeSetting modeSetting = new ModeSetting("Mode", "Tick", "Tick", "Update", "Motion", "Render");

    private final BooleanSetting activeOnRightClick = new BooleanSetting("activate on right click", false);
    private double tickTimer;

    public AutoCrystal() {
        addSettings(modeSetting, placeTicks, breakTicks, activeOnRightClick);
        tickTimer = 0;
    }

    public static boolean nullCheck() {
        return mc.player != null && mc.world != null;
    }

    public void placeCrystal() {
        if (passedTicks(placeTicks.getValue())) {
            Vec3d cameraPos = mc.player.getCameraPosVec(0F);
            Vec3d rotationVec = mc.player.getRotationVec(0F);
            Vec3d targetPos = cameraPos.add(rotationVec.multiply(4.5D));
            BlockHitResult hit = mc.world.raycast(new RaycastContext(cameraPos, targetPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, mc.player));
            if (hit.getType() == HitResult.Type.BLOCK && CrystalUtils.canPlaceCrystalServer(hit.getBlockPos()) && (Blocks.OBSIDIAN == mc.world.getBlockState(hit.getBlockPos()).getBlock() || Blocks.BEDROCK == mc.world.getBlockState(hit.getBlockPos()).getBlock()) && (mc.player.getMainHandStack().isOf(Items.END_CRYSTAL))) {
                ActionResult result = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                if (result.isAccepted() && result.shouldSwingHand()) {
                    mc.player.swingHand(Hand.MAIN_HAND);
                }
                reset();
            }
        }
    }

    public void breakCrystal() {
        if (passedTicks(breakTicks.getValue())) {
            if (mc.crosshairTarget instanceof EntityHitResult hit) {
                if (hit.getEntity() instanceof EndCrystalEntity crystal ) {
                    if (mc.player.getMainHandStack().isOf(Items.END_CRYSTAL)) {
                        mc.interactionManager.attackEntity(mc.player, crystal);
                        mc.player.swingHand(Hand.MAIN_HAND);
                        reset();
                    }
                }
                else if (hit.getEntity() instanceof MagmaCubeEntity magmaCube) {
                    if (mc.player.getMainHandStack().isOf(Items.END_CRYSTAL)) {
                        mc.interactionManager.attackEntity(mc.player, magmaCube);
                        mc.player.swingHand(Hand.MAIN_HAND);
                        reset();
                    }
                }
                else if (hit.getEntity() instanceof SlimeEntity slime) {
                    if (mc.player.getMainHandStack().isOf(Items.END_CRYSTAL)) {
                        mc.interactionManager.attackEntity(mc.player, slime);
                        mc.player.swingHand(Hand.MAIN_HAND);
                        reset();
                    }
                }
            }
        }
    }

    public boolean passedTicks(double time) {
        return tickTimer >= time;
    }

    public void reset() {
        tickTimer = 0;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (modeSetting.isMode("Render")) {
            placeCrystal();
            breakCrystal();
        }
    }

    @Override
    public void onTick() {

        if (activeOnRightClick.isEnabled() && GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_2) != GLFW.GLFW_PRESS)
            return;

        if (nullCheck()) {
            ++tickTimer;
        } else {
            tickTimer = 0;
        }
        if (modeSetting.isMode("Tick")) {
            placeCrystal();
            breakCrystal();
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (activeOnRightClick.isEnabled() && GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_2) != GLFW.GLFW_PRESS)
            return;
        if (modeSetting.isMode("Update")) {
            placeCrystal();
            breakCrystal();
        }
    }

    @EventTarget
    public void onExplosion(EndCrystalExplosionMcPlayerEvent e) {
        placeCrystal();
    }

    @EventTarget
    public void onMove(EventMotion e) {
        if (activeOnRightClick.isEnabled()  && GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_2) != GLFW.GLFW_PRESS)
            return;
        if (modeSetting.isMode("Motion")) {
            placeCrystal();
            breakCrystal();
        }
    }
}