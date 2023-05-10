package net.fabricmc.fabric.systems.module.impl.Cpvp;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.helper.utils.BlockUtils2;
import net.fabricmc.fabric.helper.utils.InventoryUtils;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.awt.event.InputEvent;
@Module.Info(name = "AnchorMacro", description = "FROM ONYX", category = Category.CPVP)
public class AnchorMacro extends Module {
    private boolean isClicking = false;
    private int GlowStonePlaceClock = 0;
    private boolean AnchorActivateQueued = false;
    private boolean GlowStonePlaceQueued;
    private int AnchorActivateClock = 0;
    private boolean isActivatingUnactivatedAnchor = false;
    public NumberSetting delay = new NumberSetting("delay",1,8,1,1);
    public BooleanSetting disableOnCrouch = new BooleanSetting("disable on crouch",false);
    public AnchorMacro() {
        addSettings(delay,disableOnCrouch);
    }
    @Override
    public void onTick() {
        //
        // MAIN ANCHOR MACRO CODE
        //
        if (disableOnCrouch.isEnabled() && mc.player.isSneaking()) {
            return;
        }
        if (mc.player.isHolding(Items.RESPAWN_ANCHOR) && GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_2) == GLFW.GLFW_PRESS && !isClicking) {
            isClicking = true;
            GlowStonePlaceQueued = true;
            GlowStonePlaceClock = (int) delay.getValue();
        }
        if (GlowStonePlaceQueued && !isActivatingUnactivatedAnchor) {
            if (GlowStonePlaceClock == (int) delay.getValue() / 2) {
                InventoryUtils.selectItemFromHotbar(Items.GLOWSTONE);
            }
            if (GlowStonePlaceClock == 0) {
                Vec3d cameraPos = mc.player.getCameraPosVec(0F);
                Vec3d rotationVec = mc.player.getRotationVec(0F);
                Vec3d targetPos = cameraPos.add(rotationVec.multiply(4.5D));
                BlockHitResult hit = mc.world.raycast(new RaycastContext(cameraPos, targetPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, mc.player));
                mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                AnchorActivateQueued = true;
                AnchorActivateClock = (int) delay.getValue();
                GlowStonePlaceQueued = false;
            }
            GlowStonePlaceClock--;
        }
        if (AnchorActivateQueued && !isActivatingUnactivatedAnchor) {
            try {
                if (AnchorActivateClock == (int) delay.getValue() / 2) {
                    InventoryUtils.selectItemFromHotbar(Items.TOTEM_OF_UNDYING);
                }
                if (AnchorActivateClock == 0) {
                    Robot robot = new Robot();
                    robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                    AnchorActivateQueued = false;
                }
            } catch (AWTException e) {
                e.printStackTrace();
            }
            AnchorActivateClock--;
        }
        //
        //ACTIVATE UNACTIVATED ANCHOR MACRO
        //
        if (mc.crosshairTarget instanceof BlockHitResult hit) {
            if (BlockUtils2.isBlock(Blocks.RESPAWN_ANCHOR, hit.getBlockPos())) {
                if (BlockUtils2.getBlockState(hit.getBlockPos()).get(RespawnAnchorBlock.CHARGES) == 0) {
                    if (mc.player.isHolding(Items.TOTEM_OF_UNDYING) && GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_2) == GLFW.GLFW_PRESS && !isClicking) {
                        isClicking = true;
                        GlowStonePlaceQueued = true;
                        GlowStonePlaceClock = (int) delay.getValue();
                        isActivatingUnactivatedAnchor = true;
                    }
                }
            }
        }
        if (isActivatingUnactivatedAnchor) {
            if (GlowStonePlaceQueued) {
                if (GlowStonePlaceClock == (int)delay.getValue() / 2) {
                    InventoryUtils.selectItemFromHotbar(Items.GLOWSTONE);
                }
                if (GlowStonePlaceClock == 0) {
                    try {
                        Robot robot = new Robot();
                        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                        AnchorActivateQueued = true;
                        AnchorActivateClock = (int) delay.getValue();
                        GlowStonePlaceQueued = false;
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }
                }
                GlowStonePlaceClock--;
            }
            if (AnchorActivateQueued) {
                try {
                    if (AnchorActivateClock == (int)delay.getValue() / 2) {
                        InventoryUtils.selectItemFromHotbar(Items.TOTEM_OF_UNDYING);
                    }
                    if (AnchorActivateClock == 0) {
                        Robot robot = new Robot();
                        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                        AnchorActivateQueued = false;
                        isActivatingUnactivatedAnchor = false;
                    }
                } catch (AWTException e) {
                    e.printStackTrace();
                }
                AnchorActivateClock--;
            }
        }
        if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_2) == GLFW.GLFW_RELEASE && isClicking) {
            isClicking = false;
        }
    }
}
